package com.wb.sc.config;

import android.util.Log;

/**
 * 此类用于配置调试信息
 * @author liangbx
 *
 */
public class DebugConfig {
	
	/**
	 * 版本配置
	 * 测试版本：0
	 * 正式版本：1
	 */
	public static final int VERSION_CONFIG = 0;
	
	/**
	 * 网络配置
	 * 内网：0
	 * 外网：1
	 */
	public static final int NET_CONFIG = 0;
	
	/**
	 * 显示调试信息开关
	 */
	public static final boolean SHOW_DEBUG_MESSAGE = true; 
	
	/**
	 * 显示日志
	 * @param tag
	 * @param message
	 */
	public static void showLog(String tag, String message) {
		if(SHOW_DEBUG_MESSAGE) {
			Log.d(tag, message);
		}
	}
}
