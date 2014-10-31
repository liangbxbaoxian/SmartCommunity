package com.wb.sc.parser;


import java.util.ArrayList;

import com.wb.sc.bean.MyExpress;
import com.wb.sc.bean.MyExpress.ExpressItem;
import com.wb.sc.util.ParamsUtil;

public class MyExpressParser {

	public void parse(MyExpress dataBean) {	
			
		//进行数据解析处理		
		dataBean.totalNum = ParamsUtil.getRespIntParamNext(dataBean, 4);
		dataBean.hasNextPage = ParamsUtil.getNextPageFlag(dataBean);
		int dataLength = dataBean.dataBytes.length - 9 - 1;  //接口未定义有下一页
		String datasStr = ParamsUtil.getRespParam(dataBean, 9, dataLength);
		String[] itemsStr = datasStr.split(ParamsUtil.ITEMS_DIVIDER);
		dataBean.datas = new ArrayList<MyExpress.ExpressItem>();
		for(String itemStr : itemsStr) {
			String[] values = itemStr.split(ParamsUtil.ITEM_DIVIDER);
			ExpressItem item =  dataBean. new ExpressItem();
			item.id = values[0];
			item.desc = values[1];
			item.cabinetId = values[2];
			item.cabinetNum = values[3];
			item.trackingNum = values[4];
			item.takeNum = values[5];
			item.expressCompany = values[6];
			item.courierName = values[7];
			item.courierTel = values[8];
			item.takeUserTel = values[9];
			dataBean.datas.add(item);
		}
	}
}