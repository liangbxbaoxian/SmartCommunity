package com.wb.sc.bean;

public class Item {

    public String mTitle;
    public int mIconRes;
    public String name;
    public String phone;

    public Item(String title, int iconRes) {
        mTitle = title;
        mIconRes = iconRes;
    }
    
    public Item(String name, String phone) {
    	this.name = name;
    	this.phone = phone;
    }
    
}
