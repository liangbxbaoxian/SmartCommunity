package com.wb.sc.mk.butler;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.wb.sc.R;
import com.wb.sc.activity.base.BasePhotoFragment;

public class PersonalRepairsFragment extends BasePhotoFragment{
	
	private Spinner timeSp;
	
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
       return setContentView(inflater, R.layout.fragment_personal_repairs);
    }
 
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
       super.onViewCreated(view, savedInstanceState);
      
       initView(view);
    }
   
    private void initView(View view) {    
    	initPhoto(view);
    	
    	timeSp = (Spinner) view.findViewById(R.id.time);
    	String[] types = getResources().getStringArray(R.array.property_repairs_time);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), 
    			R.layout.spinner_text_layout, types);
    	adapter.setDropDownViewResource(R.layout.spinner_down_text_layout);
    	timeSp.setAdapter(adapter);
    	
    }
}
