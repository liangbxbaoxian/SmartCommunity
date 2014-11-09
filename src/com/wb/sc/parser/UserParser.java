package com.wb.sc.parser;


import com.wb.sc.bean.User;
import com.wb.sc.util.ParamsUtil;

public class UserParser {

	public void parse(User dataBean) {	
			
		//进行数据解析处理
		dataBean.userId = ParamsUtil.getRespParamNext(dataBean, 64);
		dataBean.communityId = ParamsUtil.getRespParamNext(dataBean, 64);
		dataBean.communityName = ParamsUtil.getRespParamNext(dataBean, 128);
		dataBean.account = ParamsUtil.getRespParamNext(dataBean, 32);
		dataBean.name = ParamsUtil.getRespParamNext(dataBean, 32);
		dataBean.avatarUrl = ParamsUtil.getRespParamNext(dataBean, 256);
		dataBean.auth = ParamsUtil.getRespParamNext(dataBean, 2);
		dataBean.houseNum = ParamsUtil.getRespParamNext(dataBean, 32);
		dataBean.roomNum = ParamsUtil.getRespParamNext(dataBean, 32);
		dataBean.houseId = ParamsUtil.getRespParamNext(dataBean, 64);
	}
}