package com.wb.sc.parser;

import android.util.Log;

import com.common.format.ByteHelper;
import com.common.format.HexStringBytes;
import com.common.security.Base64Tools;
import com.common.security.Des3Tools;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wb.sc.app.SCApp;
import com.wb.sc.bean.BaseBean;
import com.wb.sc.config.DebugConfig;
import com.wb.sc.util.ParamsUtil;

public class BaseParser {
	public static final String TAG = "volley_response";
	
	public static BaseBean parse(BaseBean baseBean, String resultStr) {		
		DebugConfig.showLog(TAG, resultStr);
		
		Gson gson = new Gson();
		BaseBean data = gson.fromJson(resultStr, new TypeToken<BaseBean>(){}.getType());
		baseBean.respCode = data.respCode;
		baseBean.respCodeMsg = data.respCodeMsg;
		baseBean.data = data.data;
		
		//进行base 64解码
		byte[] des3Content = Base64Tools.decode(baseBean.data);
//		if(DebugConfig.SHOW_DEBUG_MESSAGE) {
//			Log.d(TAG, HexStringBytes.bytes2HexString(des3Content));
//		}
		
		//进行3des解密
		byte[] content = Des3Tools.deTripleDES(SCApp.getInstance().getDes3Key(), des3Content);
		if(DebugConfig.SHOW_DEBUG_MESSAGE) {
			Log.d(TAG, HexStringBytes.bytes2HexString(content));
		}
		
		//截取字符长度,获取data内容,暂时忽略最后2字节的CRC16检验
		int length = (int)(((content[0]&0xff) << 8) | content[1] & 0xff) - 2;		
		content = ByteHelper.byteArraySub(content, 2, length);
		if(DebugConfig.SHOW_DEBUG_MESSAGE) {
//			Log.d(TAG, HexStringBytes.bytes2HexString(content));
			Log.d(TAG, new String(content));
		}
				
		baseBean.dataBytes = content;			
		baseBean.position = 5;
		
		baseBean.respCode = ParamsUtil.getRespParam(baseBean, 0, 5);
		return baseBean;
	}
}