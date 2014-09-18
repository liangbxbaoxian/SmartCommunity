package com.wb.sc.parser;


import com.wb.sc.bean.PullRef;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class PullRefParser {
	public PullRef parse(String resultStr) {		
		
		Gson gson = new Gson();
		PullRef data = gson.fromJson(resultStr, new TypeToken<PullRef>(){}.getType());
		
		return data;
	}
}