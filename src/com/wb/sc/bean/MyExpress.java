package com.wb.sc.bean;

import java.util.List;

public class MyExpress extends BaseBean {
	
	 public int totalNum;                  // 数据总数	hex(4)	返回列表时，列表的总数
	    public boolean hasNextPage;        
	    public List<ExpressItem> datas;
	    		
	    public class ExpressItem { 
	    	public String id;         // 快件箱Id
	    	public String desc;       // 快件箱名称
	    	public String cabinetId;  // 快件箱组别编号
	    	public String cabinetNum; // 柜子编号
	    	public String orderNum;   // 订单号
	    	public String takeNum;     // 取件码
	    	public String expressCompany;   // 快递公司名称
	    	public String expressCompanyNo;   //快递公司代码
	    	public String courierName;    // 快递员名字
	    	public String courierTel;     // 快递员电话
	    	public String takeUserTel;     // 取件用户电话
	    	public String saveTime;         // 存件时间
	    	public String takeTime;       // 取件时间|
	    	public String lng;            // 快件箱经度
	    	public String lat;            // 快件箱纬度
	    	public String addrDetial;     // 快件箱详细地址
	    	public String cabinetStatu;   // 快件当前状态
	    }
}
