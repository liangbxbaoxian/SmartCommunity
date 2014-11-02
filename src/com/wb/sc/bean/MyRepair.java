package com.wb.sc.bean;

import java.util.List;

public class MyRepair extends BaseBean {

	public int totalNum;                  // 数据总数	hex(4)	返回列表时，列表的总数
	public boolean hasNextPage;        
	public List<MyRepairItem> datas;

	public class MyRepairItem { 
		public String repairId;              // 帖子id
		public String repairTitle;           // 帖子标题
		public String repairStatus;          // 帖子类型
		public String repairSubmitTime;      // 工单提交时间
		public String repairEndTime;         // 工单最后处理时间
		public String repairMaster;          // 最后处理人
		public String repairPhoto;           // 处理结果照片
	}
}
