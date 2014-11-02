package com.wb.sc.bean;

import java.util.List;

public class Community extends BaseBean {

	public int totalNum;                  // 数据总数	hex(4)	返回列表时，列表的总数
	public boolean hasNextPage;        
	public List<CommunityItem> datas;
	
    public class CommunityItem { 
    	public String communityId;          // 社区id
    	public String communityCode;         // 社区编号
    	public String communityName;       // 社区名称
    }

}
