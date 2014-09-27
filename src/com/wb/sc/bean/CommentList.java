package com.wb.sc.bean;

import java.util.List;

public class CommentList extends BaseBean{

    public boolean hasNextPage;
    public int totalNum;
    public List<Item> datas;
    		
    public class Item { 
    	public String id;
    }
	
}
