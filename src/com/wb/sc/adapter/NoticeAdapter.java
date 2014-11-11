package com.wb.sc.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wb.sc.R;
import com.wb.sc.bean.ComNotice;
import com.wb.sc.bean.ComNotice.Item;
import com.wb.sc.mk.personal.BulletinActivity;

public class NoticeAdapter extends PagerAdapter implements OnClickListener{
	
	private Activity mActivity;
	private List<View> mViewList = new ArrayList<View>();
	private ComNotice mNotice;
	
	public NoticeAdapter(Activity activity, ComNotice notice) {
		mActivity = activity;
		mNotice = notice;
		initView();
	}
	
	private void initView() {
		int i=0;
		for(Item item : mNotice.datas) {
			View view = LayoutInflater.from(mActivity).inflate(R.layout.notice_layout, null);
			mViewList.add(view);
			ViewHolder holder = new ViewHolder();
			holder.noticeTv = (TextView) view;
			holder.index = i;
			view.setTag(holder);
			holder.noticeTv.setText("【社区公告】" + item.title);
			view.setOnClickListener(this);
			
			i++;
		}
	}
	
	@Override
	public int getCount() {
		return mViewList.size();
	}
	
	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}
	
	@Override  
    public void destroyItem(ViewGroup container, int position,  
            Object object) {  
        container.removeView(mViewList.get(position));  
    }  
	
	@Override  
    public int getItemPosition(Object object) {  

		return super.getItemPosition(object);  
    } 
	
	@Override  
    public Object instantiateItem(ViewGroup container, int position) { 
		View view = mViewList.get(position);
		container.addView(view);
		return view;
	}
	
	class ViewHolder {
		TextView noticeTv;
		int index;
	}

	@Override
	public void onClick(View v) {
		ViewHolder holder = (ViewHolder) v.getTag();
		Item item = mNotice.datas.get(holder.index);
		Intent intent = new Intent(mActivity, BulletinActivity.class);
		intent.putExtra("title", item.title);
		intent.putExtra("content", item.content);
		intent.putExtra("name", item.source);
		intent.putExtra("time", item.time);
		mActivity.startActivity(intent);
	}
}
