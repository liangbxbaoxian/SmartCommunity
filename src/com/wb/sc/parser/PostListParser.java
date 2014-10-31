package com.wb.sc.parser;


import java.util.ArrayList;

import android.text.TextUtils;

import com.wb.sc.bean.PostList;
import com.wb.sc.bean.PostList.Item;
import com.wb.sc.util.ParamsUtil;

public class PostListParser {

	public void parse(PostList dataBean) {	
			
		//进行数据解析处理
		dataBean.totalNum = ParamsUtil.getRespIntParamNext(dataBean, 4);
		dataBean.hasNextPage = ParamsUtil.getNextPageFlag(dataBean);
		if(dataBean.totalNum == 0) return;
		
		int dataLength = dataBean.dataBytes.length - 9 - 1;
		String datasStr = ParamsUtil.getRespParam(dataBean, 9, dataLength);
		String[] itemsStr = datasStr.split(ParamsUtil.ITEMS_DIVIDER);
		dataBean.datas = new ArrayList<PostList.Item>();
		for(String itemStr : itemsStr) {
			String[] values = itemStr.split(ParamsUtil.ITEM_DIVIDER);
			Item item = dataBean.new Item();
			item.id = values[0];
			item.title = values[1];
			item.type = values[2];
			item.typeName = values[3];
			item.favNum = values[4];
			item.time = values[5];
			item.source = values[6];
			item.name = values[7];
			item.commentNum = values[8];
			item.content = values[9];
			String[] imgs = values[10].split(ParamsUtil.ITEM_IMG_DIVIDER);
			item.imgList = new ArrayList<String>();
			for(String img : imgs) {
				if(!TextUtils.isEmpty(img)) {
					item.imgList.add(img);
				}
			}
		}
	}
}