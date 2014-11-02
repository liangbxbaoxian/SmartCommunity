package com.wb.sc.bean;

import java.util.List;


public class PostDetail extends BaseBean{

	/**
	 * 发帖人id
	 */
	public String sourceId;
	/**
	 * 发帖人名称
	 */
	public String sourceName;
	/**
	 * 发帖人头像
	 */
	public String sourceAvatarUrl;
	/**
	 * 帖子标题
	 */
	public String title;
	/**
	 * 帖子内容
	 */
	public String content;
	/**
	 * 帖子图片链接
	 */
	public List<String> imgList;
	/**
	 * 发帖时间
	 */
	public String time;
	/**
	 * 帖子被赞数
	 */
	public String favNum;
	/**
	 * 帖子评论数
	 */
	public String commentNum;
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
