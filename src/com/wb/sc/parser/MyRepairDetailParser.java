package com.wb.sc.parser;


import java.util.ArrayList;

import com.wb.sc.bean.MyRepairDetail;
import com.wb.sc.bean.MyRepairDetail.PhotoItem;
import com.wb.sc.util.ParamsUtil;

public class MyRepairDetailParser {

	public void parse(MyRepairDetail dataBean) {	
			
		
		dataBean.repairId = ParamsUtil.getRespParamNext(dataBean, 64);
		dataBean.repairStatus = ParamsUtil.getRespParamNext(dataBean, 2);
		dataBean.repairStatusName = ParamsUtil.getRespParamNext(dataBean, 64);
		dataBean.repairTitle = ParamsUtil.getRespParamNext(dataBean, 50);
		dataBean.repairContent = ParamsUtil.getRespParamNext(dataBean, 512);
		//进行数据解析处理		
		int dataLength = dataBean.dataBytes.length - 5 -64 -2 -64 -50 -512;  //接口未定义有下一页                   次解析有问题！！！待后续修复
		String datasStr = ParamsUtil.getRespParam(dataBean, 5, 1024);
		String[] itemsStr = datasStr.split(ParamsUtil.ITEMS_DIVIDER);
		dataBean.datas = new ArrayList<MyRepairDetail.PhotoItem>();
		for(String itemStr : itemsStr) {
			PhotoItem item =  dataBean. new PhotoItem();
			item.repairPhoto = itemStr;
			dataBean.datas.add(item);
		}
		dataBean.repairEndTime = ParamsUtil.getRespParamNext(dataBean, 16);
	}
}