package com.wb.sc.parser;


import com.wb.sc.bean.LawRuleDetial;
import com.wb.sc.util.ParamsUtil;

public class LawRuleDetialParser {

	public void parse(LawRuleDetial dataBean) {	
			
		//进行数据解析处理
		dataBean.lawId = ParamsUtil.getRespParamNext(dataBean, 64);
		dataBean.lawTitle = ParamsUtil.getRespParamNext(dataBean, 14);
		dataBean.lawContent = ParamsUtil.getRespParamNext(dataBean, 512);
		dataBean.releaseTime = ParamsUtil.getRespParamNext(dataBean, 16);
	}
}