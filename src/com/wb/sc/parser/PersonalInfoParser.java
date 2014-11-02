package com.wb.sc.parser;


import com.wb.sc.bean.PersonalInfo;
import com.wb.sc.util.ParamsUtil;

public class PersonalInfoParser {

	public void parse(PersonalInfo dataBean) {	
			
		//进行数据解析处理
		dataBean.accountName = ParamsUtil.getRespParamNext(dataBean, 32);
		dataBean.realName = ParamsUtil.getRespParamNext(dataBean, 32);
		dataBean.phoneNum = ParamsUtil.getRespParamNext(dataBean, 15);
		dataBean.birthday = ParamsUtil.getRespParamNext(dataBean, 8);
		dataBean.sex = ParamsUtil.getRespParamNext(dataBean, 2);
		dataBean.weixinAccount = ParamsUtil.getRespParamNext(dataBean, 32);
		dataBean.mail = ParamsUtil.getRespParamNext(dataBean, 64);
		dataBean.hobby = ParamsUtil.getRespParamNext(dataBean, 64);
		dataBean.userStatue = ParamsUtil.getRespParamNext(dataBean, 1);
		dataBean.protrait = ParamsUtil.getRespParamNext(dataBean, 256);
		dataBean.id = ParamsUtil.getRespParamNext(dataBean, 32);
		dataBean.registerTime = ParamsUtil.getRespParamNext(dataBean, 14);
		dataBean.notDisturbStatu = ParamsUtil.getRespParamNext(dataBean, 1);
		dataBean.disturbBeginTime = ParamsUtil.getRespParamNext(dataBean, 14);
		dataBean.disturbEndTime = ParamsUtil.getRespParamNext(dataBean, 14);
		dataBean.localCommunity = ParamsUtil.getRespParamNext(dataBean, 64);
		dataBean.communityNam = ParamsUtil.getRespParamNext(dataBean, 128);
		dataBean.imei = ParamsUtil.getRespParamNext(dataBean, 32);
	}
}