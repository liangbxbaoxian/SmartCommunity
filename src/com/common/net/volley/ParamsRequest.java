package com.common.net.volley;

import java.util.HashMap;
import java.util.Map;

import android.net.Uri;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.wb.sc.config.DebugConfig;

public abstract class ParamsRequest<T> extends Request<T> {
	public static final String TAG = "volley_request";
	
	private Map<String, String> params;
	private Listener<T> mListener;
	private TagListener<T> mTagListener;
	private TagErrorListener tagErrorListener;
	
	public ParamsRequest(int method, String url, Map<String, String> params, Listener<T> listenre, ErrorListener errorListener) {
		super(method, formatUrlParams(url, method, params), errorListener);
		mListener = listenre;
		this.params = params;	
	}
	
	public ParamsRequest(int method, String url, Map<String, String> params, Listener<T> listenre, TagErrorListener errorListener) {
		super(method, formatUrlParams(url, method, params), null);
		mListener = listenre;
		tagErrorListener = errorListener;
		this.params = params;			
	}
	
	public ParamsRequest(int method, String url, Map<String, String> params, TagListener<T> listenre, TagErrorListener errorListener) {
		super(method, formatUrlParams(url, method, params), null);
		mTagListener = listenre;
		tagErrorListener = errorListener;
		this.params = params;			
	}
	
	@Override
	abstract protected Response<T> parseNetworkResponse(NetworkResponse response);
	
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
		return params;
	}
		
	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Charset", "UTF-8"); 
		headers.put("accessToken", "A0BAA87FCF5D187EC9582866B9AE1A3B");
		return headers;
	}
	
	/**
	 * @param params
	 * @return
	 */
	private static String formatUrlParams(String url, int method, Map<String, String> params) {		
		String query="";
		if(method == Method.GET || method == Method.DELETE || method == Method.PUT) {
			String[] keys = params.keySet().toArray(new String[] {});
			for(int i = 0, size  = keys.length; i < size; i++) {
				String key = keys[i];
				String value = params.get(key).replaceAll("\n", "");
				query += (i == 0 ? "?" : "&");
				query += key;
				query += "=";
				query += Uri.encode(value);
			}
		}
		
		if(DebugConfig.SHOW_DEBUG_MESSAGE) {			
			DebugConfig.showLog(TAG, "start ====================== volley request =====================");
			if(Method.POST == method) {
				for(String key : params.keySet()){
					DebugConfig.showLog(TAG, key + "=" + params.get(key));
				}
			}				
		}
		
		url += query;
		DebugConfig.showLog(TAG, url);
		return url;
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
