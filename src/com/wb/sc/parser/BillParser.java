package com.wb.sc.parser;


import com.wb.sc.bean.Bill;
import com.wb.sc.util.ParamsUtil;

public class BillParser {

	public void parse(Bill dataBean) {	
			
		//进行数据解析处理
		dataBean.amount = ParamsUtil.getRespParamNext(dataBean, 16);
		dataBean.detail = ParamsUtil.getRespParamNext(dataBean, 256);
	}
}