package com.wb.sc.mk.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Text;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request.DownloadProgressListener;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.NetworkImageView.NetworkImageListener;
import com.common.media.BitmapHelper;
import com.viewpagerindicator.CirclePageIndicator;
import com.wb.sc.R;
import com.wb.sc.activity.base.BaseExtraLayoutFragment;
import com.wb.sc.activity.base.BaseItemPhotoFragment;
import com.wb.sc.activity.base.BaseItemPhotoFragment.OnUploadCompleteListener;
import com.wb.sc.adapter.AdvAdapter;
import com.wb.sc.adapter.CategoryAdapter;
import com.wb.sc.app.SCApp;
import com.wb.sc.bean.BaseBean;
import com.wb.sc.bean.CategoryTable;
import com.wb.sc.config.NetConfig;
import com.wb.sc.db.DbHelper;
import com.wb.sc.mk.personal.MsgCenterActivity;
import com.wb.sc.task.BaseRequest;
import com.wb.sc.util.ParamsUtil;
import com.wb.sc.widget.CircleImageView;
import com.wb.sc.widget.SelectPicPopupWindow;

public class PersonalFragment extends BaseItemPhotoFragment implements OnClickListener,
	OnUploadCompleteListener, NetworkImageListener{

	private String path[] = new String[3];
	private int index = 0;
	
	private View msgV;

	// add test for linyongzhen
	private ViewPager advVp;
	private CirclePageIndicator advIndicator;
	private AdvAdapter advAdapter;

	private List<CategoryTable> categoryTableList = new ArrayList<CategoryTable>();
	private CategoryAdapter yipayGriAdapter;

	// 标题栏相关
	private ImageView leftIv;
	private ImageView rightIv;
	private TextView nameIv;

	private ImageView img_portrait;

	public static final int REQUEST_TAKE_CAMERA = 10;
	public static final int REQUEST_PICK_LOCAL = 20;

	// 自定义的弹出框类
	private SelectPicPopupWindow menuWindow;
	
	private BaseRequest mBaseRequest;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return setContentView(inflater, R.layout.fragment_personal);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		// initData();
		initHead(view, getString(R.string.bottom_bar_center));
		initView(view);
		initPhoto(view, "FG05");
		setOnUploadCompleteListener(this);
	}

	private void initData() {
		categoryTableList.clear();
		int resId[] = { R.drawable.driver_selector, R.drawable.coupon_selector,
				R.drawable.tuan_selector, R.drawable.trade_selector,
				R.drawable.together_selector, R.drawable.car_selector };

		String categoryname[] = { "送到家", "捡便宜", "天天团", "做买卖", "一起玩", "来拼车" };

		for (int i = 0; i < resId.length; i++) {
			CategoryTable category = new CategoryTable();
			category.setId(resId[i]);
			category.setCategoryname(categoryname[i]);
			categoryTableList.add(category);
		}
	}
	
	private void initHead(View view, String title) {
		initHeader(view, title);
		setHomeBackground(R.drawable.bottom_icon_wodexiaoxi);
		setHomeListenner(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(), MsgCenterActivity.class);
				startActivity(intent);
			}
		});
	}

	private void initView(final View view) {
//		msgV = view.findViewById(R.id.msg);
//		msgV.setOnClickListener(this);
		// advVp = (ViewPager) view.findViewById(R.id.adv_pager);
		// advIndicator = (CirclePageIndicator)
		// view.findViewById(R.id.adv_indicator);
		// advAdapter = new AdvAdapter(getActivity());
		// advVp.setAdapter(advAdapter);
		// advIndicator.setViewPager(advVp);
		// yipayGriAdapter = new CategoryAdapter(getActivity(),
		// categoryTableList);
		// final GridView yipay_server = (GridView)
		// view.findViewById(R.id.yipay_server);
		// yipay_server.setSelector(R.color.transparent);
		// yipay_server.setAdapter(yipayGriAdapter);
		TextView name = (TextView) view.findViewById(R.id.name);
		name.setText(SCApp.getInstance().getUser().account);
		
		LinearLayout portrait = (LinearLayout) view.findViewById(R.id.portrait);
		portrait.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// 实例化SelectPicPopupWindow
				menuWindow = new SelectPicPopupWindow(getActivity(),
						new listenner());
				// 显示窗口
				menuWindow.showAtLocation(view, Gravity.BOTTOM
						| Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
			}
		});

		img_portrait = (ImageView) view.findViewById(R.id.img_portrait);
		final TextView txt_auth = (TextView) view.findViewById(R.id.txt_auth);
		Button btn_exit = (Button) view.findViewById(R.id.btn_exit);
		btn_exit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				int state = txt_auth.getVisibility();
				if (state == View.VISIBLE) {
					state =View.GONE;
				} else {
					state = View.VISIBLE;
				}
				txt_auth.setVisibility(state);
				
				//登出处理
				SCApp.getInstance().getUser().isLogin = 0;
				DbHelper.saveUser(SCApp.getInstance().getUser());
				getActivity().finish();
			}
		});
	}

	public class listenner implements View.OnClickListener {

		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.btn_take_photo:
//				takePhoto();
				pickFromCameraWhithCrop();
				menuWindow.dismiss();				
				break;
			case R.id.btn_pick_photo:
//				pickLocalPic();
				pickFromAlbumWithCrop();
				menuWindow.dismiss();
				break;
			default:
				break;
			}
		}

	}
	
	/**
	 * 拍照获取
	 */
	public void takePhoto() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		getActivity().startActivityForResult(intent, REQUEST_TAKE_CAMERA);
	}

	/**
	 * 本地图片
	 */
	public void pickLocalPic() {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		getActivity().startActivityForResult(intent, REQUEST_PICK_LOCAL);
	}

	private Bitmap getBitmap(String imagePath) {
		BitmapFactory.Options bfOptions = new BitmapFactory.Options();
		bfOptions.inDither = false;
		bfOptions.inPurgeable = true;
		bfOptions.outHeight = 90;
		bfOptions.outWidth = 120;
		bfOptions.inTargetDensity = this.getResources().getDisplayMetrics().densityDpi;
		bfOptions.inScaled = true;
		bfOptions.inSampleSize = 4;
		bfOptions.inTempStorage = new byte[12 * 1024];
		// bfOptions.inJustDecodeBounds = true;
		File file = new File(imagePath);
		FileInputStream fs = null;
		try {
			fs = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Bitmap bitmap = null;
		if (fs != null)
			try {
				bitmap = BitmapFactory.decodeFileDescriptor(fs.getFD(), null,
						bfOptions);
			} catch (OutOfMemoryError oom) {
				oom.printStackTrace();
				if (bitmap != null) {
					bitmap.recycle();
					bitmap = null;
				}
				System.gc();

				try {
					bitmap = BitmapFactory.decodeFileDescriptor(fs.getFD(),
							null, bfOptions);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (OutOfMemoryError oom2) {
					oom.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (fs != null) {
					try {
						fs.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		return bitmap;
	}

	public String getPath(Uri uri) {
		// just some safety built in
		if (uri == null) {
			// TODO perform some logging or show user feedback
			return null;
		}
		// try to retrieve the image from the media store first
		// this will only work for images selected from gallery
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = getActivity().managedQuery(uri, projection, null, null,
				null);
		if (cursor != null) {
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		}
		// this is our fallback here
		return uri.getPath();
	}

	@SuppressLint("NewApi") public void setPhotoBitmap(Bitmap bitmap) {
		img_portrait.setImageBitmap(bitmap);
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch(v.getId()) {
		case R.id.msg:
			Intent intent = new Intent(getActivity(), MsgCenterActivity.class);
			startActivity(intent);
			break;
		}
	}


	@Override
	public void onComplete(String imgUrl, File imgFile) {
//		img_portrait.setImageUrl(NetConfig.getPictureUrl(imgUrl), SCApp.getInstance().getImageLoader());
		Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
		Bitmap roundBmp = BitmapHelper.toRoundCorner(bitmap, bitmap.getHeight()/2);
		img_portrait.setImageBitmap(roundBmp);
	}

	@Override
	public void onGetBitmapListener(ImageView imageView, Bitmap bitmap) {
		Bitmap roundBmp = BitmapHelper.toRoundCorner(bitmap, bitmap.getHeight()/2);
		img_portrait.setImageBitmap(roundBmp);
		
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
			Listener<BaseBean> listenre, ErrorListener errorListener) {			
		if(mBaseRequest != null) {
			mBaseRequest.cancel();
		}	
		String url = NetConfig.getServerBaseUrl() + NetConfig.EXTEND_URL;
		mBaseRequest = new BaseRequest(url, paramsList, listenre, errorListener);
//		startRequest(mBaseRequest);		
	}
	
	/**
	 * 获取请求参数,请按照接口文档列表顺序排列
	 * @return
	 */
	private List<String> getBaseRequestParams() {
		List<String> params = new ArrayList<String>();
		params.add(ParamsUtil.getReqParam("FG01", 4));
		params.add(ParamsUtil.getReqParam("MC_CENTERM", 16));
		params.add(ParamsUtil.getReqParam("00001", 20));
		params.add(ParamsUtil.getReqParam("00", 2));
		params.add(ParamsUtil.getReqParam("13675013092", 15));
		params.add(ParamsUtil.getReqParam("12345678900987654321123456789009", 32));
		
		return params;
	}
	
	
}
