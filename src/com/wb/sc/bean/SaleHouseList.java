package com.wb.sc.bean;

import java.util.List;

public class SaleHouseList extends BaseBean{

    public int totalNum;
    public boolean hasNextPage;
    public List<Item> datas;
    		
    public class Item { 
    	public String id;
    	public String totalPrice;
    	public String priceDesc;
    	public String type;
    	public String area;
    	public String year;
    	public String orientation;
    	public String floor;
    	public String structure;
    	public String finish;
    	public String category;
    	public String properties;
    	public String time;
    	public String configuration;
    	public String phone;
    	public List<String> imgList;
    }
	
}
