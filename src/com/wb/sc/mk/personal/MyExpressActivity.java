package com.wb.sc.mk.personal;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wb.sc.R;
import com.wb.sc.activity.base.BaseHeaderActivity;
import com.wb.sc.adapter.MyComplaintAdpater;
import com.wb.sc.bean.SentHome;

public class MyExpressActivity extends BaseHeaderActivity implements OnMenuItemClickListener, OnClickListener{

	private PullToRefreshListView mPullToRefreshListView;
	private MyComplaintAdpater mAdpter;

	private String mKeyword;
	private String sId;

	private int pageNo;
	private boolean hasNextPage;
	private String mDistrictName;

	private List<SentHome> list = new ArrayList<SentHome>();


	private View current_express;
	private View deprecated_express;
	private View history_express;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_express);
		initView();
		initHeader(R.string.ac_my_express);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	public void back (View view) {
		finish();
	}

	public void initView() {
		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_scroll);
		mPullToRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				new GetDataTask().execute();
			}
		});

		mPullToRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				// TODO Auto-generated method stub
			}
		});


		mPullToRefreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(MyExpressActivity.this, MyExpressActivity.class);
				startActivity(intent);
			}
		});


		//		initData();
		mAdpter = new MyComplaintAdpater(MyExpressActivity.this, list);
		mPullToRefreshListView.setDividerDrawable(null);
		mPullToRefreshListView.setAdapter(mAdpter);


		current_express = findViewById(R.id.current_express);
		current_express.setSelected(true);
		current_express.setOnClickListener(this);

		deprecated_express = findViewById(R.id.deprecated_express);
		deprecated_express.setSelected(false);
		deprecated_express.setOnClickListener(this);

		history_express = findViewById(R.id.history_express);
		history_express.setSelected(false);
		history_express.setOnClickListener(this);

	}

	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {
			mAdpter.notifyDataSetChanged();

			// Call onRefreshComplete when the list has been refreshed.
			mPullToRefreshListView.onRefreshComplete();

			super.onPostExecute(result);
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


	@Override
	public void onClick(View v) {

		switch(v.getId()) {
		case R.id.deprecated_express:
			//contentVp.setCurrentItem(0);
			deprecated_express.setSelected(true);
			current_express.setSelected(false);
			history_express.setSelected(false);
			break;

		case R.id.current_express:
			//contentVp.setCurrentItem(1);
			deprecated_express.setSelected(false);
			current_express.setSelected(true);
			history_express.setSelected(false);
			break;
		case R.id.history_express:
			//	contentVp.setCurrentItem(1);
			deprecated_express.setSelected(false);
			current_express.setSelected(false);
			history_express.setSelected(true);
			break;			

		case R.id.common_header_back:
			finish();
			break;			
		}
	}



}
