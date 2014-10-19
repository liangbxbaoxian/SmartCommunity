package com.wb.sc.bean;

import java.util.List;

public class Adv extends BaseBean{

    public int totalNum;
    public boolean hasNextPage;
    public List<Item> datas;
    		
    public class Item { 
    	public String id;
    	public String title;
    	public String content;
    	public String order;
    	public String type;
    	public String imgUrl;
    	public String linkUrl;
    }
	
}
