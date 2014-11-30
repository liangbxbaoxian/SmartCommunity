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
				item.repairContent = values[4];
				item.repairSubmitTime = values[5];
				item.repairHanldeTime = values[6];
				item.repairEndTime = values[7];
				item.repairMaster = values[8];
				item.repairReuslt = values[9];
				if (11 < values.length) {    //接口返回有问题。应该去下标为10，结果返回11个，已经反馈，服务端没处理
					item.repairPhoto = values[11].split(ParamsUtil.ITEM_IMG_DIVIDER);
				}
				dataBean.datas.add(item);
			}
		}
	}
}