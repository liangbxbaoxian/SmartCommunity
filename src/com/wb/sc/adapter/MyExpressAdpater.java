package com.wb.sc.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.wb.sc.R;
import com.wb.sc.bean.MyExpress.ExpressItem;

public class MyExpressAdpater extends BaseAdapter {

	private Context mContext;
	private List<?> mList;
	
	public MyExpressAdpater(Context mContext, List<?> list ) {
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
		    arg1 = LayoutInflater.from(mContext).inflate(R.layout.itme_my_express, null);
		    
		    viewHolder.cabinetNum = (TextView) arg1.findViewById(R.id.cabinetNum);
		    viewHolder.expressCompany = (TextView) arg1.findViewById(R.id.expressCompany);
		    viewHolder.trackingNum = (TextView) arg1.findViewById(R.id.trackingNum);
		    viewHolder.courierName = (TextView) arg1.findViewById(R.id.courierName);
		    viewHolder.takeNum = (TextView) arg1.findViewById(R.id.takeNum);
		    viewHolder.district_name = (TextView) arg1.findViewById(R.id.district_name);
		    
		    
//		    viewHolder.gridView = (GridView) arg1.findViewById(R.id.yipay_server);
//		    viewHolder.state = (Button) arg1.findViewById(R.id.state);
//		    viewHolder.finish_time = (TextView) arg1.findViewById(R.id.finish_time);
//		    viewHolder.progress = (TextView) arg1.findViewById(R.id.tip_progress);
//		    viewHolder.networkImageView = (NetworkImageView) arg1.findViewById(R.id.collection_goods_icon);
//			viewHolder.district_name = (TextView) arg1.findViewById(R.id.district_name);
//			viewHolder.district_address = (TextView) arg1.findViewById(R.id.district_address);
//			viewHolder.call = (ImageView) arg1.findViewById(R.id.call);
			arg1.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) arg1.getTag();
		}
		ExpressItem item = (ExpressItem) mList.get(position);
	    viewHolder.cabinetNum.setText(item.cabinetNum);
	    viewHolder.expressCompany.setText(item.expressCompany);
	    viewHolder.trackingNum.setText(item.trackingNum);
	    viewHolder.courierName.setText(item.courierName);
	    viewHolder.takeNum.setText(item.takeNum);
	    viewHolder.district_name.setText(item.desc);
		
//		if (isStateChanged) {
//			viewHolder.state.setText("已处理");
//			viewHolder.finish_time.setText("2014-9-19  18:00");
//			viewHolder.state.setBackgroundResource(R.drawable.chuli);
////			viewHolder.progress.setVisibility(View.GONE);
//		} else {
//			viewHolder.state.setText("已受理");
//			viewHolder.finish_time.setText("");
//			viewHolder.state.setBackgroundResource(R.drawable.shouli);
////			viewHolder.progress.setVisibility(View.VISIBLE);
//		}
		
//		List<CategoryTable> list = new ArrayList<CategoryTable>();
//		int resId [] = {R.drawable.test_my_complaint_one, R.drawable.test_my_complaint_two};
//		for (int i = 0; i < resId.length; i++) {
//			CategoryTable categroy = new CategoryTable();
//			categroy.setId(resId[i]);
//			list.add(categroy);
//		}
//		ImageAdapter adapter = new ImageAdapter(mContext, list);
//		viewHolder.gridView.setAdapter(adapter);
		
//		viewHolder.networkImageView.setDefaultImageResId(sentHome.resId);
//		viewHolder.networkImageView.setErrorImageResId(sentHome.resId);
//		if(sentHome.url != null && !sentHome.url.equals("")) {
//			viewHolder.networkImageView.setImageUrl(NetConfig.getPictureUrl(sentHome.url), 
//					SCApp.getInstance().getImageLoader());
//		}
//		viewHolder.call.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				createAlterDialog("", "15980000000");
//			}
//		});
//		viewHolder.district_name.setText(sentHome.name);
//		viewHolder.district_address.setText(sentHome.category);
		return arg1;
	}
	
	
	
	public class ViewHolder {
		
		public NetworkImageView networkImageView;
		public TextView district_name;
		public TextView district_address;
		public ImageView call;
		public GridView gridView;
		public Button state;
		public TextView finish_time;
		
		public TextView cabinetNum;
		public TextView expressCompany;
		public TextView trackingNum;
		public TextView courierName;
		public TextView takeNum;
		
	}
	

}
