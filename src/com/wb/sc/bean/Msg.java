package com.wb.sc.bean;

import java.util.List;

public class Msg extends BaseBean {                 //消息有点混乱，所以另外定义一个类

	public int totalNum;                  // 数据总数	hex(4)	返回列表时，列表的总数
	public boolean hasNextPage;        
	public List<MgItem> datas;

	public class MgItem { 
		
		public MgItem() {
			msgTitle = "";
			msgContent = "";
			msgCreteTime = "";
		}
		
		public String msgNO;         // 消息编号
		public String msgTitle;      // 消息标题
		public String msgContent;    // 消息内容
		public String communityId;           // 社区编号
		public String msgTypeNO;         // 消息类型编号
		public String msgType;         // 消息类型名称
		public String msgCreteTime;         // 消息生成时间
	}
}
