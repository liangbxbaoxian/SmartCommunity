package com.wb.sc.parser;


import java.util.ArrayList;

import com.wb.sc.bean.MyRepair;
import com.wb.sc.bean.MyRepair.MyRepairItem;
import com.wb.sc.util.ParamsUtil;

public class MyRepairParser {

	public void parse(MyRepair dataBean) {	

		//进行数据解析处理		
		dataBean.totalNum = ParamsUtil.getRespIntParamNext(dataBean, 4);
		dataBean.hasNextPage = ParamsUtil.getNextPageFlag(dataBean);
		int dataLength = dataBean.dataBytes.length - 9 - 1;  //接口未定义有下一页
		String datasStr = ParamsUtil.getRespParam(dataBean, 9, dataLength);
		String[] itemsStr = datasStr.split(ParamsUtil.ITEMS_DIVIDER);
		dataBean.datas = new ArrayList<MyRepair.MyRepairItem>();
		if (dataBean.totalNum > 0) {
			for(String itemStr : itemsStr) {
				String[] values = itemStr.split(ParamsUtil.ITEM_DIVIDER);
				MyRepairItem item =  dataBean. new MyRepairItem();
				item.repairId = values[0];
				item.repairTitle = values[1];
				item.repairStatus = values[2];
				item.repairStatusName = values[3];
				item.repairSubmitTime = values[4];
				item.repairEndTime = values[5];
				item.repairMaster = values[6];
				if (7 < values.length) {
					item.repairPhoto = values[7].split("#");
				}
				dataBean.datas.add(item);
			}
		}
	}
}