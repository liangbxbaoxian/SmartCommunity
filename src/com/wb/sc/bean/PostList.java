package com.wb.sc.bean;

import java.util.List;

public class PostList extends BaseBean{

    public int totalNum;
    public List<Item> datas;
    public boolean hasNextPage;
    		
    public class Item { 
    	/**
    	 * 帖子id
    	 */
    	public String id;
    	/**
    	 * 帖子标题
    	 */
    	public String title;
    	/**
    	 * 帖子类型
    	 */
    	public String type;
    	/**
    	 * 帖子类型名称
    	 */
    	public String typeName;
    	/**
    	 * 帖子被赞数
    	 */
    	public String favNum;
    	/**
    	 * 发帖时间
    	 */
    	public String time;
    	/**
    	 * 发帖人(即帖子来源)
    	 */
    	public String source;
    	/**
    	 * 发帖名称
    	 */
    	public String name;
    	/**
    	 * 帖子评论数
    	 */
    	public String commentNum;
    	/**
    	 * 帖子内容
    	 */
    	public String content;
    	/**
    	 * 帖子图片链接
    	 */
    	public List<String> imgList;
    }
	
}
