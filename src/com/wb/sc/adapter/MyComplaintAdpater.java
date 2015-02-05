package com.wb.sc.adapter;

import java.util.ArrayList;
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
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.common.widget.ToastHelper;
import com.wb.sc.R;
import com.wb.sc.app.SCApp;
import com.wb.sc.bean.CategoryTable;
import com.wb.sc.bean.MyRepair.MyRepairItem;
import com.wb.sc.bean.User;
import com.wb.sc.config.NetConfig;
import com.wb.sc.mk.personal.MyComplaintActivity;
import com.wb.sc.widget.CircleImageView;

public class MyComplaintAdpater extends BaseAdapter {

	private Context mContext;
	private List<MyRepairItem> mList;
	private List<MyRepairItem> mfilter = new ArrayList<MyRepairItem>();
	private int statue;
	
	public MyComplaintAdpater(Context mContext, List<MyRepairItem> list ) {
		this.mContext = mContext;
		this.mList = list;
		this.mfilter.addAll(list);
	}

	@Override
	public int getCount() {
		return mfilter.size();
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
	public void notifyDataSetChanged() {
		setStatue(statue);
		super.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		MyRepairItem repair = (MyRepairItem) mfilter.get(position);
		if(arg1 == null){
			viewHolder = new ViewHolder();
		    arg1 = LayoutInflater.from(mContext).inflate(R.layout.itme_my_complaint, null);
		    viewHolder.gridView = (GridView) arg1.findViewById(R.id.yipay_server);
		    viewHolder.state = (Button) arg1.findViewById(R.id.state);
		    viewHolder.start_time = (TextView) arg1.findViewById(R.id.start_time);
		    viewHolder.hanle_time = (TextView) arg1.findViewById(R.id.hanle_time);
		    viewHolder.finish_time = (TextView) arg1.findViewById(R.id.finish_time);
		    viewHolder.title = (TextView) arg1.findViewById(R.id.title);
		    viewHolder.content = (TextView) arg1.findViewById(R.id.content);
		    viewHolder.progress = (TextView) arg1.findViewById(R.id.tip_progress);
		    viewHolder.networkImageView = (CircleImageView) arg1.findViewById(R.id.collection_goods_icon);
			viewHolder.name = (TextView) arg1.findViewById(R.id.name);
//			viewHolder.district_address = (TextView) arg1.findViewById(R.id.district_address);
//			viewHolder.call = (ImageView) arg1.findViewById(R.id.call);
			arg1.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) arg1.getTag();
		}
//		SentHome sentHome = (SentHome) mList.get(position);
		
		viewHolder.title.setText(repair.repairTitle);
		viewHolder.content.setText(repair.repairContent);
		viewHolder.state.setText(repair.repairStatusName);
		viewHolder.hanle_time.setText(repair.repairHanldeTime);
		
		if ("01".equals(repair.repairStatus)) {  // 未处理
			viewHolder.finish_time.setText("");
			viewHolder.state.setBackgroundResource(R.drawable.shouli);
			viewHolder.progress.setVisibility(View.GONE);
		} else if ("02".equals(repair.repairStatus)) {  // 处理中
			viewHolder.finish_time.setText(repair.repairSubmitTime);
			viewHolder.state.setBackgroundResource(R.drawable.shouli);
			viewHolder.progress.setVisibility(View.VISIBLE);
		} else if ("03".equals(repair.repairStatus)) {  // 已处理
			viewHolder.finish_time.setText(repair.repairEndTime);
			viewHolder.state.setBackgroundResource(R.drawable.chuli);
			viewHolder.progress.setVisibility(View.GONE);
		}
		
		if (repair.repairPhoto.length > 1) {
		List<CategoryTable> list = new ArrayList<CategoryTable>();
		int resId [] = {R.drawable.test_my_complaint_one, R.drawable.test_my_complaint_two};
		for (int i = 0; i < repair.repairPhoto.length; i++) {
			CategoryTable categroy = new CategoryTable();
//			categroy.setId(resId[i]);
			categroy.setCategorylogo(repair.repairPhoto[i]);
			list.add(categroy);
		}
		ImageAdapter adapter = new ImageAdapter(mContext, list);
		viewHolder.gridView.setAdapter(adapter);
		viewHolder.gridView.setVisibility(View.VISIBLE);
		} else {
			viewHolder.gridView.setVisibility(View.GONE);
		}
		
		User user = SCApp.getInstance().getUser();
		
		if (user.getAvatarUrl() != null && !"".equals(user.getAvatarUrl())) {
			SCApp.getInstance().getCommLoader().displayImage(NetConfig.getPictureUrl(user.getAvatarUrl()),  viewHolder.networkImageView , 39, null);
		}
		
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
		viewHolder.name.setText(user.name);
		viewHolder.start_time.setText(repair.repairSubmitTime);
//		viewHolder.district_address.setText(sentHome.category);
		return arg1;
	}
	
	
	
	public class ViewHolder {
		
		public CircleImageView networkImageView;
		public TextView name;
		public TextView district_address;
		public ImageView call;
		public GridView gridView;
		public Button state;
		public TextView start_time;
		public TextView finish_time;
		public TextView hanle_time;
		public TextView progress;
		public TextView content;
		public TextView title;
		
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

	public int getStatue() {
		return statue;
	}

	public void setStatue(int statue) {
		this.statue = statue;
		mfilter.clear();
		if (statue >= 1) {
			for (MyRepairItem item : mList) {                  // 01 未处理   02处理中   03 已处理
				if (("0" + (statue + 1)).equals(item.repairStatus)) {
					mfilter.add(item);
				}
			}
		} else {
			mfilter.addAll(mList);
		}
		
		if (mfilter.size() == 0) {
			String tips = null;
			if (statue == 0) {
				tips = "全部";
			} else if (statue == 1) {
				tips = "已受理";
			} else if (statue == 2) {
				tips = "已处理";
			}
			if (tips != null) {
				ToastHelper.showToastInBottom(mContext, "无"+ tips +"工单数据");
			}
		}
	}

}
