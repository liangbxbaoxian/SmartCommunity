package com.wb.sc.adapter;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.toolbox.NetworkImageView;
import com.wb.sc.R;
import com.wb.sc.app.SCApp;
import com.wb.sc.bean.Adv;
import com.wb.sc.bean.Adv.Item;
import com.wb.sc.config.IntentExtraConfig;
import com.wb.sc.config.NetConfig;
import com.wb.sc.mk.browser.BrowserActivity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class AdvAdapter extends PagerAdapter implements OnClickListener{
	
	private Activity mActivity;
	private List<View> mViewList = new ArrayList<View>();	
	
	public AdvAdapter(Activity activity, Adv adv) {
		mActivity = activity;
		initView(adv);
	}
	
	private void initView(Adv adv) {
		for(Item item : adv.datas) {
			View view = LayoutInflater.from(mActivity).inflate(R.layout.adv_layout, null);
			view.setOnClickListener(this);
			mViewList.add(view);
			Holder holder = new Holder();
			holder.imgIv = (NetworkImageView) view.findViewById(R.id.img);
			holder.imgIv.setImageUrl(NetConfig.getPictureUrl(item.imgUrl), SCApp.getInstance().getImageLoader());
			holder.advItem = item;
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
	
	class Holder {
		NetworkImageView imgIv;
		Item advItem;
	}
 
	@Override
	public void onClick(View v) {
		Holder holder = (Holder) v.getTag();
		Intent intent = new Intent(mActivity, BrowserActivity.class);
		intent.putExtra(IntentExtraConfig.BROWSER_TITLE, holder.advItem.title);
		intent.putExtra(IntentExtraConfig.BROWSER_URL, holder.advItem.linkUrl);
		mActivity.startActivity(intent);
	}
}
