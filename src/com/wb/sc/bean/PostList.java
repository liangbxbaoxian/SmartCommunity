package com.wb.sc.bean;

import java.util.List;

public class PostList extends BaseBean{

    public int totalNum;
    public List<Item> datas;
    public boolean hasNextPage;
    		
    public class Item { 
    	public String id;
    }
	
}
