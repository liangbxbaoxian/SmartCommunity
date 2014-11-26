package com.wb.sc.mk.post;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.NetworkImageView.NetworkImageListener;
import com.common.media.BitmapHelper;
import com.common.net.volley.VolleyErrorHelper;
import com.common.util.PageInfo;
import com.common.widget.ToastHelper;
import com.common.widget.helper.PullRefreshListViewHelper;
import com.common.widget.hzlib.HorizontalAdapterView;
import com.common.widget.hzlib.HorizontalListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wb.sc.R;
import com.wb.sc.activity.base.BaseHeaderActivity;
import com.wb.sc.activity.base.ReloadListener;
import com.wb.sc.adapter.CommentListAdapter;
import com.wb.sc.adapter.PostImgAdapter;
import com.wb.sc.app.SCApp;
import com.wb.sc.bean.Comment;
import com.wb.sc.bean.CommentList;
import com.wb.sc.bean.Favour;
import com.wb.sc.bean.ImagesItem;
import com.wb.sc.bean.PostDetail;
import com.wb.sc.config.IntentExtraConfig;
import com.wb.sc.config.NetConfig;
import com.wb.sc.config.RespCode;
import com.wb.sc.dialog.OptDialog;
import com.wb.sc.dialog.ToastLoginDialog;
import com.wb.sc.mk.img.ImageBrowseActivity;
import com.wb.sc.task.CommentListRequest;
import com.wb.sc.task.CommentRequest;
import com.wb.sc.task.FavourRequest;
import com.wb.sc.task.PostDetailRequest;
import com.wb.sc.util.ParamsUtil;

public class PostDetailActivity extends BaseHeaderActivity implements Listener<PostDetail>, 
	ErrorListener, ReloadListener, OnItemClickListener, NetworkImageListener, 
	com.common.widget.hzlib.HorizontalAdapterView.OnItemClickListener{
	
	private NetworkImageView avatarIv;
	private TextView nameTv;
	private TextView titleTv;
	private TextView timeTv;
	private TextView descTv;
	private TextView msgNumTv;
	private TextView favNumTv;
	private HorizontalListView imgLv;
	
	private View favBtn;
	private EditText commentContentEt;
	private ImageButton commentBtn;
	
	//帖子ID
	private String postId;
	
	//帖子详情
	private PostDetailRequest mPostDetailRequest;
	private PostDetail mPostDetail;
	
	//评论列表
	private PullToRefreshListView mPullListView;
	private PullRefreshListViewHelper mPullHelper;
	private ListView mListView;
	private CommentListAdapter mAdapter;
	private PageInfo mPage = new PageInfo();
	private int loadState = PullRefreshListViewHelper.BOTTOM_STATE_LOAD_IDLE;		
	private CommentListRequest mCommentListRequest;
	private CommentListListener mCommListListener = new CommentListListener();
	private CommentList mCommentList;
	
	//发表评论
	private CommentRequest mCommentRequest;
	private CommentListener mCommentListener = new CommentListener();
	
	//点赞
	private FavourRequest mFavourRequest;
	private FavourListener mFavourListener = new FavourListener();
	
	//操作对话框
	private OptDialog mOptDialog;
	
	//我要报名
	private Button applyBtn;
	private String postType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_header_layout, R.layout.activity_postdetail);
		
		getIntentData();
		initHeader(getResources().getString(R.string.ac_post_detail));
		initView();				
		
		showLoading();
		
		//获取详情信息
		requestPostDetail(getPostDetailRequestParams(), this, this);
		//获取评论列表		
		startCommentListRequest();
