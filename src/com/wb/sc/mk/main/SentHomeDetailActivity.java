package com.wb.sc.mk.main;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.common.net.volley.VolleyErrorHelper;
import com.common.widget.ToastHelper;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wb.sc.R;
import com.wb.sc.activity.base.BaseActivity;
import com.wb.sc.activity.base.ReloadListener;
import com.wb.sc.adapter.SentHomeAdpater;
import com.wb.sc.bean.BaseBean;
import com.wb.sc.bean.OneKmDetail;
import com.wb.sc.bean.SentHome;
import com.wb.sc.config.NetConfig;
import com.wb.sc.config.RespCode;
import com.wb.sc.task.BaseRequest;
import com.wb.sc.task.OneKmRequest;
import com.wb.sc.task.OneKmRequestDetail;
import com.wb.sc.util.Constans;
import com.wb.sc.util.MetaUtil;
import com.wb.sc.util.ParamsUtil;

public class SentHomeDetailActivity extends BaseActivity implements OnMenuItemClickListener, Listener<OneKmDetail>, 
ErrorListener, ReloadListener{

	private PullToRefreshListView mPullToRefreshListView;
	private SentHomeAdpater mAdpter;
	
	private String mKeyword;
	private String sId;
	
	
	private OneKmRequestDetail mOneKmRequestDetail;
	
	private int pageNo;
	private boolean hasNextPage;
	private String mDistrictName;
	private String mMerchantId;
	
	private List<SentHome> list = new ArrayList<SentHome>();
	
	private NetworkImageView merchantLogo;
    private TextView merchantOpenTime;
    private TextView merchantPriceChart;
    private TextView merchantPriceAddr;
    private TextView merchantPriceTel;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_sent_homet_detial);
		getIntentData();
		
	    showLoading();		
		
		requestBase(getBaseRequestParams(), this, this);
	}
	

	@Override
	public void initView() {
		 merchantLogo = (NetworkImageView) findViewById(R.id.merchantLogo);
	    merchantOpenTime = (TextView) findViewById(R.id.merchantPriceChart);
	    merchantPriceChart = (TextView) findViewById(R.id.merchantOpenTime);
	    merchantPriceAddr = (TextView) findViewById(R.id.merchantPriceAddr);
	    merchantPriceTel = (TextView) findViewById(R.id.merchantPriceTel);
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
		mMerchantId = intent.getStringExtra("mMerchantId");
		pageNo = 1;
	}

	@Override
	public boolean onMenuItemClick(MenuItem arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void call(View view) {
		createAlterDialog("缇斯西饼", "0591-87547389");
	}
	
	private void createAlterDialog(String name, final String phoneNum) {
		AlertDialog.Builder builder = new Builder(this);
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
		this.startActivity(intent);  
	}
	
	/**
	 * 获取请求参数,请按照接口文档列表顺序排列
	 * @return
	 */
	private List<String> getBaseRequestParams() {
		List<String> params = new ArrayList<String>();
		params.add(ParamsUtil.getReqParam("FG47", 4));
		params.add(ParamsUtil.getReqParam("MC_CENTERM", 16));
		params.add(ParamsUtil.getReqParam(MetaUtil.readMeta(this, Constans.APP_CHANNEL), 20));
		params.add(ParamsUtil.getReqParam(mMerchantId, 64));
		
		return params;
	}
		
	/**
	 * 执行任务请求
	 * @param method
	 * @param url
	 * @param params
	 * @param listenre
	 * @param errorListener
	 */	
	private void requestBase(List<String> paramsList,	 
			Listener<OneKmDetail> listenre, ErrorListener errorListener) {			
		if(mOneKmRequestDetail != null) {
			mOneKmRequestDetail.cancel();
		}	
		String url = NetConfig.getServerBaseUrl() + NetConfig.EXTEND_URL;
		mOneKmRequestDetail = new OneKmRequestDetail(url, paramsList, listenre, errorListener);
		startRequest(mOneKmRequestDetail);		
	}
	


	@Override
	public void onReload() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onErrorResponse(VolleyError error) {
		showLoadError(this);	
		ToastHelper.showToastInBottom(getApplicationContext(), VolleyErrorHelper.getErrorMessage(this, error));
	}


	@Override
	public void onResponse(OneKmDetail response) {
		if(response.respCode.equals(RespCode.SUCCESS)) {
			showContent();
		    merchantOpenTime.setText(response.merchantOpenTime);
		    merchantPriceAddr.setText(response.merchantPriceAddr);
		    merchantPriceTel.setText(response.merchantPriceTel);
		} else {
			showLoadError(this);
			ToastHelper.showToastInBottom(this, response.respCodeMsg);
		}
	}



}
