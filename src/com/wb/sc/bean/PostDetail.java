package com.wb.sc.bean;

import java.util.List;


public class PostDetail extends BaseBean{

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
	/**
	 * 是否精帖
	 * 0：否，1：是
	 */
	public String isGoodPost;
	/**
	 * 是否置顶
	 * 0：否，1：是
	 */
	public String isTop;
	/**
	 * 工单处理状态
	 */
	public String state;
	/***
	 * 工单处理状态名称
	 */
	public String stateName;
	/**
	 * 处理说明
	 */
	public String handleExplain;
	/**
	 * 处理结果图片地址
	 */
	public String handleImgUrl;
}
