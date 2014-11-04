package com.wb.sc.activity.base;

import java.io.File;
import java.io.FileNotFoundException;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;

import com.common.file.FileDirUtil;
import com.common.media.BitmapHelper;
import com.common.media.CameraHelper;
import com.common.security.MD5Tools;
import com.common.widget.ToastHelper;
import com.wb.sc.app.SCApp;
import com.wb.sc.bean.PhotoUpload;
import com.wb.sc.config.AcResultCode;
import com.wb.sc.config.DebugConfig;
import com.wb.sc.config.NetConfig;
import com.wb.sc.config.NetInterface;
import com.wb.sc.config.RespCode;
import com.wb.sc.dialog.ConfirmDialog;
import com.wb.sc.parser.PhotoUploadParser;

public class BaseItemPhotoFragment extends BaseExtraLayoutFragment {
	
	private static final int CROP_WIDTH = 400;
	private static final int CROP_HEIGHT = 400;
	
	private File photoFile;
	private Uri photoUri;
	
	private CameraHelper cameraHelper;
		
	//照片上传时的页面类型
	protected String messageType;
	protected String imgUrl;
	
	private OnUploadCompleteListener listener;
	
	private void initPhoto(View view) {
		cameraHelper = new CameraHelper(getActivity());
	}
	
	public void initPhoto(View view, String messageType) {
		initPhoto(view);
		this.messageType = messageType;
	}
	    
    /**
     * 
     * @描述:从照片取图
     */
    public void pickFromCamera() {
		pickFromCamera(false);
	}
	
    /**
     * 
     * @描述:拍照取图并剪裁
     */
	public void pickFromCameraWhithCrop() {
		pickFromCamera(true);
	}
	
	/**
	 * 
	 * @描述:拍照取图
	 */
	private void pickFromCamera(boolean crop) {
		photoFile = cameraHelper.getOutputMediaFile(getActivity(), CameraHelper.MEDIA_TYPE_IMAGE);
		if(photoFile != null) {
			photoUri = Uri.fromFile(photoFile);
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
			intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
			if(crop) {
				getActivity().startActivityForResult(intent, AcResultCode.REQUEST_CODE_CAMERA_CROP_IMAGE);
			} else {
				getActivity().startActivityForResult(intent, AcResultCode.REQUEST_CODE_CAMERA_IMAGE);
			}
		} else {
			ToastHelper.showToastInBottom(getActivity(), "无法创建图片文件，请检测存储器是否异常");
		}
	}	
	
	/**
	 * 
	 * @描述:相册取图
	 */
	public void pickFromAlbum() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		getActivity().startActivityForResult(intent, AcResultCode.REQUEST_CODE_ALBUM_IMAGE);
	}
	
	/**
	 * 
	 * @描述:相册取图并剪裁  
	 */
	public void pickFromAlbumWithCrop() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		getActivity().startActivityForResult(intent, AcResultCode.REQUEST_CODE_ALBUM_CROP_IMAGE);
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
		getActivity().startActivityForResult(intent, AcResultCode.REQUEST_CODE_IMAGE_CROP);
	}
    
//	@TargetApi(Build.VERSION_CODES.KITKAT) 
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode) {
		case AcResultCode.REQUEST_CODE_CAMERA_IMAGE:
			if(resultCode != 0 && photoFile.exists()) {
				uploadPhoto(photoFile);
			} else {
				ToastHelper.showToastInBottom(getActivity(), "拍照取图失败");
			}
			break;
			
		case AcResultCode.REQUEST_CODE_ALBUM_IMAGE:
			if(resultCode != 0 && data != null) {
				String photoPath = FileDirUtil.getPathFromUri(getActivity(), data.getData());
				if(!TextUtils.isEmpty(photoPath)) {
					photoFile = new File(photoPath);
					uploadPhoto(photoFile);
				} else {
					ToastHelper.showToastInBottom(getActivity(), "相册选图失败");
				}
			} else {
				ToastHelper.showToastInBottom(getActivity(), "相册选图失败");
			}
			break;
				
		case AcResultCode.REQUEST_CODE_CAMERA_CROP_IMAGE:
			if(resultCode != 0 && photoFile.exists()) {
				photoUri = Uri.fromFile(photoFile);
				startCrop(photoUri);
			} else {
				ToastHelper.showToastInBottom(getActivity(), "拍照取图失败");
			}
			break;		
	    
		case AcResultCode.REQUEST_CODE_ALBUM_CROP_IMAGE:
			if(resultCode != 0 && data != null) {
				String photoPath = FileDirUtil.getPathFromUri(getActivity(), data.getData());
				if(!TextUtils.isEmpty(photoPath)) {
					photoFile = new File(photoPath);
					photoUri = Uri.fromFile(photoFile);
					startCrop(photoUri);
				} else {
					ToastHelper.showToastInBottom(getActivity(), "相册选图失败");
				}
			} else {
				ToastHelper.showToastInBottom(getActivity(), "相册选图失败");
			}
			break;
			
		case AcResultCode.REQUEST_CODE_IMAGE_CROP:
			if(resultCode != 0 && photoFile.exists()) {
				uploadPhoto(photoFile);
			} else {
				ToastHelper.showToastInBottom(getActivity(), "剪裁取消");
			}
			break;
		}
	}
    
    @Override
	public void onDestroy() {
		super.onDestroy();
	}
        
    /**
	 * 上传照片
	 * @param file
	 */
	private void uploadPhoto(File file) {		
		
		try {
			String urlParams = "?userId=" + SCApp.getInstance().getUser().userId;
			urlParams += "&messageType=" + messageType;
			urlParams += "&checkcodeMD5=" + MD5Tools.getDigestFromFile(file);
			
			AjaxParams params = new AjaxParams();
			params.put("photo", file);			
			FinalHttp fh = new FinalHttp(); 
			fh.configTimeout(NetConfig.UPLOAD_IMG_TIMEOUT);
			String url = NetConfig.getServerBaseUrl() + NetInterface.METHOD_UPLOAD_PHOTO + urlParams;
			fh.post(url, params, new AjaxCallBack<String>(){

				@Override
				public void onSuccess(String result) {
					DebugConfig.showLog("volley_response", result);
					PhotoUpload pUpload = new PhotoUploadParser().parse(result);
					if(pUpload.respCode.equals(RespCode.SUCCESS)) {						
						if(listener != null) {
							listener.onComplete(pUpload.data, photoFile);
						}
						ToastHelper.showToastInBottom(getActivity(), "头像上传成功");
					} 
				}
				
				@Override
				public void onFailure(Throwable t, int errorNo,
						String strMsg) {
					((BaseActivity)getActivity()).dismissProcess();
					
					ConfirmDialog dialog = new ConfirmDialog();
					dialog.getDialog(getActivity(), "提示", "照片上传失败，是否重试?", 
							new DialogInterface.OnClickListener(){

								@Override
								public void onClick(DialogInterface dialog, int which) {											
									uploadPhoto(photoFile);
									((BaseActivity)getActivity()).showProcess("照片上传中，请稍候...");
									dialog.dismiss();
								}
						
					}).show();
				}				
			});
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void setOnUploadCompleteListener (OnUploadCompleteListener listener) {
		this.listener = listener;
	}
	
	public interface OnUploadCompleteListener {
		public void onComplete(String imgUrl, File imgFile);
	}
}
