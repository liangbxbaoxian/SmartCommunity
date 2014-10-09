package com.wb.sc.mk.main;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wb.sc.R;
import com.wb.sc.adapter.SentHomeAdpater;
import com.wb.sc.bean.SentHome;

public class SentHomeDetialActivity extends Activity implements OnMenuItemClickListener{

	private PullToRefreshListView mPullToRefreshListView;
	private SentHomeAdpater mAdpter;
	
	private String mKeyword;
	private String sId;
	
	private int pageNo;
	private boolean hasNextPage;
	private String mDistrictName;
	
	private List<SentHome> list = new ArrayList<SentHome>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_sent_homet_detial);
		getIntentData();
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	public void back (View view) {
		finish();
     }
	
	
	private void initData() {
		String [] name = {"缇斯西饼(洪山桥)", "安德鲁森(洪山桥太阳城店)", "陌上花开(仓山店)", "比哥鸡排", "那时花开"};
		String []  category = {"餐饮", "餐饮", "花店", "餐饮", "花店"};
		int [] resId = {R.drawable.xibing, R.drawable.mianbao, R.drawable.huadian, R.drawable.jipai, R.drawable.nashihuadian};
		for (int i = 0; i < resId.length; i++) {
			SentHome sentHome = new SentHome();
			sentHome.name = name [i];
			sentHome.category = category [i];
			sentHome.resId = resId [i];
			list.add(sentHome);
		}
 	}


	public void getIntentData() {
		Intent intent = getIntent();
		mKeyword = intent.getStringExtra("mKeyword");
		pageNo = 1;
	}

	@Override
	public boolean onMenuItemClick(MenuItem arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	
	

}
