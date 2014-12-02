package fr.moreaubenjamin.imageloader;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.wb.sc.R;

import fr.moreaubenjamin.imageloader.settings.ImageLoaderSettings;

public class MemoryCache {
    private Map<String, Bitmap> mCache = Collections.synchronizedMap(new LinkedHashMap<String, Bitmap> (10, 1.5f, true));
    private long mSize = 0;
    private long mLimit = 1000000;
    private Context mContext;

    public MemoryCache(Context context) {
        mContext = context;
        setLimit(Runtime.getRuntime().maxMemory() / 4);
    }

    private void setLimit(long newLimit) {
        mLimit = newLimit;
//        Log.i(ImageLoaderSettings.MEMORY_CACHE_TAG, mContext.getString(R.string.memory_cache_set_limit, mLimit / 1024. / 1024.));
    }

    public Bitmap get(String id) {
        try {
            if (!mCache.containsKey(id)) {
                return null;
            }
            return mCache.get(id);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void put(String id, Bitmap bitmap) {
        try {
            if (mCache.containsKey(id)) {
                mSize -= getSizeInBytes(mCache.get(id));
            }
            mCache.put(id, bitmap);
            mSize += getSizeInBytes(bitmap);
            checkSize();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    private void checkSize() {
//        Log.i(ImageLoaderSettings.MEMORY_CACHE_TAG, mContext.getString(R.string.memory_cache_check_size_start, mSize, mCache.size()));
        if (mSize > mLimit) {
            Iterator<Map.Entry<String, Bitmap>> iterator = mCache.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Bitmap> entry = iterator.next();
                mSize -= getSizeInBytes(entry.getValue());
                iterator.remove();
                if (mSize <= mLimit) {
                    break;
                }
            }
//            Log.i(ImageLoaderSettings.MEMORY_CACHE_TAG, mContext.getString(R.string.memory_cache_check_size_end, mCache.size()));
        }
    }

    private long getSizeInBytes(Bitmap bitmap) {
        return (bitmap == null) ? 0 : (bitmap.getRowBytes() * bitmap.getHeight());
    }

    public void clear() {
        try {
            mCache.clear();
            mSize = 0;
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }
}
