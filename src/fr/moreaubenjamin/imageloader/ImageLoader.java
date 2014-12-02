package fr.moreaubenjamin.imageloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.ImageView;

import com.wb.sc.R;

import fr.moreaubenjamin.imageloader.settings.ImageLoaderSettings;

public class ImageLoader {
    private Context mContext;
    private MemoryCache mMemoryCache;
    private FileCache mFileCache;
    private Map<ImageView, String> mImageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String> ());
    private ExecutorService mExecutorService;
    private Handler mHandler = new Handler();
    private int mLoadingPictureResource;
    private int mNoPictureResource;
    private ImageView.ScaleType mLoadingScaleType;

    public ImageLoader(Context context, String pathExtension, String cacheFolderName, int loadingPictureResource, int noPictureResource, ImageView.ScaleType loadingScaleType) {
        mContext = context;
        mMemoryCache = new MemoryCache(mContext);
        mFileCache = new FileCache(mContext, pathExtension, cacheFolderName);
        mExecutorService = Executors.newFixedThreadPool(ImageLoaderSettings.THREAD_NUMBER);
        mLoadingPictureResource = (loadingPictureResource == -1) ? R.drawable.ic_launcher : loadingPictureResource;
        mNoPictureResource = (noPictureResource == -1) ? R.drawable.ic_launcher : noPictureResource;
        if (loadingScaleType != null) {
            mLoadingScaleType = loadingScaleType;
        }
    }

    public void displayImage(String url, ImageView imageView, int requiredSize, ImageView.ScaleType scaleType) {
        if (!TextUtils.isEmpty(url) && (url.startsWith("http://") || url.startsWith("https://"))) {
            url += ImageLoaderSettings.SEPARATOR + requiredSize;
            mImageViews.put(imageView, url);
            Bitmap bitmap = mMemoryCache.get(url);
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
                if (scaleType != null) {
                    imageView.setScaleType(scaleType);
                }
            } else {
                queuePhoto(url, imageView, requiredSize, scaleType);
                imageView.setImageResource(mLoadingPictureResource);
                if (mLoadingScaleType != null) {
                    imageView.setScaleType(mLoadingScaleType);
                }
            }
        } else {
            imageView.setImageResource(mNoPictureResource);
            if (scaleType != null) {
                imageView.setScaleType(scaleType);
            }
        }
    }

    private void queuePhoto(String url, ImageView imageView, int requiredSize, ImageView.ScaleType scaleType) {
        PhotoToLoad photoToLoad = new PhotoToLoad(url, imageView, requiredSize, scaleType);
        mExecutorService.submit(new PhotosLoader(photoToLoad));
    }

    private class PhotoToLoad {
        public String mUrl;
        public ImageView mImageView;
        public int mRequiredSize;
        public ImageView.ScaleType mScaleType;

        public PhotoToLoad(String url, ImageView imageView, int requiredSize, ImageView.ScaleType scaleType) {
            mUrl = url;
            mImageView = imageView;
            mRequiredSize = requiredSize;
            mScaleType = scaleType;
        }
    }

    private class PhotosLoader implements Runnable {
        private PhotoToLoad mPhotoToLoad;

        public PhotosLoader(PhotoToLoad photoToLoad) {
            mPhotoToLoad = photoToLoad;
        }

        @Override
        public void run() {
            try {
                if (imageViewReused(mPhotoToLoad)) {
                    return;
                }
                String url = mPhotoToLoad.mUrl;
                if (url.contains(ImageLoaderSettings.SEPARATOR)) {
                    StringTokenizer tokens = new StringTokenizer(url, ImageLoaderSettings.SEPARATOR);
                    url = tokens.nextToken();
                }
                Bitmap bitmap = getBitmap(url, mPhotoToLoad.mRequiredSize);
                mMemoryCache.put(mPhotoToLoad.mUrl, bitmap);
                if (imageViewReused(mPhotoToLoad)) {
                    return;
                }
                BitmapDisplayer bitmapDisplayer = new BitmapDisplayer(bitmap, mPhotoToLoad);
                mHandler.post(bitmapDisplayer);
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
    }

    boolean imageViewReused(PhotoToLoad photoToLoad) {
        String tag = mImageViews.get(photoToLoad.mImageView);
        return (tag == null) || !tag.equals(photoToLoad.mUrl);
    }

    private Bitmap getBitmap(String url, int requiredSize) {
        File file = mFileCache.getFile(url);

        Bitmap bitmap1 = decodeFile(file, requiredSize);
        if (bitmap1 != null) {
            return bitmap1;
        }

        try {
            Bitmap bitmap2;
            URL imageURL = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) imageURL.openConnection();
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(30000);
            connection.setInstanceFollowRedirects(true);
            InputStream inputStream = connection.getInputStream();
            OutputStream outputStream = new FileOutputStream(file);
            copyStream(inputStream, outputStream);
            outputStream.close();
            bitmap2 = decodeFile(file, requiredSize);
            return bitmap2;
        } catch (Throwable ex) {
            ex.printStackTrace();;
            if (ex instanceof OutOfMemoryError) {
                mMemoryCache.clear();
            }
            return null;
        }
    }

    private Bitmap decodeFile(File file, int requiredSize) {
        try {
            BitmapFactory.Options options1 = new BitmapFactory.Options();
            options1.inJustDecodeBounds = true;
            FileInputStream inputStream1 = new FileInputStream(file);
            BitmapFactory.decodeStream(inputStream1, null, options1);
            inputStream1.close();

            int widthTmp = options1.outWidth;
            int heightTmp = options1.outHeight;
            int scale = 1;
            if (requiredSize > -1) {
                while (true) {
                    if (((widthTmp / 2) < requiredSize) || ((heightTmp / 2) < requiredSize)) {
                        break;
                    }
                    widthTmp /= 2;
                    heightTmp /= 2;
                    scale *= 2;
                }
            }

            BitmapFactory.Options options2 = new BitmapFactory.Options();
            options2.inSampleSize = scale;
            FileInputStream inputStream2 = new FileInputStream(file);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream2, null, options2);
            inputStream2.close();
            return bitmap;
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private void copyStream(InputStream inputStream, OutputStream outputStream) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for(  ; ; ) {
                int count = inputStream.read(bytes, 0, buffer_size);
                if (count == -1) {
                    break;
                }
                outputStream.write(bytes, 0, count);
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    private class BitmapDisplayer implements Runnable {
        private Bitmap mBitmap;
        private PhotoToLoad mPhotoToLoad;

        public BitmapDisplayer(Bitmap bitmap, PhotoToLoad photoToLoad) {
            mBitmap = bitmap;
            mPhotoToLoad = photoToLoad;
        }

        @Override
        public void run() {
            if (imageViewReused(mPhotoToLoad)) {
                return;
            }
            if (mBitmap != null) {
                mPhotoToLoad.mImageView.setImageBitmap(mBitmap);
                if (mPhotoToLoad.mScaleType != null) {
                    mPhotoToLoad.mImageView.setScaleType(mPhotoToLoad.mScaleType);
                }
            } else {
                mPhotoToLoad.mImageView.setImageResource(mNoPictureResource);
                if (mPhotoToLoad.mScaleType != null) {
                    mPhotoToLoad.mImageView.setScaleType(mPhotoToLoad.mScaleType);
                }
            }
        }
    }

    public void clearCache() {
        mMemoryCache.clear();
        mFileCache.clear();
    }
}
