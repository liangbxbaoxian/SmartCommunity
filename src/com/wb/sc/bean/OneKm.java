package com.wb.sc.bean;

import java.util.List;

public class OneKm extends BaseBean {
	
	 public int totalNum;                  // 数据总数	hex(4)	返回列表时，列表的总数
	    public boolean hasNextPage;        
	    public List<MerchantItem> datas;
	    		
	    public class MerchantItem { 
	    	public String merchantId;         // 户id
	    	public String merchantName;       // 商户名称
	    	public String merchantCategoryId;  // 商户类别
	    	public String merchantCategoryName; // 商户类别名称
	    	public String merchantTel; // 电话号码
	    	public String longitude;   // 经度
	    	public String latitude;     // 维度
	    	public String merchantLogo;    // 标题图片
	    }
}
