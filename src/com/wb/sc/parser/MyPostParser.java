package com.wb.sc.parser;


import java.util.ArrayList;

import com.wb.sc.bean.MyPost;
import com.wb.sc.bean.MyPost.MyPostItem;
import com.wb.sc.util.ParamsUtil;

public class MyPostParser {

	public void parse(MyPost dataBean) {	
			
		//进行数据解析处理		
		dataBean.totalNum = Integer.valueOf(ParamsUtil.getRespParamNext(dataBean, 4));
		dataBean.hasNextPage = ParamsUtil.getNextPageFlag(dataBean);
		int dataLength = dataBean.dataBytes.length - 9 - 1;  //接口未定义有下一页
		String datasStr = ParamsUtil.getRespParam(dataBean, 9, dataLength);
		String[] itemsStr = datasStr.split(ParamsUtil.ITEMS_DIVIDER);
		dataBean.datas = new ArrayList<MyPost.MyPostItem>();
		for(String itemStr : itemsStr) {
			String[] values = itemStr.split(ParamsUtil.ITEM_DIVIDER);
			MyPostItem item =  dataBean. new MyPostItem();
			item.postId = values[0];
			item.postTitle = values[1];
			item.postType = values[2];
			item.postSupportNum = values[3];
			item.postTime = values[4];
			item.postMaster = values[5];
			item.postName = values[6];
			dataBean.datas.add(item);
		}
	}
}