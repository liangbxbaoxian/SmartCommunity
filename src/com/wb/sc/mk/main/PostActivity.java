package com.wb.sc.mk.main;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;

import com.wb.sc.R;
import com.wb.sc.activity.base.BaseHeaderActivity;
import com.wb.sc.mk.personal.MyPostActivity;

public class PostActivity extends BaseHeaderActivity {

	private View myPostBtn;
	
	private Fragment postFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post);
		initHeader(R.string.ac_post);
		initView();
	}


	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
	}

	public void getIntentData() {
		
	}



	public void initView() {
		postFragment = new PostFragment();
		getSupportFragmentManager().beginTransaction().add(R.id.fragment_containner, postFragment).commit();
		
		myPostBtn = findViewById(R.id.my_posts);
		myPostBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//跳转至我的帖子
				Intent intent = new Intent(PostActivity.this, MyPostActivity.class);
				startActivity(intent);
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {		
		if(postFragment != null) {
			postFragment.onActivityResult(requestCode, requestCode, data);
		}
	}
	
	/**
	 * 处理在拍照时屏幕翻转的问题
	 */
	public void onConfigurationChanged(Configuration newConfig) {  

        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {   
            Configuration o = newConfig;  
            o.orientation = Configuration.ORIENTATION_PORTRAIT;  
            newConfig.setTo(o);  
        }   
        super.onConfigurationChanged(newConfig);  
    }
}
