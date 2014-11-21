package com.wb.sc.mk.main;

import java.io.File;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.NetworkImageView.NetworkImageListener;
import com.common.media.BitmapHelper;
import com.wb.sc.R;
import com.wb.sc.activity.base.BaseItemPhotoFragment;
import com.wb.sc.activity.base.BaseItemPhotoFragment.OnUploadCompleteListener;
import com.wb.sc.app.SCApp;
import com.wb.sc.config.NetConfig;
import com.wb.sc.db.DbHelper;

public class PersonalFragment extends BaseItemPhotoFragment implements OnClickListener,
	OnUploadCompleteListener, NetworkImageListener{
	
	private NetworkImageView avatarIv;
	
	private TextView txt_auth;
	private TextView communityName;
	private TextView roomNum;
	
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
		
		initView(view);
		initPhoto(view, "FG05");
		setOnUploadCompleteListener(this);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if ("01".equals(SCApp.getInstance().getUser().auth)) {
			txt_auth.setText("已提交认证");
		} else if ("02".equals(SCApp.getInstance().getUser().auth)) {
			txt_auth.setText("住户已认证");
		} else if ("03".equals(SCApp.getInstance().getUser().auth)){
			txt_auth.setText("认证失败");
		} else {
			txt_auth.setText("住户未认证");
		}
		
		String community = SCApp.getInstance().getUser().communityName;
		if (!"".equals(community)) {
			communityName.setText(community);
		}
			
		if (!"".equals(SCApp.getInstance().getUser().roomNum)) {
			roomNum.setText(SCApp.getInstance().getUser().roomNum);
		}
	}
	
	private void initView(final View view) {
		TextView name = (TextView) view.findViewById(R.id.name);
		name.setText(SCApp.getInstance().getUser().account);
		
		txt_auth = (TextView) view.findViewById(R.id.txt_auth);
		if ("01".equals(SCApp.getInstance().getUser().auth)) {
			txt_auth.setText("已提交认证");
		} else if ("02".equals(SCApp.getInstance().getUser().auth)) {
			txt_auth.setText("住户已认证");
		} else if ("03".equals(SCApp.getInstance().getUser().auth)){
			txt_auth.setText("认证失败");
		} else {
			txt_auth.setText("住户未认证");
		}
		
		communityName = (TextView) view.findViewById(R.id.communityName);
		String community = SCApp.getInstance().getUser().communityName;
		if (!"".equals(community)) {
			communityName.setText(SCApp.getInstance().getUser().communityName);
		}
		
		
		roomNum = (TextView) view.findViewById(R.id.roomNum);
		if (!"".equals(SCApp.getInstance().getUser().roomNum)) {
			roomNum.setText(SCApp.getInstance().getUser().roomNum);
		}
				
		LinearLayout portrait = (LinearLayout) view.findViewById(R.id.portrait);
		portrait.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showOptDialog();
			}
		});

		avatarIv = (NetworkImageView) view.findViewById(R.id.img_portrait);
		avatarIv.setNetworkImageListener(this);
		if(SCApp.getInstance().getUser().isLogin == 1 &&
				!TextUtils.isEmpty(SCApp.getInstance().getUser().avatarUrl)) {
			avatarIv.setImageUrl(NetConfig.getPictureUrl(SCApp.getInstance().getUser().avatarUrl), 
					SCApp.getInstance().getImageLoader());
		}
//		final TextView txt_auth = (TextView) view.findViewById(R.id.txt_auth);
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
	
	@Override
	public void onComplete(String imgUrl, File imgFile) {
		Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
		Bitmap roundBmp = BitmapHelper.toRoundCorner(bitmap, bitmap.getHeight()/2);
		avatarIv.setImageBitmap(roundBmp);
		avatarIv.requestLayout();
		SCApp.getInstance().getUser().setAvatarUrl(imgUrl);
		DbHelper.saveUser(SCApp.getInstance().getUser());
	}
	
	@Override
	public void onGetBitmapListener(ImageView imageView, Bitmap bitmap) {
		Bitmap roundBmp = BitmapHelper.toRoundCorner(bitmap, bitmap.getHeight()/2);
		imageView.setImageBitmap(roundBmp);	
	}
}
