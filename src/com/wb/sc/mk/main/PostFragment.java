package com.wb.sc.mk.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.common.widget.hzlib.HorizontalAdapterView.OnItemClickListener;
import com.wb.sc.R;
import com.wb.sc.activity.base.BasePhotoFragment;
import com.wb.sc.mk.personal.MyForumActivity;

public class PostFragment extends BasePhotoFragment implements OnItemClickListener{
	
	private View postBtn;
	private Spinner typeSp;
	
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
       return setContentView(inflater, R.layout.fragment_post);
    }
 
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
       super.onViewCreated(view, savedInstanceState);
      
       initView(view);
    }
   
    private void initView(View view) {
    	initPhoto(view);  
    	postBtn = view.findViewById(R.id.my_posts);
    	postBtn.setOnClickListener(this);
    	
    	typeSp = (Spinner) view.findViewById(R.id.type);
    	String[] types = getResources().getStringArray(R.array.post_type);
    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), 
    			R.layout.spinner_text_layout, types);
    	adapter.setDropDownViewResource(R.layout.spinner_down_text_layout);
    	typeSp.setAdapter(adapter);
    }
    
    @Override
	public void onClick(View v) {
		super.onClick(v);
		switch(v.getId()) {
		case R.id.my_posts:
			//跳转至我的帖子
			Intent intent = new Intent(getActivity(), MyForumActivity.class);
			startActivity(intent);
			break;
		}
    }
}
