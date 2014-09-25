package com.wb.sc.dialog;

import com.wb.sc.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AddPhotoDialog extends Dialog implements OnClickListener {
	
	private Button takePictureBtn;
	private Button photoAlbumBtn;
	private Button delPhotoBtn;
	private Button cancleBtn;
	private Context mContext;
	private int delState;
	
	private android.view.View.OnClickListener listener;
	
	public AddPhotoDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		mContext = context;
		
	}

	public AddPhotoDialog(Context context, int theme) {
		super(context, theme);
		mContext = context;
	}

	public AddPhotoDialog(Context context) {
		super(context);
		mContext = context;
	}
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_add_photo);
		
		getWindow().setGravity(Gravity.BOTTOM);
		setCancelable(true);
		
		initView();
	}	
	
	private void initView() {
		takePictureBtn = (Button) findViewById(R.id.take_picture);
		photoAlbumBtn = (Button) findViewById(R.id.photo_album);
		delPhotoBtn = (Button) findViewById(R.id.photo_del);
		cancleBtn = (Button) findViewById(R.id.cancle);
		
		takePictureBtn.setOnClickListener(this);
		photoAlbumBtn.setOnClickListener(this);
		delPhotoBtn.setOnClickListener(this);
		cancleBtn.setOnClickListener(this);
	}
	
	public void setListener(android.view.View.OnClickListener listener) {
		this.listener = listener;
	}

	@Override
	public void onClick(View v) {
		if (listener != null)
			listener.onClick(v);	
		
		if(v.getId() == R.id.cancle) {
			dismiss();
		}
	}
	
	public void hidDel() {
		delState = View.GONE;
	}
	
	public void showDel() {
		delState = View.VISIBLE;
	}

	@Override
	public void show() {
		super.show();
		delPhotoBtn.setVisibility(delState);
		
		WindowManager windowManager = ((Activity)mContext).getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.width = (int)(display.getWidth()); //设置宽度
		getWindow().setAttributes(lp);
	}
		
}
