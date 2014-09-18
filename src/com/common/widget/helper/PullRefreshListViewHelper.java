package com.common.widget.helper;

import com.wb.sc.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 
 * @描述：上拉下拉刷新控件辅助类，用于添加底部加载状态
 * @作者：liang bao xian
 * @时间：2014年8月4日 上午10:41:40
 */
public class PullRefreshListViewHelper {
	
	//正在加载
	public static final int BOTTOM_STATE_LOADING = 0;
	//加载失败
	public static final int BOTTOM_STATE_LOAD_FAIL = 1;
	//加载完毕，无更多可加载数据
	public static final int BOTTOM_STATE_NO_MORE_DATE = 2;	
	//空闲状态
	public static final int BOTTOM_STATE_LOAD_IDLE = 3;
	
	private Context mContext;
	private ListView mListView;
	private View mBottomView;
	private int mPageSize;
	
	public PullRefreshListViewHelper(Context context, ListView listView) {
		this(context, listView , 10);
	}
	
	public PullRefreshListViewHelper(Context context, ListView listView, int pageSize) {
		mContext = context;
		mListView = listView;
		mPageSize = pageSize;
		
		mBottomView = LayoutInflater.from(context).inflate(R.layout.bottom_loading_layout, null);
		BottomHolder holder = new BottomHolder();
		holder.progressBar = (ProgressBar) mBottomView.findViewById(R.id.loading_processbar);
		holder.stateTv = (TextView) mBottomView.findViewById(R.id.state);
		mBottomView.setTag(holder);
		listView.addFooterView(mBottomView);
	}
		
	/**
	 * 
	 * @描述: 设置底部显示的状态
	 * @param state
	 */
	public void setBottomState(int state) {
		setBottomState(state, mPageSize);
	}
	
	/**
	 * 设置底部显示的状态
	 * @param state
	 * @param pageSize
	 */
	public void setBottomState(int state, int pageSize) {
		BottomHolder holder = (BottomHolder) mBottomView.getTag();
		switch (state) {		
		case BOTTOM_STATE_LOADING:			
			holder.progressBar.setVisibility(View.VISIBLE);
			holder.stateTv.setText(pageSize + mContext.getResources().getString(R.string.pull_refresh_loading));
			break;
			
		case BOTTOM_STATE_LOAD_FAIL:
			holder.progressBar.setVisibility(View.GONE);
			holder.stateTv.setText(mContext.getResources().getString(R.string.pull_refresh_load_fail));
			break;

		case BOTTOM_STATE_NO_MORE_DATE:
			mListView.removeFooterView(mBottomView);
			mBottomView.setVisibility(View.GONE);
			holder.progressBar.setVisibility(View.GONE);
			holder.stateTv.setText("");
			break;
		}
	}
	
	public void setBottomClick(OnClickListener listener) {
		mBottomView.setOnClickListener(listener);
	}
	
	public class BottomHolder {
		private ProgressBar progressBar;
		private TextView stateTv;
	}
	
}
