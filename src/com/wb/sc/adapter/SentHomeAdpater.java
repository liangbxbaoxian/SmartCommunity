package com.wb.sc.adapter;

import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.wb.sc.R;
import com.wb.sc.app.SCApp;
import com.wb.sc.bean.SentHome;
import com.wb.sc.config.NetConfig;

public class SentHomeAdpater extends BaseAdapter {

	private Context mContext;
	private List<?> mList;
	public SentHomeAdpater(Context mContext, List<?> list ) {
		this.mContext = mContext;
		this.mList = list;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if(arg1 == null){
			viewHolder = new ViewHolder();
		    arg1 = LayoutInflater.from(mContext).inflate(R.layout.itme_sent_home, null);
		    viewHolder.networkImageView = (NetworkImageView) arg1.findViewById(R.id.collection_goods_icon);
			viewHolder.district_name = (TextView) arg1.findViewById(R.id.district_name);
			viewHolder.district_address = (TextView) arg1.findViewById(R.id.district_address);
			viewHolder.district  = (TextView) arg1.findViewById(R.id.district);
			viewHolder.call = (ImageView) arg1.findViewById(R.id.call);
			arg1.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) arg1.getTag();
		}
		SentHome sentHome = (SentHome) mList.get(position);
		
		viewHolder.networkImageView.setDefaultImageResId(sentHome.resId);
		viewHolder.networkImageView.setErrorImageResId(sentHome.resId);
		if(sentHome.url != null && !sentHome.url.equals("")) {
			viewHolder.networkImageView.setImageUrl(NetConfig.getPictureUrl(sentHome.url), 
					SCApp.getInstance().getImageLoader());
		}
		viewHolder.call.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				createAlterDialog("", "15980000000");
			}
		});
		viewHolder.district_name.setText(sentHome.name);
		viewHolder.district_address.setText(sentHome.category);
		viewHolder.district.setText(sentHome.distance);
		return arg1;
	}
	
	
	
	public class ViewHolder {
		
		public NetworkImageView networkImageView;
		public TextView district_name;
		public TextView district_address;
		public TextView district;
		public ImageView call;
		
	}
	
	private void createAlterDialog(String name, final String phoneNum) {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setMessage(phoneNum);

		builder.setTitle(name);

		builder.setPositiveButton("呼叫", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				callPhone(phoneNum.split("/")[0]);
				//				dialog.dismiss();

			}
		});

		builder.setNegativeButton("取消", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		builder.create().show();
	}

	private void callPhone(String phoneNum) {
		Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phoneNum));  
		mContext.startActivity(intent);  
	}

}
