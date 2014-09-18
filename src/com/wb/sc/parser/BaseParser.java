package com.wb.sc.parser;


import com.wb.sc.bean.BaseBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class BaseParser {
	public BaseBean parse(String resultStr) {		
		
		Gson gson = new Gson();
		BaseBean data = gson.fromJson(resultStr, new TypeToken<BaseBean>(){}.getType());
		
		return data;
	}
}