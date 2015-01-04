package com.wb.sc.parser;


import java.util.ArrayList;

import com.wb.sc.bean.LawRule;
import com.wb.sc.bean.LawRule.LawRuleItem;
import com.wb.sc.util.ParamsUtil;

public class LawRuleParser {

	public void parse(LawRule dataBean) {	
			
		//进行数据解析处理		
		dataBean.totalNum = ParamsUtil.getRespIntParamNext(dataBean, 4);
		dataBean.hasNextPage = ParamsUtil.getNextPageFlag(dataBean);
		int dataLength = dataBean.dataBytes.length - 9 - 1;  //接口未定义有下一页
		String datasStr = ParamsUtil.getRespParam(dataBean, 9, dataLength);
		String[] itemsStr = datasStr.split(ParamsUtil.ITEMS_DIVIDER);
		dataBean.datas = new ArrayList<LawRule.LawRuleItem>();
		if (dataBean.totalNum > 0) {
			for(String itemStr : itemsStr) {
				String[] values = itemStr.split(ParamsUtil.ITEM_DIVIDER);
				LawRuleItem item =  dataBean. new LawRuleItem();
				item.lawRuleId = values[0];
				item.lawRuleTitle = values[1];
				item.lawRuleContent = values[2];
				item.lawRuleTime = values[3];
				dataBean.datas.add(item);
			}
		}
	}
}