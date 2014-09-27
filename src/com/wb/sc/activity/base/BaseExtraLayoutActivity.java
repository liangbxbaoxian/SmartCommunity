package com.wb.sc.activity.base;

import com.wb.sc.R;

import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * 此类用于处理一些额外信息如：内容为空、正在加载中、加载失败点击重试
 * @author liangbx
 *
 */
public class BaseExtraLayoutActivity extends BaseActionBarActivity implements OnClickListener{
	
	private FrameLayout rootLayout;
	private ViewGroup emptyContentLayout;
	private TextView emptyToastTv;
	private ViewGroup loadingLayout;
	private ViewGroup loadErrorLayout;
	private ViewGroup contentLayout;		
	
	private ReloadListener listener;
	
	@Override
	public void setContentView(int layoutResID) {		
		rootLayout = (FrameLayout) LayoutInflater.from(this).inflate(R.layout.root_layout, null);
		
		emptyContentLayout = (ViewGroup) rootLayout.findViewById(R.id.empty_content_layout);
		emptyToastTv = (TextView) emptyContentLayout.findViewById(R.id.toast);
		emptyContentLayout.setVisibility(View.GONE);
		
		loadingLayout = (ViewGroup) rootLayout.findViewById(R.id.loading_layout);
		loadingLayout.setVisibility(View.GONE);
		
		loadErrorLayout = (ViewGroup) rootLayout.findViewById(R.id.load_error_layout);
		loadErrorLayout.setVisibility(View.GONE);
		loadErrorLayout.setOnClickListener(this);
		
		contentLayout = (ViewGroup) rootLayout.findViewById(R.id.content_layout);
		View contentView = LayoutInflater.from(this).inflate(layoutResID, null);
		contentLayout.addView(contentView);						
		
		super.setContentView(rootLayout);
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
}
