package com.wb.sc.bean;

import java.util.List;

public class PostList extends BaseBean{

    public int totalNum;
    public boolean hasNextPage;
    public List<Item> datas;
    
    public class Item { 
    	/**
    	 * 
    	 */
    	public String id;
    	
    	/**
    	 * 
    	 */
    	public String title;
    	
    	/**
    	 * 
    	 */
    	public String type;
    	
    	/**
    	 * 
    	 */
    	public String typeName;
    	
    	/**
    	 * 
    	 */
    	public int favNum;
    	
    	public String time;
    	
    	public String source;
    	    	
    }
	
}
