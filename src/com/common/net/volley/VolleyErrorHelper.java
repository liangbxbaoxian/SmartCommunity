package com.common.net.volley;

import java.io.EOFException;
import java.net.UnknownHostException;

import android.content.Context;
import android.content.res.Resources;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.wb.sc.R;

public class VolleyErrorHelper {
	
	/**
	 * 获取错误信息
	 * 	AuthFailureError：如果在做一个HTTP的身份验证，可能会发生这个错误。
	 *	NetworkError：Socket关闭，服务器宕机，DNS错误都会产生这个错误。
	 *	NoConnectionError：和NetworkError类似，这个是客户端没有网络连接。
	 *  ParseError：在使用JsonObjectRequest或JsonArrayRequest时，如果接收到的JSON是畸形，会产生异常。
	 *  SERVERERROR：服务器的响应的一个错误，最有可能的4xx或5xx HTTP状态代码。
	 *	TimeoutError：Socket超时，服务器太忙或网络延迟会产生这个异常。默认情况下，Volley的超时时间为2.5秒。如果得到这个错误可以使用RetryPolicy。
	 * @param error
	 * @return
	 */	
	public static String getErrorMessage(Context context, VolleyError error) {
		String errorMsg = "";
		Resources resources = context.getResources();
		if (error instanceof TimeoutError) {
			errorMsg = resources.getString(R.string.net_error_timeout);
		} else if(error instanceof NoConnectionError) {
			if(error.getCause() instanceof UnknownHostException ||
					error.getCause() instanceof EOFException ) {
				errorMsg = resources.getString(R.string.net_error_connect_network);
			} else {
				if(error.getCause().toString().contains("Network is unreachable")) {
					errorMsg = resources.getString(R.string.net_error_no_network);
				} else {
					errorMsg = resources.getString(R.string.net_error_connect_network);					
				}
			}
		} else if(error instanceof NetworkError) {
			errorMsg = resources.getString(R.string.net_error_connect_network);
		} else if(error instanceof AuthFailureError) {
			errorMsg = resources.getString(R.string.net_error_auth_failure);
		} else if(error instanceof ServerError) {
			errorMsg = resources.getString(R.string.net_error_server, error.networkResponse.statusCode);
		} else if(error instanceof ParseError){
			errorMsg = resources.getString(R.string.net_error_parser);
		} else if(error.getCause() instanceof NullPointerException){
			errorMsg = resources.getString(R.string.net_error_null_pointer);
		} else {
			errorMsg = resources.getString(R.string.net_error_unkown);
		}
		
		return errorMsg;
	}
}
