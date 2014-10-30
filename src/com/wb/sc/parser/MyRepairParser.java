package com.wb.sc.parser;


import java.util.ArrayList;

import com.wb.sc.bean.MyRepair;
import com.wb.sc.bean.MyRepair.MyRepairItem;
import com.wb.sc.util.ParamsUtil;

public class MyRepairParser {

	public void parse(MyRepair dataBean) {	
			
		//进行数据解析处理		
		dataBean.totalNum = Integer.valueOf(ParamsUtil.getRespParamNext(dataBean, 4));
		dataBean.hasNextPage = ParamsUtil.getNextPageFlag(dataBean);
		int dataLength = dataBean.dataBytes.length - 9 - 1;  //接口未定义有下一页
		String datasStr = ParamsUtil.getRespParam(dataBean, 9, dataLength);
		String[] itemsStr = datasStr.split(ParamsUtil.ITEMS_DIVIDER);
		dataBean.datas = new ArrayList<MyRepair.MyRepairItem>();
		for(String itemStr : itemsStr) {
			String[] values = itemStr.split(ParamsUtil.ITEM_DIVIDER);
			MyRepairItem item =  dataBean. new MyRepairItem();
			item.repairId = values[0];
			item.repairTitle = values[1];
			item.repairStatus = values[2];
			item.repairSubmitTime = values[3];
			item.repairEndTime = values[4];
			item.repairMaster = values[5];
			item.repairPhoto = values[6];
			dataBean.datas.add(item);
		}
	}
}