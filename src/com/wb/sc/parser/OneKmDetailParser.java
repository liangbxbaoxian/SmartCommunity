package com.wb.sc.parser;


import com.wb.sc.bean.OneKmDetail;
import com.wb.sc.bean.UpdateApp;
import com.wb.sc.util.ParamsUtil;

public class OneKmDetailParser {

	public void parse(OneKmDetail dataBean) {	
			
		//进行数据解析处理
		dataBean.merchantLogo = ParamsUtil.getRespParamNext(dataBean, 256);
		dataBean.merchantOpenTime = ParamsUtil.getRespParamNext(dataBean, 128);
		dataBean.merchantId = ParamsUtil.getRespParamNext(dataBean, 64);
		dataBean.merchantPriceChart = ParamsUtil.getRespParamNext(dataBean, 256);
		dataBean.merchantPriceAddr = ParamsUtil.getRespParamNext(dataBean, 256);
		dataBean.merchantPriceTel = ParamsUtil.getRespParamNext(dataBean, 15);
		dataBean.merchantPriceName = ParamsUtil.getRespParamNext(dataBean, 32);
		dataBean.merchantPriceCategoryId = ParamsUtil.getRespParamNext(dataBean, 32);
		dataBean.merchantPriceCategoryName = ParamsUtil.getRespParamNext(dataBean, 2);
		dataBean.longitude = ParamsUtil.getRespParamNext(dataBean, 128);
		dataBean.latitude = ParamsUtil.getRespParamNext(dataBean, 128);
		
	}
}