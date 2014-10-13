package com.common.net.volley;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.common.format.ByteHelper;
import com.common.format.HexStringBytes;
import com.common.security.Base64Tools;
import com.common.security.CRC16;
import com.common.security.Des3Tools;
import com.google.gson.Gson;
import com.wb.sc.app.SCApp;
import com.wb.sc.bean.RequestData;
import com.wb.sc.config.DebugConfig;
import com.wb.sc.security.RSA;

public abstract class ParamsRequest<T> extends Request<T> {
	public static final String TAG = "volley_request";
	
	private String url;
	private List<String> params;
	private Listener<T> mListener;
	private TagListener<T> mTagListener;
	private TagErrorListener tagErrorListener;
	
	public ParamsRequest(String url, List<String> params, Listener<T> listenre, ErrorListener errorListener) {
		super(Method.POST, url, errorListener);
		mListener = listenre;
		this.params = params;
		this.url = url;
	}
	
	public ParamsRequest(String url, List<String> params, Listener<T> listenre, TagErrorListener errorListener) {
		super(Method.POST, url, null);
		mListener = listenre;
		tagErrorListener = errorListener;
		this.params = params;
		this.url = url;
	}
	
	public ParamsRequest(String url, List<String> params, TagListener<T> listenre, TagErrorListener errorListener) {
		super(Method.POST, url, null);
		mTagListener = listenre;
		tagErrorListener = errorListener;
		this.params = params;	
		this.url = url;
	}
	
//	@Override
//	abstract protected Response<T> parseNetworkResponse(NetworkResponse response);
	
//	@Override
//	protected Response<T> parseNetworkResponse(NetworkResponse response) {
//		
//	}
	
	@Override
	protected void deliverResponse(T response) {
		if(mTagListener == null) {
			mListener.onResponse(response);
		} else {
			mTagListener.onResponse(response, getTag());
		}
	}
	
