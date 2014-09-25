/*
 * Powered By [sid-framework]
 * Web Site: http://www.rapid-framework.org.cn
 * Google Code: http://code.google.com/p/rapid-framework/
 * Since 2008 - 2014
 */

package com.wb.sc.bean;


public class CategoryTable implements java.io.Serializable {
	private static final long serialVersionUID = 5454155825314635342L;
	
	/*** 分类ID  说明:主键 ***/
	private java.lang.Long categoryid;
	
	/*** 类别ID  说明:类别ID ***/
	private java.lang.Long typeid;
	
	/*** 商家ID  说明:商家ID ***/
	private java.lang.Long bussinessid;
	
	/*** 外部系统编号  说明:外部系统编号 ***/
	private java.lang.String producttype;
	
	/*** 分类名称  说明:分类名称 ***/
	private java.lang.String categoryname;
	
	/*** 父分类ID  说明:父分类ID，根分类ID为0 ***/
	private java.lang.Long fcid;
	
	/*** 分类图标  说明:分类图标URL ***/
	private java.lang.String categoryicon;
	
	/*** 类别  说明:类别 0：超便民 1：超优惠 ***/
	private java.lang.Long categorytype;
	
	/*** 备注  说明:备注 ***/
	private java.lang.String remark;
	
	private String tag;
	
	private String color;
	
	private String categorylogo;
	
	private Long isleaf;
	private String picSize;
	
	private int id;
	
	public Long getIsleaf() {
		return isleaf;
	}

	public void setIsleaf(Long isleaf) {
		this.isleaf = isleaf;
	}

	public void setCategoryid(java.lang.Long value) {
		this.categoryid = value;
	}
	
	public java.lang.Long getCategoryid() {
		return this.categoryid;
	}
	public void setTypeid(java.lang.Long value) {
		this.typeid = value;
	}
	
	public java.lang.Long getTypeid() {
		return this.typeid;
	}
	public void setBussinessid(java.lang.Long value) {
		this.bussinessid = value;
	}
	
	public java.lang.Long getBussinessid() {
		return this.bussinessid;
	}
	public void setProducttype(java.lang.String value) {
		this.producttype = value;
	}
	
	public java.lang.String getProducttype() {
		return this.producttype;
	}
	public void setCategoryname(java.lang.String value) {
		this.categoryname = value;
	}
	
	public java.lang.String getCategoryname() {
		return this.categoryname;
	}
	public void setFcid(java.lang.Long value) {
		this.fcid = value;
	}
	
	public java.lang.Long getFcid() {
		return this.fcid;
	}
	public void setCategoryicon(java.lang.String value) {
		this.categoryicon = value;
	}
	
	public java.lang.String getCategoryicon() {
		return this.categoryicon;
	}
	public void setCategorytype(java.lang.Long value) {
		this.categorytype = value;
	}
	
	public java.lang.Long getCategorytype() {
		return this.categorytype;
	}
	public void setRemark(java.lang.String value) {
		this.remark = value;
	}
	
	public java.lang.String getRemark() {
		return this.remark;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getCategorylogo() {
		return categorylogo;
	}

	public void setCategorylogo(String categorylogo) {
		this.categorylogo = categorylogo;
	}

	public String getPicSize() {
		return picSize;
	}

	public void setPicSize(String picSize) {
		this.picSize = picSize;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
