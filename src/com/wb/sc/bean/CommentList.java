package com.wb.sc.bean;

import java.util.List;

public class CommentList extends BaseBean{

    public boolean hasNextPage;
    public int totalNum;
    public List<Item> datas;
    		
    public class Item { 
    	/**
    	 * 评论ID
    	 */
    	public String id;
    	
    	/**
    	 * 评论人ID
    	 */
    	public String sourceId;
    	
    	/**
    	 * 评论人头像
    	 */
    	public String sourceAvatar;
    	
    	/**
    	 * 评论人名称
    	 */
    	public String sourceName;
    	
    	/**
    	 * 评论内容
    	 */
    	public String content;
    	
    	/**
    	 * 父评论人ID
    	 */
    	public String parentSourceId;
    	
    	/**
    	 * 父评论人名称
    	 */
    	public String parentSourceName;
    	
    	/**
    	 * 评论时间
    	 */
    	public String time;
    }
	
}
