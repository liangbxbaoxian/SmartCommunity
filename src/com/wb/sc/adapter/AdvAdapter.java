package com.wb.sc.adapter;

import java.util.ArrayList;
import java.util.List;

import com.wb.sc.R;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AdvAdapter extends PagerAdapter{
	
	private Activity mActivity;
	private List<View> mViewList = new ArrayList<View>();
	
	public AdvAdapter(Activity activity) {
		mActivity = activity;
		initWeather();
	}
	
	private void initWeather() {
		View view = LayoutInflater.from(mActivity).inflate(R.layout.weather_layout, null);
		mViewList.add(view);
		view = LayoutInflater.from(mActivity).inflate(R.layout.weather_layout, null);
		mViewList.add(view);
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
}
