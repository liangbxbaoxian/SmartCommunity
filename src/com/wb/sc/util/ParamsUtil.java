package com.wb.sc.util;

import com.common.format.ByteHelper;
import com.wb.sc.bean.BaseBean;

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
	 * @描述: 获取响应字段中的参数，并自动下移指针位置
	 * @param baseBean
	 * @param length
	 * @return
	 */
	public static String getRespNextParam(BaseBean baseBean, int length) {
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
