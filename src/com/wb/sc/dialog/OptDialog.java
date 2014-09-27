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

public class OptDialog extends Dialog implements OnClickListener {
	
	private Button opt1Btn;
	private String opt1Name;
	private boolean opt1Visible;
	
	private Button opt2Btn;
	private String opt2Name;
	private boolean opt2Visible;
	
	private Button opt3Btn;
	private String opt3Name;
	private boolean opt3Visible;
	
	private Button cancleBtn;
	
	private Context mContext;
	private int delState;
	
	private android.view.View.OnClickListener listener;
	
	public OptDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		mContext = context;
		
	}

	public OptDialog(Context context, int theme) {
		super(context, theme);
		mContext = context;
	}

	public OptDialog(Context context) {
		super(context);
		mContext = context;
	}
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_operation);
		
		getWindow().setGravity(Gravity.BOTTOM);
		setCancelable(true);
		
		initView();
	}	
	
	private void initView() {
		opt1Btn = (Button) findViewById(R.id.opt_1);
		opt2Btn = (Button) findViewById(R.id.opt_2);
		opt3Btn = (Button) findViewById(R.id.opt_3);
		cancleBtn = (Button) findViewById(R.id.opt_cancle);
		
		opt1Btn.setOnClickListener(this);
		opt2Btn.setOnClickListener(this);
		opt3Btn.setOnClickListener(this);
		cancleBtn.setOnClickListener(this);		
		
		opt1Btn.setText(opt1Name);
		if(opt1Visible) {
			opt1Btn.setVisibility(View.VISIBLE);
		} else {
			opt1Btn.setVisibility(View.GONE);
		}
		
		opt2Btn.setText(opt2Name);
		if(opt2Visible) {
			opt2Btn.setVisibility(View.VISIBLE);
		} else {
			opt2Btn.setVisibility(View.GONE);
		}	
	}
	
	public void setOpt1Btn(String name, boolean visible) {
		opt1Name = name;
		opt1Visible = visible;
	}
	
	public void setOpt2Btn(String name, boolean visible) {
		opt2Name = name;
		opt2Visible = visible;
	}
	
	public void setOpt3Btn(String name, boolean visible) {
		opt3Name = name;
		opt3Visible = visible;
	}
	
	public void setListener(android.view.View.OnClickListener listener) {
		this.listener = listener;
	}

	@Override
	public void onClick(View v) {
		if (listener != null)
			listener.onClick(v);	
		
		if(v.getId() == R.id.opt_cancle) {
			dismiss();
		}
	}
	
	@Override
	public void show() {
		super.show();
		
		WindowManager windowManager = ((Activity)mContext).getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.width = (int)(display.getWidth()); //设置宽度
		getWindow().setAttributes(lp);
	}
		
}
