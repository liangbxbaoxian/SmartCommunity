package com.wb.sc.parser;


import java.util.ArrayList;

import com.wb.sc.bean.PostType;
import com.wb.sc.bean.PostType.Item;
import com.wb.sc.util.ParamsUtil;

public class PostTypeParser {

	public void parse(PostType dataBean) {	
		
		//进行数据解析处理
		dataBean.totalNum = Integer.valueOf(ParamsUtil.getRespParamNext(dataBean, 4));
		dataBean.hasNextPage = ParamsUtil.getNextPageFlag(dataBean);
		int dataLength = dataBean.dataBytes.length - 9 - 1;
		String datasStr = ParamsUtil.getRespParam(dataBean, 9, dataLength);
		String[] itemsStr = datasStr.split(ParamsUtil.ITEMS_DIVIDER);
		dataBean.datas = new ArrayList<PostType.Item>();
		for(String itemStr : itemsStr) {
			String[] values = itemStr.split(ParamsUtil.ITEM_DIVIDER);
			Item item = dataBean.new Item();
			item.id = values[0];
			item.name = values[1];
			item.order = values[2];
			dataBean.datas.add(item);
		}
	}
}