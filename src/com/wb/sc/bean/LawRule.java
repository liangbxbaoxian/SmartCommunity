package com.wb.sc.bean;

import java.util.List;

public class LawRule extends BaseBean {
	
	 public int totalNum;                  // 数据总数	hex(4)	返回列表时，列表的总数
	    public boolean hasNextPage;        
	    public List<LawRuleItem> datas;
	    		
	    public class LawRuleItem { 
	    	public String lawRuleId;          // 法规id
	    	public String lawRuleTitle;       // 法规标题-
	    	public String lawRuleContent;     // 法规内容
	    	public String lawRuleTime;        // 发布时间
	    }
}
