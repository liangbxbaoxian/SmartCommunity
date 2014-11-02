package com.wb.sc.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.volley.toolbox.NetworkImageView;
import com.wb.sc.R;
import com.wb.sc.app.SCApp;
import com.wb.sc.config.NetConfig;

public class PostImgAdapter extends BaseAdapter {
		
	private Context mContext;
	private List<String> imgList;
	
	public PostImgAdapter(Context context, List<String> imgList) {
		mContext = context;
		this.imgList = imgList;
	}

	@Override
	public int getCount() {
		return imgList.size();
	}

	@Override
	public Object getItem(int position) {
		return imgList.get(position);
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
			LayoutInflater inflater = LayoutInflater.from(mContext);
			view = inflater.inflate(R.layout.post_item_img, null);
			holder = new ViewHolder();
			holder.imgIv = (NetworkImageView) view.findViewById(R.id.img);	
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		
		holder.imgIv.setImageUrl(NetConfig.getPictureUrl(imgList.get(position)), SCApp.getInstance().getImageLoader());		
		return view;
	}
	
	class ViewHolder {
		NetworkImageView imgIv;;
	}
}
