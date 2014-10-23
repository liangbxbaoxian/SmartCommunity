package com.wb.sc.bean;

import java.util.List;

public class ComNotice extends BaseBean {

	public int totalNum;
	public boolean hasNextPage;
	public List<Item> datas;

	public class Item {
		public String id;
		public String title;
		public String content;
		public String source;
		public String time;
	}

}
