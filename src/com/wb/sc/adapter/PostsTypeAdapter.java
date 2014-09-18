package com.wb.sc.adapter;

import com.wb.sc.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PostsTypeAdapter extends BaseAdapter {

	private Context mContext;

	public PostsTypeAdapter(Context context) {
		mContext = context;
	}

	@Override
	public int getCount() {

		return 4;
	}

	@Override
	public Object getItem(int position) {

		return null;
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
			view = inflater.inflate(R.layout.posts_type_item, null);
			holder = new ViewHolder();
			holder.picIv = (ImageView) view.findViewById(R.id.pic);
			holder.dateTv = (TextView) view.findViewById(R.id.date);
			holder.newMsgIv = (ImageView)view.findViewById(R.id.new_msg);
			holder.typeTv = (TextView) view.findViewById(R.id.type);
			holder.titleTv = (TextView) view.findViewById(R.id.title);
			holder.descTv = (TextView) view.findViewById(R.id.desc);
			holder.favourNumTv = (TextView) view.findViewById(R.id.favour_num);
			holder.msgNumTv = (TextView) view.findViewById(R.id.msg_num);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}

		return view;
	}

	class ViewHolder {
		private ImageView picIv;
		private TextView dateTv;
		private ImageView newMsgIv;
		private TextView typeTv;
		private TextView titleTv;		
		private TextView descTv;	
		private TextView favourNumTv;
		private TextView msgNumTv;
	}
}
