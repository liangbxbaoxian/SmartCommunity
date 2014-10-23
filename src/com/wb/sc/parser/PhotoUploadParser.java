package com.wb.sc.parser;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wb.sc.bean.PhotoUpload;

public class PhotoUploadParser {

	public PhotoUpload parse(String resultStr) {	
			
		//进行数据解析处理
		Gson gson = new Gson();
		PhotoUpload photoUpload = gson.fromJson(resultStr, new TypeToken<PhotoUpload>(){}.getType());
		return photoUpload;
	}
}