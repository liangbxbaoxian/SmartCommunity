package com.wb.sc.bean;

import java.util.List;

public class MsgCenter extends BaseBean {

	public int totalNum;                  // 数据总数	hex(4)	返回列表时，列表的总数
	public boolean hasNextPage;        
	public List<MsgItem> datas;

	public class MsgItem { 
		public String bulletinId;         // 公告id
		public String bulletinTitle;      // 公告标题
		public String bulletinContent;    // 柜子单元编号
		public String notifier;           // 通知人
		public String notifyTime;         // 通知时间
	}
}
