package com.wb.sc.config;

/**
 * 
 * @描述：用于配置Activity的ResultCode
 * @作者：liang bao xian
 * @时间：2014年8月6日 下午5:30:01
 */
public interface AcResultCode {
	
	public static final int REQUEST_CODE_REFRESH_MSG_FAV_NUM = 1;
	
	public static final int REQUEST_CODE_REFRESH_POST_LIST = 2;
	
	/***************************** 通用 *********************************************/
	
	/**
	 * 相册取图
	 */
	public static final int REQUEST_CODE_ALBUM_IMAGE = 800;
	
	/**
	 * 拍照取图
	 */
	public static final int REQUEST_CODE_CAMERA_IMAGE = 801;
	
	/**
	 * 剪裁图片
	 */
	public static final int REQUEST_CODE_IMAGE_CROP = 802;	
	
	/**
	 * 拍照并剪裁图片
	 */
	public static final int REQUEST_CODE_CAMERA_CROP_IMAGE = 803;
		
	/**
	 * 相册取图并剪裁图片
	 */
	public static final int REQUEST_CODE_ALBUM_CROP_IMAGE = 804;
}
