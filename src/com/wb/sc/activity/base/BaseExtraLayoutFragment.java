package com.wb.sc.activity.base;

import com.wb.sc.R;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.FrameLayout;
import android.widget.TextView;

public class BaseExtraLayoutFragment extends Fragment implements OnClickListener{
	private FrameLayout rootLayout;
	private ViewGroup emptyContentLayout;
	private TextView emptyToastTv;
	private ViewGroup loadingLayout;
	private ViewGroup loadErrorLayout;
	private ViewGroup contentLayout;		
	
	private ReloadListener listener;
	
	protected View backIv;
	protected View homeIv;
	protected View header_home;
	protected TextView titleTv;                 //添加头部返回
	
	protected View setContentView(LayoutInflater inflater, int layoutResID) {
		rootLayout = (FrameLayout) inflater.inflate(R.layout.root_layout, null);
		
		emptyContentLayout = (ViewGroup) rootLayout.findViewById(R.id.empty_content_layout);
		emptyToastTv = (TextView) emptyContentLayout.findViewById(R.id.toast);
		emptyContentLayout.setVisibility(View.GONE);
		
		loadingLayout = (ViewGroup) rootLayout.findViewById(R.id.loading_layout);
		loadingLayout.setVisibility(View.GONE);
		
		loadErrorLayout = (ViewGroup) rootLayout.findViewById(R.id.load_error_layout);
		loadErrorLayout.setVisibility(View.GONE);
		loadErrorLayout.setOnClickListener(this);
		
		contentLayout = (ViewGroup) rootLayout.findViewById(R.id.content_layout);
		View contentView = inflater.inflate(layoutResID, null);
		contentLayout.addView(contentView);	
		
		return rootLayout;
	}
		
	/**
	 * 设置内容为空时的提示文字
	 * @param toast
	 */
	public void setEmptyToastText(String toast) {
		emptyToastTv.setText(toast);
	}
	
	/**
	 * 设置内容为空时的提示文字
	 * @param toast
	 */
	public void setEmptyToastText(int resId) {
		emptyToastTv.setText(getResources().getString(resId));
	}
	
	/**
	 * 设置内容为空提示是否显示
	 * @param visibility
	 */
	public void showEmpty() {		
		emptyContentLayout.setVisibility(View.VISIBLE);
		loadingLayout.setVisibility(View.GONE);
		contentLayout.setVisibility(View.GONE);
		loadErrorLayout.setVisibility(View.GONE);		
	}
	
	/**
	 * 显示正在加载状态
	 * @param visibility
	 */
	public void showLoading() {		
		loadingLayout.setVisibility(View.VISIBLE);
		contentLayout.setVisibility(View.GONE);
		loadErrorLayout.setVisibility(View.GONE);
		emptyContentLayout.setVisibility(View.GONE);
	}
	
	/**
	 * 加载失败，显示再次加载状态
	 */
	public void showLoadError(ReloadListener listener) {
		loadErrorLayout.setVisibility(View.VISIBLE);
		loadingLayout.setVisibility(View.GONE);
		emptyContentLayout.setVisibility(View.GONE);
		contentLayout.setVisibility(View.GONE);
		
		this.listener = listener;
	}
	
	/**
	 * 显示内容
	 */
	public void showContent() {
		if(contentLayout.getVisibility() != View.VISIBLE) {
			contentLayout.setVisibility(View.VISIBLE);
			contentLayout.startAnimation(getFadeIAnim());
		}
		loadErrorLayout.setVisibility(View.GONE);
		loadingLayout.setVisibility(View.GONE);
		emptyContentLayout.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		if(listener != null) {
			listener.onReload();
		}
	}
	
	/**
	 * 获取淡入效果动画
	 * @return
	 */
	public Animation getFadeIAnim() {
		Animation anim = new AlphaAnimation(0.0f, 1.0f);
		AnimationSet animSet = new AnimationSet(true);
		animSet.addAnimation(anim);
		animSet.setDuration(1000);
		return animSet;
	}
	
	/**
	 * 清除所有Fragment在堆栈中的记录
	 */
	public void cleanAllBackStack() {
		for(int i=0; i<getFragmentManager().getBackStackEntryCount(); i++) {
			getFragmentManager().popBackStack();
		}
	}
	
	public void initHeader(View view, String title) {
		backIv = view.findViewById(R.id.common_header_back);
		backIv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				getActivity().finish();
			}
		});
		homeIv = view.findViewById(R.id.common_header_home);
		titleTv = (TextView)view.findViewById(R.id.common_header_title);
		titleTv.setText(title);
		if(homeIv != null) {
			homeIv.setVisibility(View.GONE);
		}
		
		header_home = view.findViewById(R.id.header_home);
	}
	
	public void setHomeBackground(int resId) {
		homeIv.setBackgroundResource(resId);
		homeIv.setVisibility(View.VISIBLE);
	}
	
	public void setHomeListenner(View.OnClickListener listenner) {
		header_home.setOnClickListener(listenner);
	}
	
}
