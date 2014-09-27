package com.wb.sc.parser;


import com.wb.sc.bean.PostList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class PostListParser {
	public PostList parse(String resultStr) {		
		
		Gson gson = new Gson();
		PostList data = gson.fromJson(resultStr, new TypeToken<PostList>(){}.getType());
		
		return data;
	}
}