	@Override
	public void deliverError(VolleyError error) {
		if(tagErrorListener == null) {
			super.deliverError(error);
		} else {
			tagErrorListener.onErrorResponse(error, getTag());
		}
	}
		
	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		Map<String, String> map = new HashMap<String, String>();
		RequestData requestData = getRequestData(url, params);
		//进行转换输出
		Gson gson = new Gson();
		map.put("request", gson.toJson(requestData));
		return map;
	}
	
	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Charset", "UTF-8"); 
		return headers;
	}
	
	/**
	 * @param params
	 * @return
	 */
	private static RequestData getRequestData(String url, List<String> params) {		
		
		DebugConfig.showLog(TAG, "start ====================== volley request =====================");	
		DebugConfig.showLog(TAG, url);
		byte[][] tempDatas = new byte[params.size()][];    	
    	for(int i=0; i<params.size(); i++) {
    		String[] values = params.get(i).split("&");    		
    		tempDatas[i] = ByteHelper.getParamsByte(values[0], Integer.parseInt(values[1]));
    		DebugConfig.showLog(TAG, "[" + values[0] + "," + values[1] +"]");
    	}    	    	
    	
    	byte[] byteDatas = ByteHelper.byteArrayAdd(tempDatas);
    	//CRC16
    	byte[] crc16 = CRC16.cal_crc16(byteDatas, byteDatas.length);
    	byteDatas = ByteHelper.byteArrayAdd(byteDatas, crc16);    	
    	
    	//data length
    	String lengthStr = Integer.toHexString(byteDatas.length);
    	for(int i=lengthStr.length(); i<8; i++) {
    		lengthStr = "0" + lengthStr;
    	}
    	byte[] lengthByte = HexStringBytes.String2Bytes(lengthStr);
    	
    	// all data
    	byteDatas = ByteHelper.byteArrayAdd(lengthByte, byteDatas);
    	if(DebugConfig.SHOW_DEBUG_MESSAGE) {
    		Log.d(TAG, "数据源");
    		Log.d(TAG, HexStringBytes.bytes2HexString(byteDatas));
    	}
    	    	
    	// 3DES 加密
    	byte[] des3Result = null;
		try {
			des3Result = Des3Tools.enTripleDES(SCApp.getInstance().getDes3Key(), 
					Des3Tools.extendMsgForDes(byteDatas));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
    	DebugConfig.showLog(TAG, "3DES 加密后===");
    	if(DebugConfig.SHOW_DEBUG_MESSAGE) {
    		Log.d(TAG, HexStringBytes.bytes2HexString(des3Result));
    	}
    	
    	// 3DES 解密
    	byte[] result = null;
    	try {
    		result = Des3Tools.deTripleDES(SCApp.getInstance().getDes3Key(), des3Result);    		
    	} catch (Exception e1) {
			e1.printStackTrace();
		}
    	DebugConfig.showLog(TAG, "3DES 解密后===");
    	if(DebugConfig.SHOW_DEBUG_MESSAGE) {
    		Log.d(TAG, HexStringBytes.bytes2HexString(result));
    	}
    	
    	// Base 64 编码
    	String data = Base64Tools.encode(des3Result);
//    	String data = Base64.encodeToString(des3Result, Base64.DEFAULT);
    	DebugConfig.showLog(TAG, "Base 64编码后===");
    	if(DebugConfig.SHOW_DEBUG_MESSAGE) {
    		Log.d(TAG, data);
    	}
    	
    	//密钥处理
    	String keyMsg = "4646733932303731335832423459656134346735313158ECB308";
    	DebugConfig.showLog(TAG, "3DES密钥拼接字符串后,未加密===");
    	if(DebugConfig.SHOW_DEBUG_MESSAGE) {
    		Log.d(TAG, keyMsg);
    	}
    	RSA rsa = new RSA();
    	byte[] sec = null;
    	try {
    		sec = rsa.encryptByPublicKey(SCApp.getInstance().getPublicKey(), HexStringBytes.String2Bytes(keyMsg));    		
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	DebugConfig.showLog(TAG, "3DES密钥经过RSA加密后===");
    	if(DebugConfig.SHOW_DEBUG_MESSAGE) {
    		Log.d(TAG, HexStringBytes.bytes2HexString(sec));
    	}
    	// Base 64 编码
    	String secData = Base64Tools.encode(sec);
    	DebugConfig.showLog(TAG, "3DES密钥经过Base 64编码后===");
    	if(DebugConfig.SHOW_DEBUG_MESSAGE) {
    		Log.d(TAG, secData);
    	}    	    	
    	
//    	// Base 64解码
//    	byte[] decodeData = Base64Tools.decode(secData);
//    	DebugConfig.showLog(TAG, "3DES密钥经过Base 64解码后===");
//    	if(DebugConfig.SHOW_DEBUG_MESSAGE) {
//    		Log.d(TAG, HexStringBytes.bytes2HexString(decodeData));
//    	}    
//    	// RSA 解密
//    	try {
//			decodeData = rsa.decryptByPrivateKey(SCApp.getInstance().getPrivateKey(), decodeData);
//			DebugConfig.showLog(TAG, "3DES密钥拼接字符串后,RSA解密后===");
//	    	if(DebugConfig.SHOW_DEBUG_MESSAGE) {
//	    		Log.d(TAG, HexStringBytes.bytes2HexString(decodeData));
//	    	}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}    	
  	
    	RequestData requestData = new RequestData();
    	requestData.version = "V1.0";
    	requestData.data = data;
    	requestData.secData = secData;    	
    	
    	return requestData;
	}
		
	/** Callback interface for delivering error responses. */
    public interface TagErrorListener {
        /**
         * Callback method that an error has been occurred with the
         * provided error code and optional user-readable message.
         */
        public void onErrorResponse(VolleyError error, Object tag);
    }
    
    /** Callback interface for delivering parsed responses. */
    public interface TagListener<T> {
        /** Called when a response is received. */
        public void onResponse(T response, Object tag);
    }
}
