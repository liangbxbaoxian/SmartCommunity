package com.wb.sc.bean;

import java.util.List;

public class MyRepairDetail extends BaseBean {

	
	public String repairId;                  // 工单id
	public String repairStatus;              // 工单状态
	public String repairStatusName;          // 工单状态名称
	public String repairTitle;               // 工单标题
	public String repairContent;             // 工单内容
	public List<PhotoItem> datas;            // 工单图片
	public String repairEndTime;             // 工单最后处理时间

	public class PhotoItem { 
		public String repairPhoto;           // 处理结果照片
	}
	
}