//		test();
	}
			
	@Override
	public void getIntentData() {
		postId = getIntent().getStringExtra(IntentExtraConfig.DETAIL_ID);
		postType = getIntent().getStringExtra(IntentExtraConfig.POST_TYPE);
	}
	
	@Override
	public void initView() {
		avatarIv = (NetworkImageView) findViewById(R.id.avatar);
        nameTv = (TextView) findViewById(R.id.postMaster);
        titleTv = (TextView) findViewById(R.id.postTitle);
        timeTv = (TextView) findViewById(R.id.postTime);
        descTv = (TextView) findViewById(R.id.postName);
        msgNumTv = (TextView) findViewById(R.id.msg_num);
        favNumTv = (TextView) findViewById(R.id.favourite_num);
        imgLv = (HorizontalListView) findViewById(R.id.list);
        imgLv.setOnItemClickListener(this);
		
		mPullListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);		
		mPullListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				//处理下拉刷新
				mPage.pageNo = 1;
				startCommentListRequest();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				//处理上拉加载
				mPage.pageNo++;		
				startCommentListRequest();
			}
		});
		
		mPullListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				//滑动到底部的处理
				if(loadState == PullRefreshListViewHelper.BOTTOM_STATE_LOAD_IDLE && mCommentList.hasNextPage) {
					loadState = PullRefreshListViewHelper.BOTTOM_STATE_LOADING;
					mPage.pageNo++;		
					startCommentListRequest();
				}
			}
		});
		
		//设置刷新时请允许滑动的开关使能   		
		mPullListView.setScrollingWhileRefreshingEnabled(true);
		
		//设置自动刷新功能
		mPullListView.setRefreshing(false);
		
		//设置拉动模式
		mPullListView.setMode(Mode.PULL_FROM_END);
		
		mListView = mPullListView.getRefreshableView();
		mListView.setOnItemClickListener(this);
		
		mPage = new PageInfo();
		mPullHelper = new PullRefreshListViewHelper(this, mListView, mPage.pageSize);
		mPullHelper.setBottomClick(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(loadState == PullRefreshListViewHelper.BOTTOM_STATE_LOAD_FAIL) {
					//加载失败，点击重试
					loadState = PullRefreshListViewHelper.BOTTOM_STATE_LOADING;
					mPullHelper.setBottomState(loadState);		
					startCommentListRequest();
				}
			}
		});
		
		favBtn = findViewById(R.id.fav);
		favBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(!ToastLoginDialog.checkLogin(mActivity)) {
					return;
				}
				
				requestFavour(getFavourRequestParams(), mFavourListener, PostDetailActivity.this);
			}
		});
		
		commentContentEt = (EditText) findViewById(R.id.comment_content);
		commentBtn = (ImageButton) findViewById(R.id.comment);
		commentBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String content = commentContentEt.getText().toString();
				
				if(TextUtils.isEmpty(content)) {
					ToastHelper.showToastInBottom(mActivity, "评论不能为空");
					return;
				}
				
				if(!ToastLoginDialog.checkLogin(mActivity)) {
					return;
				}
				
				showProcess("正在发表评论...");
				requestComment(getCommentRequestParams(content), mCommentListener, PostDetailActivity.this);
			}
		});
		
		applyBtn = (Button) findViewById(R.id.apply);
		applyBtn.setOnClickListener(this);
		if(postType.equals("04")) {
			applyBtn.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * 评论列表选项点击的处理
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
//		if(mOptDialog == null) {
//			mOptDialog = new OptDialog(this, R.style.popupStyle);
//			mOptDialog.setOpt1Btn("回复", true);
//			mOptDialog.setOpt2Btn("复制", true);
//			mOptDialog.setOpt3Btn("", false);
//			mOptDialog.setListener(this);
//		}
//		
//		mOptDialog.show();
	}
	
	@Override
	public void onClick(View v) {
		
		
		switch(v.getId()) {
		case R.id.opt_1:
			break;
			
		case R.id.opt_2:
			break;		
			
		case R.id.common_header_back:{
			Intent intent = new Intent();
			intent.putExtra(IntentExtraConfig.MSG_NUM, mPostDetail.commentNum);
			intent.putExtra(IntentExtraConfig.FAV_NUM, mPostDetail.favNum);
			setResult(0, intent);
		}break;	
			
		case R.id.apply:{
			Intent intent = new Intent(this, ApplyActivity.class);
			startActivity(intent);
		}break;
		}
		
		super.onClick(v);
	}
	
	public boolean onKeyDown (int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.putExtra(IntentExtraConfig.MSG_NUM, mPostDetail.commentNum);
			intent.putExtra(IntentExtraConfig.FAV_NUM, mPostDetail.favNum);
			setResult(0, intent);
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
		
	/**
	 * 获取请求参数
	 * @return
	 */
	private List<String> getPostDetailRequestParams() {
		List<String> params = new ArrayList<String>();
		params.add(ParamsUtil.getReqParam("FG35", 4));
		params.add(ParamsUtil.getReqParam("MC_CENTERM", 16));
		params.add(ParamsUtil.getReqParam("00001", 20));	
		params.add(ParamsUtil.getReqParam(SCApp.getInstance().getUser().userId, 64));
		params.add(ParamsUtil.getReqParam(postId, 64));
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
	private void requestPostDetail(List<String> params,	 
			Listener<PostDetail> listenre, ErrorListener errorListener) {			
		if(mPostDetailRequest != null) {
			mPostDetailRequest.cancel();
		}	
		String url = NetConfig.getServerBaseUrl() + NetConfig.EXTEND_URL;
		mPostDetailRequest = new PostDetailRequest(url, params, listenre, errorListener);
		startRequest(mPostDetailRequest);		
	}
	
	/**
	 * 网络请求错误处理
	 *
	 */
	@Override
	public void onErrorResponse(VolleyError error) {	
		showLoadError(this);	
		ToastHelper.showToastInBottom(getApplicationContext(), VolleyErrorHelper.getErrorMessage(this, error));
	}
	
	/**
	 * 重新加载请求
	 */
	@Override
	public void onReload() {
		showLoading();
		//requestPostDetail(Method.GET, "请求方法", getPostDetailRequestParams(), this, this);			
	}
	
	/**
	 * 请求完成，处理UI更新
	 */
	@Override
	public void onResponse(PostDetail response) {		
		showContent();	
		if(response.respCode.equals(RespCode.SUCCESS)) {			
			mPostDetail = response;
			avatarIv.setImageUrl(NetConfig.getPictureUrl(mPostDetail.sourceAvatarUrl), SCApp.getInstance().getImageLoader());
			nameTv.setText(mPostDetail.sourceName);
			titleTv.setText(mPostDetail.title);
			timeTv.setText(mPostDetail.time);
			descTv.setText(mPostDetail.content);
			msgNumTv.setText(mPostDetail.commentNum+"");
		    favNumTv.setText(mPostDetail.favNum+"");
//		    mPostDetail.imgList.clear();
//		    mPostDetail.imgList.add("http://img5.cache.netease.com/photo/0001/2014-11-02/AA2G0LS100AN0001.jpg");
//		    mPostDetail.imgList.add("http://img5.cache.netease.com/photo/0001/2014-11-02/AA2G0LS100AN0001.jpg");
//		    mPostDetail.imgList.add("http://img5.cache.netease.com/photo/0001/2014-11-02/AA2G0LS100AN0001.jpg");
//		    mPostDetail.imgList.add("http://img5.cache.netease.com/photo/0001/2014-11-02/AA2G0LS100AN0001.jpg");
//		    mPostDetail.imgList.add("http://img5.cache.netease.com/photo/0001/2014-11-02/AA2G0LS100AN0001.jpg");
//		    mPostDetail.imgList.add("http://img5.cache.netease.com/photo/0001/2014-11-02/AA2G0LS100AN0001.jpg");
//		    mPostDetail.imgList.add("http://img5.cache.netease.com/photo/0001/2014-11-02/AA2G0LS100AN0001.jpg");
//		    mPostDetail.imgList.add("http://img5.cache.netease.com/photo/0001/2014-11-02/AA2G0LS100AN0001.jpg");
//		    mPostDetail.imgList.add("http://img5.cache.netease.com/photo/0001/2014-11-02/AA2G0LS100AN0001.jpg");
//		    mPostDetail.imgList.add("http://img5.cache.netease.com/photo/0001/2014-11-02/AA2G0LS100AN0001.jpg");
//		    mPostDetail.imgList.add("http://img5.cache.netease.com/photo/0001/2014-11-02/AA2G0LS100AN0001.jpg");
		    if(mPostDetail.imgList.size() > 0) {
		    	PostImgAdapter adapter = new PostImgAdapter(this, mPostDetail.imgList);
		    	imgLv.setAdapter(adapter);
		    } else {
		    	imgLv.setVisibility(View.GONE);
		    }
		} else {
			ToastHelper.showToastInBottom(this, response.respCodeMsg);
		}
	}
	
	/**
	 * 
	 * @描述:启动请求
	 */
	private void startCommentListRequest() {
		//requestComment(Method.GET, "请求方法", getCommentRequestParams(), this, this);
		requestCommentList(getCommentListRequestParams(), mCommListListener, this);
	}
	
	/**
	 * 获取请求参数
	 * @return
	 */
	private List<String> getCommentListRequestParams() {
		List<String> params = new ArrayList<String>();
		params.add(ParamsUtil.getReqParam("FG54", 4));
		params.add(ParamsUtil.getReqParam("MC_CENTERM", 16));
		params.add(ParamsUtil.getReqParam("00001", 20));
		params.add(ParamsUtil.getReqParam(SCApp.getInstance().getUser().userId, 64));
		params.add(ParamsUtil.getReqParam(postId, 64));
		params.add(ParamsUtil.getReqIntParam(mPage.pageNo, 3));
		params.add(ParamsUtil.getReqIntParam(mPage.pageSize, 2));
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
	private void requestCommentList(List<String> params,	 
			Listener<CommentList> listenre, ErrorListener errorListener) {			
		if(mCommentListRequest != null) {
			mCommentListRequest.cancel();
		}	
		String url = NetConfig.getServerBaseUrl() + NetConfig.EXTEND_URL ;
		mCommentListRequest = new CommentListRequest(url, params, listenre, errorListener);
		startRequest(mCommentListRequest);		
	}
	
	/**
	 * 
	 * @描述：最新评论处理
	 * @作者：liang bao xian
	 * @时间：2014年9月27日 上午11:27:15
	 */
	class CommentListListener implements Listener<CommentList>, ErrorListener, ReloadListener {
		/**
		 * 请求完成，处理UI更新
		 */
		@Override
		public void onResponse(CommentList response) {		
			showContent();	
			if(response.respCode.equals(RespCode.SUCCESS)) {			
				if(response.datas.size() <= 0) {					
					return;
				}
				
				if(mPage.pageNo == 1) {
					mCommentList = response;
					mAdapter = new CommentListAdapter(mActivity, mCommentList);
					mListView.setAdapter(mAdapter);
				} else {
					mCommentList.hasNextPage = response.hasNextPage;
					mCommentList.datas.addAll(response.datas);
					mAdapter.notifyDataSetChanged();
				}
				
				loadState = PullRefreshListViewHelper.BOTTOM_STATE_LOAD_IDLE;	
				if(mCommentList.hasNextPage) {
					mPullHelper.setBottomState(PullRefreshListViewHelper.BOTTOM_STATE_LOADING);
				} else {
					mPullHelper.setBottomState(PullRefreshListViewHelper.BOTTOM_STATE_NO_MORE_DATE);
				}		
			} else {
				ToastHelper.showToastInBottom(mActivity, response.respCodeMsg);
			}
		}
		
		@Override
		public void onReload() {
			mPage.pageNo = 1;		
			showLoading();
			startCommentListRequest();
		}
	
		/**
		 * 网络请求错误处理
		 *
		 */
		@Override
		public void onErrorResponse(VolleyError error) {	
			mPullListView.onRefreshComplete();		
			ToastHelper.showToastInBottom(getApplicationContext(), 
					VolleyErrorHelper.getErrorMessage(mActivity, error));
		
			if(mPage.pageNo == 1) {
				showLoadError(this);
			} else {
				loadState = PullRefreshListViewHelper.BOTTOM_STATE_LOAD_FAIL;
				mPullHelper.setBottomState(PullRefreshListViewHelper.BOTTOM_STATE_LOAD_FAIL, mPage.pageSize);
			}
		}
	}
	
	/**
	 * 获取请求参数
	 * @return
	 */
	private List<String> getFavourRequestParams() {
		List<String> params = new ArrayList<String>();
		params.add(ParamsUtil.getReqParam("FG31", 4));
		params.add(ParamsUtil.getReqParam("MC_CENTERM", 16));
		params.add(ParamsUtil.getReqParam("00001", 20));
		params.add(ParamsUtil.getReqParam(SCApp.getInstance().getUser().userId, 64));
		params.add(ParamsUtil.getReqParam(postId, 64));	
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
	private void requestFavour(List<String> params,	 
			Listener<Favour> listenre, ErrorListener errorListener) {			
		if(mFavourRequest != null) {
			mFavourRequest.cancel();
		}	
		String url = NetConfig.getServerBaseUrl() + NetConfig.EXTEND_URL;
		mFavourRequest = new FavourRequest(url, params, listenre, errorListener);
		startRequest(mFavourRequest);		
	}
	
	/**
	 * 
	 * @描述：点赞监听
	 * @作者：liang bao xian
	 * @时间：2014年10月27日 下午3:28:03
	 */
	class FavourListener implements Listener<Favour> {

		@Override
		public void onResponse(Favour response) {
			if(response.respCode.equals(RespCode.SUCCESS)) {		
				mPostDetail.favNum++;
				favNumTv.setText(mPostDetail.favNum+"");
				ToastHelper.showToastInBottom(PostDetailActivity.this, "谢谢您的点赞");
			} else {
				ToastHelper.showToastInBottom(PostDetailActivity.this, response.respCodeMsg);
			}
		}		
	}
	
	/**
	 * 获取请求参数
	 * @return
	 */
	private List<String> getCommentRequestParams(String content) {
		List<String> params = new ArrayList<String>();
		params.add(ParamsUtil.getReqParam("FG32", 4));
		params.add(ParamsUtil.getReqParam("MC_CENTERM", 16));
		params.add(ParamsUtil.getReqParam("00001", 20));
		params.add(ParamsUtil.getReqParam(SCApp.getInstance().getUser().userId, 64));
		params.add(ParamsUtil.getReqParam(postId, 64));				
		params.add(ParamsUtil.getReqParam("", 64));
		params.add(ParamsUtil.getReqParam(content, 250));
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
	private void requestComment(List<String> params,	 
			Listener<Comment> listenre, ErrorListener errorListener) {			
		if(mCommentRequest != null) {
			mCommentRequest.cancel();
		}	
		String url = NetConfig.getServerBaseUrl() + NetConfig.EXTEND_URL;
		mCommentRequest = new CommentRequest(url, params, listenre, errorListener);
		startRequest(mCommentRequest);		
	}
	
	/**
	 * 
	 * @描述：发表评论监听
	 * @作者：liang bao xian
	 * @时间：2014年10月27日 下午3:13:33
	 */
	class CommentListener implements Listener<Comment> {
		
		@Override
		public void onResponse(Comment response) {
			if(mPullListView.isRefreshing()) {
				mPullListView.onRefreshComplete();
			}
			dismissProcess();
			commentContentEt.setText("");
			if(response.respCode.equals(RespCode.SUCCESS)) {			
				ToastHelper.showToastInBottom(PostDetailActivity.this, "评论成功");
				mPostDetail.commentNum++;
				msgNumTv.setText(mPostDetail.commentNum+"");
				//刷新评论列表
				mPage.pageNo = 1;		
				startCommentListRequest();
			} else {
				ToastHelper.showToastInBottom(PostDetailActivity.this, response.respCodeMsg);
			}
		}		
	}
	
	private void test() {
		mCommentList = new CommentList();
		mCommentList.datas = new ArrayList<CommentList.Item>();
		for(int i=0; i<10; i++) {
			mCommentList.datas.add(mCommentList.new Item());
		}
		mAdapter = new CommentListAdapter(this, mCommentList);
		mListView.setAdapter(mAdapter);
	}

	@Override
	public void onGetBitmapListener(ImageView imageView, Bitmap bitmap) {
		Bitmap roundBmp = BitmapHelper.toRoundCorner(bitmap, bitmap.getHeight()/2);
		imageView.setImageBitmap(roundBmp);		
	}
	
	@Override
	public void onItemClick(HorizontalAdapterView<?> parent, View view,
			int position, long id) {
		ArrayList<ImagesItem> imgs = new ArrayList<ImagesItem>();
		ImagesItem imgItem = new ImagesItem();
		imgs.add(imgItem);
		imgItem.name = "";		
		imgItem.imageNum = mPostDetail.imgList.size();
		imgItem.images = new String[imgItem.imageNum];
		int i=0;
		for(String imgUrl : mPostDetail.imgList) {
			imgItem.images[i] = imgUrl;
			i++;
		}
		Intent intent = new Intent(mActivity, ImageBrowseActivity.class);
		intent.putParcelableArrayListExtra(IntentExtraConfig.IMAGE_BROWSER_DATA, imgs);
		intent.putExtra(IntentExtraConfig.IMAGE_BROWSER_DIS_TAB, false);
		intent.putExtra(IntentExtraConfig.IMAGE_BROWSER_POS, position);
		startActivity(intent);
	}
}
