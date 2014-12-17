package com.wb.sc.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wb.sc.R;
import com.wb.sc.bean.Adv;
import com.wb.sc.bean.Adv.Item;
import com.wb.sc.config.IntentExtraConfig;
import com.wb.sc.mk.browser.BrowserActivity;

public class NewsAdapter extends PagerAdapter implements OnClickListener{
	
	private Activity mActivity;
	private List<View> mViewList = new ArrayList<View>();	
	
	public NewsAdapter(Activity activity, Adv adv) {
		mActivity = activity;
		initView(adv);
	}
	
	private void initView(Adv adv) {
		for(Item item : adv.datas) {
			View view = LayoutInflater.from(mActivity).inflate(R.layout.news_item_layout, null);
			view.setOnClickListener(this);
			mViewList.add(view);
			Holder holder = new Holder();
			holder.nameTv = (TextView) view.findViewById(R.id.news_name);
			holder.timeTv = (TextView) view.findViewById(R.id.news_time);
			holder.advItem = item;
			view.setTag(holder);
			
			holder.nameTv.setText(item.title);
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
		TextView nameTv;
		TextView timeTv;
		Item advItem;
	}

	@Override
	public void onClick(View v) {
		Holder holder = (Holder) v.getTag();
		Intent intent = new Intent(mActivity, BrowserActivity.class);
		if(!TextUtils.isEmpty(holder.advItem.title)) {
			intent.putExtra(IntentExtraConfig.BROWSER_TITLE, holder.advItem.title);
		}
		if(!TextUtils.isEmpty(holder.advItem.linkUrl)) {
			intent.putExtra(IntentExtraConfig.BROWSER_URL, holder.advItem.linkUrl);
		}
		mActivity.startActivity(intent);
	}
}
