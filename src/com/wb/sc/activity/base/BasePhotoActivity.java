package com.wb.sc.activity.base;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;

import com.common.file.FileDirUtil;
import com.common.media.CameraHelper;
import com.common.widget.ToastHelper;
import com.common.widget.hzlib.HorizontalAdapterView;
import com.common.widget.hzlib.HorizontalAdapterView.OnItemClickListener;
import com.common.widget.hzlib.HorizontalListView;
import com.wb.sc.R;
import com.wb.sc.adapter.PhotoAdapter;
import com.wb.sc.config.AcResultCode;
import com.wb.sc.dialog.AddPhotoDialog;

public abstract class BasePhotoActivity extends BaseHeaderActivity implements OnItemClickListener{
	
	private static final int CROP_WIDTH = 400;
	private static final int CROP_HEIGHT = 400;
	
	public int itemWidth = 83;
	
	private List<File> fileList;
	private File photoFile;
	private Uri photoUri;
	
	private CameraHelper cameraHelper;
	
	private HorizontalListView listView;	
	private PhotoAdapter photoAdapter;
	
	private int state = 0; // 0:新增  1：替换
	private int selPos;
	private AddPhotoDialog optDialog;
	
	public void initPhoto() {
		cameraHelper = new CameraHelper(this);
		fileList = new ArrayList<File>();
    	fileList.add(null);
    	listView = (HorizontalListView) findViewById(android.R.id.list); 
    	listView.setOnItemClickListener(this);
    	photoAdapter = new PhotoAdapter(this, fileList, itemWidth, itemWidth);
    	listView.setAdapter(photoAdapter);		
	}
	
	@Override
	public void onItemClick(HorizontalAdapterView<?> parent, View view,
			int position, long id) {
    	selPos = position;
    	if(optDialog == null) {
			optDialog = new AddPhotoDialog(this, R.style.popupStyle);
			optDialog.setListener(this);
		}
		
		selPos = position;
		File file = fileList.get(position);
		if(file == null) {
			optDialog.hidDel();
			if(position == 0) {
				state = 0;
			} else {
				state = 1;
			}
		} else {
			state = 1;
			optDialog.showDel();
		}
		
		optDialog.show();
    }
	
	@Override
	public void onClick(View v) {
    	super.onClick(v);
    	
    	switch(v.getId()) {
    	case R.id.take_picture:
    		optDialog.dismiss();
			pickFromCamera();
    		break;
    		
    	case R.id.photo_album:
    		optDialog.dismiss();
    		pickFromAlbum();
    		break;
    		
    	case R.id.photo_del:
    		optDialog.dismiss();
    		break;
    	}
    }
    
    private void pickFromCamera() {
		pickFromCamera(false);
	}
	
	private void pickFromCameraWhithCrop() {
		pickFromCamera(true);
	}
	
	/**
	 * 
	 * @描述:拍照取图
	 */
	private void pickFromCamera(boolean crop) {
		photoFile = cameraHelper.getOutputMediaFile(this, CameraHelper.MEDIA_TYPE_IMAGE);
		if(photoFile != null) {
			photoUri = Uri.fromFile(photoFile);
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
			intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
			if(crop) {
				startActivityForResult(intent, AcResultCode.REQUEST_CODE_CAMERA_CROP_IMAGE);
			} else {
				startActivityForResult(intent, AcResultCode.REQUEST_CODE_CAMERA_IMAGE);
			}
		} else {
			ToastHelper.showToastInBottom(this, "无法创建图片文件，请检测存储器是否异常");
		}
	}	
	
	/**
	 * 
	 * @描述:相册取图
	 */
	private void pickFromAlbum() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		startActivityForResult(intent, AcResultCode.REQUEST_CODE_ALBUM_IMAGE);
	}
	
	/**
	 * 
	 * @描述:相册取图并剪裁  
	 */
	private void pickFromAlbumWithCrop() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		startActivityForResult(intent, AcResultCode.REQUEST_CODE_ALBUM_CROP_IMAGE);
	}
	
	private void startCrop(Uri photoUri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(photoUri, "image/*");
		intent.putExtra("crop", "true");//可裁剪
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", CROP_WIDTH);
		intent.putExtra("outputY", CROP_HEIGHT);
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
		intent.putExtra("return-data", false);//若为false则表示不返回数据
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); 
		startActivityForResult(intent, AcResultCode.REQUEST_CODE_IMAGE_CROP);
	}
    
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode) {
		case AcResultCode.REQUEST_CODE_CAMERA_IMAGE:
			if(resultCode != 0 && photoFile.exists()) {		
				fileList.add(photoFile);	
				photoAdapter.notifyDataSetChanged();
			} else {
				ToastHelper.showToastInBottom(this, "拍照取图失败");
			}
			break;
			
		case AcResultCode.REQUEST_CODE_ALBUM_IMAGE:
			if(resultCode != 0 && data != null) {
				String photoPath = FileDirUtil.getPathFromUri(this, data.getData());
				if(!TextUtils.isEmpty(photoPath)) {
					photoFile = new File(photoPath);
					fileList.add(photoFile);
					photoAdapter.notifyDataSetChanged();
				} else {
					ToastHelper.showToastInBottom(this, "相册选图失败");
				}
			} else {
				ToastHelper.showToastInBottom(this, "相册选图失败");
			}
			break;				
		}
	}
    
    @Override
	protected void onDestroy() {
		photoAdapter.recycleBmp();
		super.onDestroy();
	}
}
