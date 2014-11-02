package com.wb.sc.parser;


import java.util.ArrayList;

import android.text.TextUtils;

import com.wb.sc.bean.PostDetail;
import com.wb.sc.util.ParamsUtil;

public class PostDetailParser {

	public void parse(PostDetail baseBean) {	
			
		//进行数据解析处理
		baseBean.sourceId = ParamsUtil.getRespParamNext(baseBean, 64);
		baseBean.sourceName = ParamsUtil.getRespParamNext(baseBean, 64);
		baseBean.sourceAvatarUrl = ParamsUtil.getRespParamNext(baseBean, 256);
		baseBean.title = ParamsUtil.getRespParamNext(baseBean, 64);
		baseBean.content = ParamsUtil.getRespParamNext(baseBean, 256);
		String[] imgs = ParamsUtil.getRespParamNext(baseBean, 1024).split(ParamsUtil.ITEMS_DIVIDER);
		baseBean.imgList = new ArrayList<String>();
		for(String img :imgs) {
			if(!TextUtils.isEmpty(img)) {
				baseBean.imgList.add(img);
			}
		}
		baseBean.time = ParamsUtil.getRespParamNext(baseBean, 16);
		baseBean.favNum = ParamsUtil.getRespParamNext(baseBean, 4);
		baseBean.commentNum = ParamsUtil.getRespParamNext(baseBean, 4);
		baseBean.isGoodPost = ParamsUtil.getRespParamNext(baseBean, 2);
		baseBean.isTop = ParamsUtil.getRespParamNext(baseBean, 2);
		baseBean.state = ParamsUtil.getRespParamNext(baseBean, 2);
		baseBean.stateName = ParamsUtil.getRespParamNext(baseBean, 64);
		baseBean.handleExplain = ParamsUtil.getRespParamNext(baseBean, 256);
		baseBean.handleImgUrl = ParamsUtil.getRespParamNext(baseBean, 512);
	}
}