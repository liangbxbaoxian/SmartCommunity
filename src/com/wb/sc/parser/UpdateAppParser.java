package com.wb.sc.parser;


import com.wb.sc.bean.UpdateApp;
import com.wb.sc.util.ParamsUtil;

public class UpdateAppParser {

	public void parse(UpdateApp dataBean) {	
			
		//进行数据解析处理
		dataBean.newVersionNum = ParamsUtil.getRespParamNext(dataBean, 64);
		dataBean.updateTime = ParamsUtil.getRespParamNext(dataBean, 14);
		dataBean.updateDesc = ParamsUtil.getRespParamNext(dataBean, 256);
		dataBean.updateAppUrl = ParamsUtil.getRespParamNext(dataBean, 256);
		dataBean.versionSerial = ParamsUtil.getRespParamNext(dataBean, 4);
		dataBean.isForce = ParamsUtil.getRespParamNext(dataBean, 1);
		dataBean.crc = ParamsUtil.getRespParamNext(dataBean, 2);
	}
}