package com.wb.sc.mk.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viewpagerindicator.CirclePageIndicator;
import com.wb.sc.R;
import com.wb.sc.activity.base.BaseExtraLayoutFragment;
import com.wb.sc.adapter.AdvAdapter;
import com.wb.sc.adapter.CategoryAdapter;
import com.wb.sc.bean.CategoryTable;
import com.wb.sc.mk.personal.PersonalInfoActivity;
import com.wb.sc.widget.CircleImageView;
import com.wb.sc.widget.SelectPicPopupWindow;

public class PersonalFragment extends BaseExtraLayoutFragment {

	private String path[] = new String[3];
	private int index = 0;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

//	    requestCode = requestCode>> 16;
//	    if (requestCode != 0) {
//	    	requestCode--;
//	    }
		switch (requestCode) {
		case REQUEST_TAKE_CAMERA:
			if (data != null) {
				Uri uri = data.getData();
				path[index] = getPath(uri);
				if (uri != null) {
					try {
						Bitmap bmp = getBitmap(path[index]);
						if (bmp != null)
							setPhotoBitmap(bmp);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					Bundle extras = data.getExtras();
					if (extras != null) {
						Bitmap bmp = extras.getParcelable("data");
						if (bmp != null)
							setPhotoBitmap(bmp);
					}
				}
			}
			break;

		case REQUEST_PICK_LOCAL:
				Uri uri = data.getData();
				path[index] = getPath(uri);
				try {
					Bitmap bmp = getBitmap(path[index]);
					setPhotoBitmap(bmp);
				} catch (Exception e) {
					e.printStackTrace();
				}
			break;
		}

	}

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

	private com.wb.sc.widget.CircleImageView img_portrait;

	public static final int REQUEST_TAKE_CAMERA = 10;
	public static final int REQUEST_PICK_LOCAL = 20;

	// 自定义的弹出框类
	private SelectPicPopupWindow menuWindow;

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
		initView(view);
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

	private void initView(final View view) {
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

		img_portrait = (CircleImageView) view.findViewById(R.id.img_portrait);
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
			}
		});
	}

	public class listenner implements View.OnClickListener {

		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.btn_take_photo:
				takePhoto();
				menuWindow.dismiss();
				break;
			case R.id.btn_pick_photo:
				pickLocalPic();
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
}
