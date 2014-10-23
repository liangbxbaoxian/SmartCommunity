package com.wb.sc.activity.base;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Debug;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;

import com.common.file.FileDirUtil;
import com.common.media.BitmapHelper;
import com.common.media.CameraHelper;
import com.common.security.MD5Tools;
import com.common.widget.ToastHelper;
import com.common.widget.hzlib.HorizontalAdapterView;
import com.common.widget.hzlib.HorizontalAdapterView.OnItemClickListener;
import com.common.widget.hzlib.HorizontalListView;
import com.wb.sc.R;
import com.wb.sc.adapter.PhotoAdapter;
import com.wb.sc.app.SCApp;
import com.wb.sc.bean.PhotoUpload;
import com.wb.sc.config.AcResultCode;
import com.wb.sc.config.DebugConfig;
import com.wb.sc.config.ImageConfig;
import com.wb.sc.config.NetConfig;
import com.wb.sc.config.NetInterface;
import com.wb.sc.config.RespCode;
import com.wb.sc.dialog.AddPhotoDialog;
import com.wb.sc.dialog.ConfirmDialog;
import com.wb.sc.parser.PhotoUploadParser;

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
	private int currentUploadIndex;
	private PhotoUploadListener listener;
	
	//照片上传时的页面类型
	protected String messageType;
	protected List<String> imgUrlList = new ArrayList<String>();
	
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
    		fileList.remove(selPos);			
    		photoAdapter.getPhotoList().get(selPos).get().recycle();
    		photoAdapter.getPhotoList().get(selPos).clear();
    		photoAdapter.getPhotoList().remove(selPos);
			if(fileList.get(0) != null) {
				fileList.add(0, null);
				photoAdapter.getPhotoList().add(0, new SoftReference<Bitmap>(null));
			}
			photoAdapter.notifyDataSetChanged();
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
    
    /**
     * 
     * @描述:开始上传用户照片
     */
    public void startUploadPhot() {
    	imgUrlList.clear();
    	if(fileList.size() > 1) {
    		if(fileList.get(0) != null) {
    			currentUploadIndex = 0;    			
    		} else {
    			currentUploadIndex = 1;
    		}
    		uploadIndexPhoto(currentUploadIndex);
    	} else {
    		if(listener != null) {
    			listener.onUploadComplete(imgUrlList);
    		}
    	}
    }
    
    /**
     * 
     * @描述: 上传用户照片
     */
    public void uploadIndexPhoto(int index) {
    	//压缩照片，进行上传
    	File scalePhoto = BitmapHelper.getScaleBitmapFile(this, fileList.get(currentUploadIndex).getAbsolutePath(), ImageConfig.MAX_WIDTH);
    	uploadPhoto(scalePhoto);
    }
    
    /**
	 * 上传照片
	 * @param file
	 */
	private void uploadPhoto(File file) {		
//		String suffixName = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(".")+1);
		
		try {			
			String urlParams = "?userId=" + SCApp.getInstance().getUser().userId;
			urlParams += "&messageType=" + messageType;
			urlParams += "&checkcodeMD5=" + MD5Tools.getDigestFromFile(file);
			
			AjaxParams params = new AjaxParams();
			params.put("photo", file);				

			FinalHttp fh = new FinalHttp(); 
			fh.configTimeout(NetConfig.UPLOAD_IMG_TIMEOUT);
			String url = NetConfig.getServerBaseUrl() + NetInterface.METHOD_UPLOAD_PHOTO + urlParams;
			DebugConfig.showLog("volley_request", url);
			fh.post(url, params, new AjaxCallBack<String>(){

				@Override
				public void onSuccess(String result) {
					PhotoUpload pUpload = new PhotoUploadParser().parse(result);
					DebugConfig.showLog("volley_response", result);
					if(pUpload.respCode.equals(RespCode.SUCCESS)) {
						imgUrlList.add(pUpload.imgUrl);
						currentUploadIndex++;
						if(currentUploadIndex < fileList.size()) {
							uploadIndexPhoto(currentUploadIndex);
						} else {
							if(listener != null) {
								listener.onUploadComplete(imgUrlList);
							}
						}
					} else {
						ToastHelper.showToastInBottom(mActivity, pUpload.respCodeMsg);
					}
				}
				
				@Override
				public void onFailure(Throwable t, int errorNo,
						String strMsg) {
					dismissProcess();
					
					ConfirmDialog dialog = new ConfirmDialog();
					dialog.getDialog(BasePhotoActivity.this, "提示", "照片上传失败，是否重试?", 
							new DialogInterface.OnClickListener(){

								@Override
								public void onClick(DialogInterface dialog, int which) {											
									uploadIndexPhoto(currentUploadIndex);
									showProcess("照片上传中，请稍候...");
									dialog.dismiss();
								}
						
					}).show();
				}				
			});
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public interface PhotoUploadListener {
		
		public void onUploadComplete(List<String> imgUrlList);
	}
	
	public void setUploadListener(PhotoUploadListener listener) {
		this.listener = listener;
	}
}
