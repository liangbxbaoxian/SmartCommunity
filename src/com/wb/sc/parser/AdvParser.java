package com.wb.sc.parser;


import java.util.ArrayList;

import com.wb.sc.bean.Adv;
import com.wb.sc.bean.Adv.Item;
import com.wb.sc.util.ParamsUtil;

public class AdvParser {

	public void parse(Adv dataBean) {	
			
		//进行数据解析处理		
		dataBean.totalNum = Integer.valueOf(ParamsUtil.getRespParamNext(dataBean, 4));
		dataBean.hasNextPage = ParamsUtil.getNextPageFlag(dataBean);
		int dataLength = dataBean.dataBytes.length - 9 - 1;
		String datasStr = ParamsUtil.getRespParam(dataBean, 9, dataLength);
		String[] itemsStr = datasStr.split(ParamsUtil.ITEMS_DIVIDER);
		dataBean.datas = new ArrayList<Adv.Item>();
		for(String itemStr : itemsStr) {
			String[] values = itemStr.split(ParamsUtil.ITEM_DIVIDER);
			Item item = dataBean.new Item();
			item.id = values[0];
			item.imgUrl = values[1];
			item.title = values[2];
			item.content = values[3];
			item.order = values[4];
			item.linkUrl = values[5];
			item.type = values[6];
			dataBean.datas.add(item);
		}
	}
}