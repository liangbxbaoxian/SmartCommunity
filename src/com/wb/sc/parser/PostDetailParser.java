package com.wb.sc.parser;


import com.wb.sc.bean.PostDetail;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class PostDetailParser {
	public PostDetail parse(String resultStr) {		
		
		Gson gson = new Gson();
		PostDetail data = gson.fromJson(resultStr, new TypeToken<PostDetail>(){}.getType());
		
		return data;
	}
}