package com.wb.sc.adapter;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.common.media.BitmapHelper;
import com.wb.sc.R;

public class PhotoAdapter extends BaseAdapter {
	
	public int itemWidth = 83;
	
	private Activity mActivity;
	private List<File> fileList;
	private List<SoftReference<Bitmap>> photoList = new ArrayList<SoftReference<Bitmap>>();
	
	public PhotoAdapter(Activity activity, List<File> fileList) {
		mActivity = activity;
		this.fileList = fileList;
		
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		itemWidth = (int) (itemWidth * dm.density);
	}

	@Override
	public int getCount() {

		return fileList.size();
	}

	@Override
	public Object getItem(int position) {

		return fileList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = null;
		ViewHolder holder;
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mActivity);
			view = inflater.inflate(R.layout.photo_item, null);
			holder = new ViewHolder();
			holder.photoIv = (ImageView) view.findViewById(R.id.photo);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		
		Bitmap bmp = null;
		if(position >= photoList.size()) {
			bmp = BitmapHelper.getReSizeBmp(fileList.get(position).getAbsolutePath(), itemWidth, itemWidth, Bitmap.Config.RGB_565);
			photoList.add(new SoftReference<Bitmap>(bmp));
		} else {
			bmp = photoList.get(position).get();
			if(bmp == null) {
				bmp = BitmapHelper.getReSizeBmp(fileList.get(position).getAbsolutePath(), itemWidth, itemWidth, Bitmap.Config.RGB_565);
				photoList.set(position, new SoftReference<Bitmap>(bmp));
			}
		}
		
		if(bmp != null) {
			holder.photoIv.setImageBitmap(bmp);
		}
		
		return view;
	}
	
	class ViewHolder {
		ImageView photoIv;
	}
	
	public void recycleBmp() {
		for(SoftReference<Bitmap> bmp : photoList) {
			if(bmp.get() != null)
				bmp.get().recycle();
		}
	}
}
