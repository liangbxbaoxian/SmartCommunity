package com.wb.sc.parser;


import java.util.ArrayList;

import android.text.TextUtils;

import com.wb.sc.bean.SaleHouseList;
import com.wb.sc.bean.SaleHouseList.Item;
import com.wb.sc.util.ParamsUtil;

public class SaleHouseListParser {

	public void parse(SaleHouseList dataBean) {	
			
		//进行数据解析处理
		dataBean.totalNum = ParamsUtil.getRespIntParamNext(dataBean, 4);
		dataBean.hasNextPage = ParamsUtil.getNextPageFlag(dataBean);
		dataBean.datas = new ArrayList<SaleHouseList.Item>();
		if(dataBean.totalNum == 0) return;
		
		int dataLength = dataBean.dataBytes.length - 9 - 1;
		String datasStr = ParamsUtil.getRespParam(dataBean, 9, dataLength);
		String[] itemsStr = datasStr.split(ParamsUtil.ITEMS_DIVIDER);		
		for(String itemStr : itemsStr) {
			String[] values = itemStr.split(ParamsUtil.ITEM_DIVIDER);
			Item item = dataBean.new Item();
			item.id = values[0];
			item.totalPrice = values[1];
			item.priceDesc = values[2];
			item.type = values[3];
			item.area = values[4];
			item.year = values[5];
			item.orientation = values[6];
			item.floor = values[7];
			item.structure = values[8];
			item.finish = values[9];
			item.category = values[10];
			item.properties = values[11];
			item.time = values[12];
			item.configuration = values[13];
			item.phone = values[14];
			item.imgList = new ArrayList<String>();
			if(values.length >= 16 && !TextUtils.isEmpty(values[15])) {
				String[] imgs = values[15].split(ParamsUtil.ITEM_IMG_DIVIDER);				
				for(String img : imgs) {
					if(!TextUtils.isEmpty(img) && !img.equals(" ")) {
						item.imgList.add(img);
					}
				}
			}
			dataBean.datas.add(item);
		}
	}
}