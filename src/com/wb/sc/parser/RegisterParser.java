package com.wb.sc.parser;


import com.wb.sc.bean.Register;
import com.wb.sc.util.ParamsUtil;

public class RegisterParser {

	public void parse(Register dataBean) {	
			
		//进行数据解析处理
		dataBean.userId = ParamsUtil.getRespParamNext(dataBean, 64);
	}
}