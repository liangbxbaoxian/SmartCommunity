package com.wb.sc.bean;

import java.util.List;

public class MyExpress extends BaseBean {
	
	 public int totalNum;                  // 数据总数	hex(4)	返回列表时，列表的总数
	    public boolean hasNextPage;        
	    public List<ExpressItem> datas;
	    		
	    public class ExpressItem { 
	    	public String id;         // 快件箱编号
	    	public String desc;       // 快件箱简称
	    	public String cabinetId;  // 柜子单元编号
	    	public String cabinetNum; // 柜子编号
	    	public String trackingNum; // 运单号
	    	public String takeNum;     // 取件码
	    	public String expressCompany;   // 快递公司
	    	public String courierName;    // 快递员名字
	    	public String courierTel;     // 快递员电话
	    	public String takeUserTel;               // 取件用户电话
	    }
}
