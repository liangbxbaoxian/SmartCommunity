package com.wb.sc.bean;

import java.util.List;

public class Dictionary extends BaseBean {

	public int totalNum;                  // 数据总数	hex(4)	返回列表时，列表的总数
	public boolean hasNextPage;        
	public List<DictionaryItem> datas;

}
