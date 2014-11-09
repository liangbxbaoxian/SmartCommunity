package com.wb.sc.parser;


import java.util.ArrayList;

import com.wb.sc.bean.Community;
import com.wb.sc.bean.Community.CommunityItem;
import com.wb.sc.util.ParamsUtil;

public class CommunityParser {

	public void parse(Community dataBean) {	

		//进行数据解析处理		
		dataBean.totalNum = ParamsUtil.getRespIntParamNext(dataBean, 4);
		dataBean.hasNextPage = ParamsUtil.getNextPageFlag(dataBean);
		int dataLength = dataBean.dataBytes.length - 9 - 1;  //接口未定义有下一页
		String datasStr = ParamsUtil.getRespParam(dataBean, 9, dataLength);
		String[] itemsStr = datasStr.split(ParamsUtil.ITEMS_DIVIDER);
		dataBean.datas = new ArrayList<CommunityItem>();
		if (dataBean.totalNum > 0) {
			for(String itemStr : itemsStr) {
				String[] values = itemStr.split(ParamsUtil.ITEM_DIVIDER);
				CommunityItem item =  dataBean. new CommunityItem();
				item.communityId = values[0];
				item.communityCode = values[1];
				item.communityName = values[2];
				dataBean.datas.add(item);
			}
		}
	}
}