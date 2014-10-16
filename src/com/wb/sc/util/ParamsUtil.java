package com.wb.sc.util;

import com.common.format.ByteHelper;
import com.common.format.HexStringBytes;
import com.wb.sc.app.SCApp;
import com.wb.sc.bean.BaseBean;
import com.wb.sc.security.RSA;

public class ParamsUtil {
	
	/**
	 * 
	 * @描述: 封装请求参数
	 * @param value 字段的值
	 * @param length 字段在接口文档中定义的长度
	 * @return
	 */
	public static String getReqParam(String value, int length) {
		return value + "&" + length;
	}
	
	/**
	 * 
	 * @描述: 封装请求参数, 并进行RSA加密
	 * @param value
	 * @param length
	 * @return
	 */
	public static String getReqRsaParam(String value, int length) {
		RSA rsa = new RSA();
		try {
			byte[] psd = rsa.encryptByPublicKey(SCApp.getInstance().getPublicKey(), 
					value.getBytes());
			return getReqHexParam(HexStringBytes.bytes2HexString(psd), length);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		return "";
	}
	
	
	public static String getReqHexParam(String value, int length) {
		return value + "&" + length + "&" + "Hex";
	}
	/**
	 * 
	 * @描述: 获取响应字段中的参数，并自动下移指针位置
	 * @param baseBean
	 * @param length
	 * @return
	 */
	public static String getRespParamNext(BaseBean baseBean, int length) {
		byte[] paramBytes = ByteHelper.byteArraySub(baseBean.dataBytes, baseBean.position, length);
		baseBean.position += length;
		return new String(paramBytes).trim();
	}
	
	/**
	 * 
	 * @描述: 获取响应字段中的指定参数
	 * @param baseBean
	 * @param start
	 * @param length
	 * @return
	 */
	public static String getRespParam(BaseBean baseBean, int start, int length) {
		byte[] paramBytes = ByteHelper.byteArraySub(baseBean.dataBytes, start, length);
		return new String(paramBytes).trim();
	}
}
