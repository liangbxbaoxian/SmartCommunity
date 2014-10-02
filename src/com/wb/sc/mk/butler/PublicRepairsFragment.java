package com.wb.sc.mk.butler;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wb.sc.R;
import com.wb.sc.activity.base.BasePhotoFragment;

public class PublicRepairsFragment extends BasePhotoFragment {
	
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
       return setContentView(inflater, R.layout.fragment_public_repairs);
    }
 
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
       super.onViewCreated(view, savedInstanceState);
      
       initView(view);
    }
   
    private void initView(View view) {
    	initPhoto(view);
    }
    
    
}
