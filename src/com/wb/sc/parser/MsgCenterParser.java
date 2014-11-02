package com.wb.sc.parser;


import java.util.ArrayList;

import com.wb.sc.bean.MsgCenter;
import com.wb.sc.bean.MsgCenter.MsgItem;
import com.wb.sc.util.ParamsUtil;

public class MsgCenterParser {

	public void parse(MsgCenter dataBean) {	
			
		//进行数据解析处理		
		dataBean.totalNum = ParamsUtil.getRespIntParamNext(dataBean, 4);
		dataBean.hasNextPage = ParamsUtil.getNextPageFlag(dataBean);
		int dataLength = dataBean.dataBytes.length - 9 - 1;  //接口未定义有下一页
		String datasStr = ParamsUtil.getRespParam(dataBean, 9, dataLength);
		String[] itemsStr = datasStr.split(ParamsUtil.ITEMS_DIVIDER);
		dataBean.datas = new ArrayList<MsgCenter.MsgItem>();
		for(String itemStr : itemsStr) {
			String[] values = itemStr.split(ParamsUtil.ITEM_DIVIDER);
			MsgItem item =  dataBean. new MsgItem();
			item.bulletinId = values[0];
			item.bulletinTitle = values[1];
			item.bulletinContent = values[2];
			item.notifier = values[3];
			item.notifyTime = values[4];
			dataBean.datas.add(item);
		}
	}
}