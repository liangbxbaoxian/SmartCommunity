package com.wb.sc.parser;

import java.util.ArrayList;

import com.wb.sc.bean.CommentList;
import com.wb.sc.bean.CommentList.Item;
import com.wb.sc.util.ParamsUtil;

public class CommentListParser {

	public void parse(CommentList dataBean) {

		// 进行数据解析处理
		dataBean.totalNum = ParamsUtil.getRespIntParamNext(dataBean, 4);
		dataBean.hasNextPage = ParamsUtil.getNextPageFlag(dataBean);
		dataBean.datas = new ArrayList<CommentList.Item>();
		if (dataBean.totalNum == 0)
			return;
		
		int dataLength = dataBean.dataBytes.length - 9 - 1;
		String datasStr = ParamsUtil.getRespParam(dataBean, 9, dataLength);
		String[] itemsStr = datasStr.split(ParamsUtil.ITEMS_DIVIDER);
		for(String itemStr : itemsStr) {
			String[] values = itemStr.split(ParamsUtil.ITEM_DIVIDER);
			Item item = dataBean.new Item();
			item.id = values[0];
			item.sourceId = values[1];
			item.sourceAvatar = values[2];
			item.sourceName = values[3];
			item.content = values[4];
			item.parentSourceId = values[5];
			item.parentSourceName = values[6];
			item.time = values[7];
			dataBean.datas.add(item);
		}
	}
}