package com.wb.sc.mk.main;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.common.net.volley.VolleyErrorHelper;
import com.common.widget.ToastHelper;
import com.common.widget.hzlib.HorizontalAdapterView.OnItemClickListener;
import com.wb.sc.R;
import com.wb.sc.activity.base.BaseActivity;
import com.wb.sc.activity.base.BaseNetActivity;
import com.wb.sc.activity.base.BasePhotoActivity.PhotoUploadListener;
import com.wb.sc.activity.base.BasePhotoFragment;
import com.wb.sc.app.SCApp;
import com.wb.sc.bean.Post;
import com.wb.sc.bean.PostType;
import com.wb.sc.config.AcResultCode;
import com.wb.sc.config.NetConfig;
import com.wb.sc.config.RespCode;
import com.wb.sc.dialog.ToastLoginDialog;
import com.wb.sc.task.PostRequest;
import com.wb.sc.task.PostTypeRequest;
import com.wb.sc.util.ParamsUtil;

public class PostFragment extends BasePhotoFragment implements OnItemClickListener,
	PhotoUploadListener, ErrorListener{
	
	private BaseActivity mActivity;
	
	private View postBtn;
	private Spinner typeSp;
	private String selTypeId;
	
	private TextView titleTv;
	private TextView contentTv;
	private String title;
	private String content;
	
	//发帖类型
	private PostTypeRequest mPostTypeRequest;
	private PostTypeListener mPostTypeListener = new PostTypeListener();
	private PostType mPostType;
	
	//发帖
	private PostRequest mPostRequest;
	private PostListener mPostListener = new PostListener();
	private Post mPost;
	
	@Override
    public void onAttach(Activity activity) {
       super.onAttach(activity);
       mActivity = (BaseActivity) activity;
    }
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
    }
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
           Bundle savedInstanceState) {
       return setContentView(inflater, R.layout.fragment_post);
    }
 
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
       super.onViewCreated(view, savedInstanceState);
      
       initView(view);
       
       requestPostType(getPostTypeRequestParams(), mPostTypeListener, this);
    }
   
    private void initView(View view) {
    	titleTv = (TextView) view.findViewById(R.id.title);
    	contentTv = (TextView) view.findViewById(R.id.content);
    	
    	initPhoto(view, "FG30");      	
    	setUploadListener(this);
    	
    	postBtn = view.findViewById(R.id.submit);
    	postBtn.setOnClickListener(this);
    	
    	typeSp = (Spinner) view.findViewById(R.id.type);
//    	String[] types = getResources().getStringArray(R.array.post_type);
//    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), 
//    			R.layout.spinner_text_layout, types);
//    	adapter.setDropDownViewResource(R.layout.spinner_down_text_layout);
//    	typeSp.setAdapter(adapter);
    }
    
    @Override
	public void onClick(View v) {
		super.onClick(v);
		switch(v.getId()) {			
		case R.id.submit:
			if(ToastLoginDialog.checkLogin(getActivity())) {
				submit();
			}
			break;
		}
    }
    
    private void submit() {
    	title = titleTv.getText().toString();
    	content = contentTv.getText().toString();
    	
    	if(TextUtils.isEmpty(title)) {
    		ToastHelper.showToastInBottom(getActivity(), "标题不能为空");
    		return;
    	}
    	
    	if(title.length() > 33) {
    		ToastHelper.showToastInBottom(getActivity(), "标题过长，请删减");
    		return;
    	}
    	
    	if(TextUtils.isEmpty(content)) {
    		ToastHelper.showToastInBottom(getActivity(), "内容不能为空");
    		return;
    	}
    	
    	if(content.length() > 341) {
    		ToastHelper.showToastInBottom(getActivity(), "内容过长，请删减内容");
    		return;
    	}
    	
    	//获取发贴类型
    	if(mPostType == null) {
    		ToastHelper.showToastInBottom(getActivity(), "帖子类型不能为空");
    		return;	
    	}
    	
    	selTypeId = mPostType.datas.get(typeSp.getSelectedItemPosition()).id;
    	
    	mActivity.showProcess(R.string.submit_toast);
    	startUploadPhoto();
    	
    }
    
    /**
	 * 获取帖子分类请求参数
	 * @return
	 */
	private List<String> getPostTypeRequestParams() {
		List<String> params = new ArrayList<String>();
		params.add(ParamsUtil.getReqParam("FG33", 4));
		params.add(ParamsUtil.getReqParam("MC_CENTERM", 16));
		params.add(ParamsUtil.getReqParam("00001", 20));
		params.add(ParamsUtil.getReqParam(SCApp.getInstance().getUser().userId, 64));
		params.add(ParamsUtil.getReqIntParam(1, 3));
		params.add(ParamsUtil.getReqIntParam(10, 2));
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
	private void requestPostType(List<String> params,	 
			Listener<PostType> listenre, ErrorListener errorListener) {			
		if(mPostTypeRequest != null) {
			mPostTypeRequest.cancel();
		}	
		String url = NetConfig.getServerBaseUrl() + NetConfig.EXTEND_URL;
		mPostTypeRequest = new PostTypeRequest(url, params, listenre, errorListener);
		((BaseNetActivity)getActivity()).startRequest(mPostTypeRequest);		
	}
    
    /**
	 * 
	 * @描述：帖子分类监听
	 * @作者：liang bao xian
	 * @时间：2014年10月27日 上午8:51:09
	 */
	class PostTypeListener implements Listener<PostType>{
		/**
		 * 请求完成，处理UI更新
		 */
		@Override
		public void onResponse(PostType response) {		
			showContent();	
			if(response.respCode.equals(RespCode.SUCCESS)) {			
				mPostType = response;
				String[] types = new String[mPostType.datas.size()];
				for(int i=0; i<mPostType.datas.size(); i++) {
					types[i] = mPostType.datas.get(i).name;								
				}
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), 
		    			R.layout.spinner_text_layout, types);
		    	adapter.setDropDownViewResource(R.layout.spinner_down_text_layout);
		    	typeSp.setAdapter(adapter);

			} else {
				ToastHelper.showToastInBottom(getActivity(), response.respCodeMsg);
			}
		}
	}
	
	/**
	 * 获取请求参数
	 * @return
	 */
	private List<String> getPostRequestParams(String imgsUrl) {
		List<String> params = new ArrayList<String>();
		params.add(ParamsUtil.getReqParam("FG30", 4));
		params.add(ParamsUtil.getReqParam("MC_CENTERM", 16));
		params.add(ParamsUtil.getReqParam("00001", 20));
		params.add(ParamsUtil.getReqParam(SCApp.getInstance().getUser().userId, 64));
		params.add(ParamsUtil.getReqParam(selTypeId, 64));
		params.add(ParamsUtil.getReqParam(title, 100));
		params.add(ParamsUtil.getReqParam(content, 1024));
		params.add(ParamsUtil.getReqParam(imgsUrl, 1024));
		params.add(ParamsUtil.getReqParam(SCApp.getInstance().getUser().communityId, 64));
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
	private void requestPost(List<String> params,	 
			Listener<Post> listenre, ErrorListener errorListener) {			
		if(mPostRequest != null) {
			mPostRequest.cancel();
		}	
		String url = NetConfig.getServerBaseUrl() + NetConfig.EXTEND_URL;
		mPostRequest = new PostRequest(url, params, listenre, errorListener);
		((BaseNetActivity)getActivity()).startRequest(mPostRequest);		
	}
	
	/**
	 * 
	 * @描述：发帖监听
	 * @作者：liang bao xian
	 * @时间：2014年11月1日 下午10:14:47
	 */
	class PostListener implements Listener<Post> {

		/**
		 * 请求完成，处理UI更新
		 */
		@Override
		public void onResponse(Post response) {		
			mActivity.dismissProcess();
			showContent();	
			if(response.respCode.equals(RespCode.SUCCESS)) {			
				mPost = response;
				ToastHelper.showToastInBottom(getActivity(), "发布成功");
				getActivity().setResult(AcResultCode.REQUEST_CODE_REFRESH_POST_LIST);
				getActivity().finish();
			} else {
				ToastHelper.showToastInBottom(getActivity(), response.respCodeMsg);
			}
		}		
	}

	@Override
	public void onUploadComplete(List<String> imgUrlList) {
		String imgsUrl = "";
		for(int i=0; i<imgUrlList.size(); i++) {
			imgsUrl += imgUrlList.get(i);
			imgsUrl += "-|";
		}
		//发起发帖请求
		requestPost(getPostRequestParams(imgsUrl), mPostListener, this);
	}
	
	/**
	 * 网络请求错误处理
	 *
	 */
	@Override
	public void onErrorResponse(VolleyError error) {		
		ToastHelper.showToastInBottom(getActivity(), VolleyErrorHelper.getErrorMessage(getActivity(), error));
	}
}
