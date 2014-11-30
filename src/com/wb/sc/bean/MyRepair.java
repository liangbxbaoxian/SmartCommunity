package com.wb.sc.bean;

import java.util.List;

public class MyRepair extends BaseBean {

	public int totalNum;                  // 数据总数	hex(4)	返回列表时，列表的总数
	public boolean hasNextPage;        
	public List<MyRepairItem> datas;

	public class MyRepairItem { 
		public String repairId;              // 工单id
		public String repairTitle;           // 工单标题
		public String repairStatus;          // 工单状态
		public String repairStatusName;      // 工单状态名称
		public String repairContent;         // 工单内容
		public String repairSubmitTime;      // 工单提交时间
		public String repairHanldeTime;      // 工单受理时间
		public String repairEndTime;         // 工单最后处理时间
		public String repairMaster;          // 最后处理人
		public String repairReuslt;          // 处理结果
		public String []repairPhoto;         // 处理结果照片
	}
	
}
