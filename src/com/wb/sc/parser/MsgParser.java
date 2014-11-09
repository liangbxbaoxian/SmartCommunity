package com.wb.sc.parser;


import java.util.ArrayList;

import com.wb.sc.bean.Msg;
import com.wb.sc.util.ParamsUtil;

public class MsgParser {

	public void parse(Msg dataBean) {	

		//进行数据解析处理		
		dataBean.totalNum = ParamsUtil.getRespIntParamNext(dataBean, 4);
		dataBean.hasNextPage = ParamsUtil.getNextPageFlag(dataBean);
		int dataLength = dataBean.dataBytes.length - 9 - 1;  //接口未定义有下一页
		String datasStr = ParamsUtil.getRespParam(dataBean, 9, dataLength);
		String[] itemsStr = datasStr.split(ParamsUtil.ITEMS_DIVIDER);
		dataBean.datas = new ArrayList<Msg.MgItem>();
		if (dataBean.totalNum > 0) {
			for(String itemStr : itemsStr) {
				String[] values = itemStr.split(ParamsUtil.ITEM_DIVIDER);
				com.wb.sc.bean.Msg.MgItem item =  dataBean. new MgItem();
				item.msgNO = values[0];
				item.msgTitle = values[1];
				item.msgContent = values[2];
				item.communityId = values[3];
				item.msgTypeNO = values[4];
				item.msgType = values[5];
				item.msgCreteTime = values[6];
				dataBean.datas.add(item);
			}
		}
	}
}