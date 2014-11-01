package com.wb.sc.bean;

import java.util.List;

public class PhoneList extends BaseBean{

    public int totalNum;
    public boolean hasNextPage;
    public List<Item> datas;
    		
    public class Item { 
    	public String id;
    	public String number;
    	public String remarks;
    }
	
}
