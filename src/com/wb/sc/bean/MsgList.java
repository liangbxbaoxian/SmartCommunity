package com.wb.sc.bean;

import java.util.List;

public class MsgList {
	
	public List<Item> datas;
	
	public class Item {
		public int type;
		public String name;
		public String time;
		public String desc;
	}
}
