package com.wb.sc.mk.main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.common.file.FileDirUtil;
import com.common.media.CameraHelper;
import com.common.widget.ToastHelper;
import com.common.widget.hzlib.HorizontalAdapterView;
import com.common.widget.hzlib.HorizontalAdapterView.OnItemClickListener;
import com.common.widget.hzlib.HorizontalListView;
import com.wb.sc.R;
import com.wb.sc.activity.base.BaseExtraLayoutFragment;
import com.wb.sc.adapter.PhotoAdapter;
import com.wb.sc.config.AcResultCode;

public class PostFragment extends BaseExtraLayoutFragment implements OnItemClickListener{
		
	private List<File> fileList = new ArrayList<File>();
	private File photoFile;
	private Uri photoUri;
	
	private CameraHelper cameraHelper;
	
	private HorizontalListView listView;	
	private GridView photosGv;
	private PhotoAdapter photoAdapter;
	
	private int state = 0; // 0:新增  1：替换
	private int selPos;
	
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
       cameraHelper = new CameraHelper(getActivity());
    }
   
    private void initView(View view) {
    	fileList.add(null);
    	listView = (HorizontalListView) view.findViewById(android.R.id.list);    	
    	photoAdapter = new PhotoAdapter(getActivity(), fileList);
    	listView.setAdapter(photoAdapter);    	
    }
    
    @Override
	public void onItemClick(HorizontalAdapterView<?> parent, View view,
			int position, long id) {
    	selPos = position;
    }
     
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode) {
		case AcResultCode.REQUEST_CODE_CAMERA_IMAGE:
			if(resultCode != 0 && photoFile.exists()) {			
			} else {
				ToastHelper.showToastInBottom(getActivity(), "拍照取图失败");
			}
			break;
			
		case AcResultCode.REQUEST_CODE_ALBUM_IMAGE:
			if(resultCode != 0 && data != null) {
				String photoPath = FileDirUtil.getPathFromUri(getActivity(), data.getData());
				if(!TextUtils.isEmpty(photoPath)) {
					photoFile = new File(photoPath);
					fileList.add(photoFile);
				} else {
					ToastHelper.showToastInBottom(getActivity(), "相册选图失败");
				}
			} else {
				ToastHelper.showToastInBottom(getActivity(), "相册选图失败");
			}
			break;				
		}
	}
    
    @Override
	public void onDestroy() {
		photoAdapter.recycleBmp();
		super.onDestroy();
	}
}
