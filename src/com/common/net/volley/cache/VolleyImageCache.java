package com.common.net.volley.cache;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

public class VolleyImageCache implements ImageCache{
	
	public static final int DEFAULT_MAX_SIZE = 4 * 1024 * 1024;
	
	private LruCache<String, Bitmap> mImageCache;
	
	public VolleyImageCache() {
		initCache(DEFAULT_MAX_SIZE);
	}
	
	public VolleyImageCache(int maxSize) {		
		initCache(maxSize);
	}
	
	@Override
	public Bitmap getBitmap(String url) {
		return mImageCache.get(url);
	}
	
	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		mImageCache.put(url, bitmap);
	}
	
	private void initCache(int maxSize) {
		mImageCache = new LruCache<String, Bitmap>(maxSize) {

			@Override
			protected int sizeOf(String key, Bitmap value) {
				
				return value.getRowBytes()*value.getHeight();
			}						
		};
	}
}
