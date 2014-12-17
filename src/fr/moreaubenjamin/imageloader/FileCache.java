package fr.moreaubenjamin.imageloader;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by Benjamin on 30/01/14.
 */
public class FileCache {
    private Context mContext;
    private File mCacheDir;

    public FileCache(Context context, String pathExtension, String cacheFolderName) {
        mContext = context;
        if (android.os.Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            mCacheDir = new File(android.os.Environment.getExternalStorageDirectory() + ((pathExtension == null) ? "" : "/" + pathExtension), cacheFolderName);
        } else {
            mCacheDir = mContext.getCacheDir();
        }
        if ((mCacheDir != null) && !mCacheDir.exists()) {
            mCacheDir.mkdirs();
        }
    }

    public File getFile(String url) {
        String fileName = String.valueOf(url.hashCode());
        return new File(mCacheDir, fileName);
    }

    public void clear() {
        File[] files = mCacheDir.listFiles();
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
    }
}
