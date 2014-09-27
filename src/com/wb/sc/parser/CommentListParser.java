package com.wb.sc.parser;


import com.wb.sc.bean.CommentList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class CommentListParser {
	public CommentList parse(String resultStr) {		
		
		Gson gson = new Gson();
		CommentList data = gson.fromJson(resultStr, new TypeToken<CommentList>(){}.getType());
		
		return data;
	}
}