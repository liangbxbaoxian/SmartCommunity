package com.wb.sc.bean;

import com.wb.sc.config.DbConfig;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

@Table(name=DbConfig.TN_USER)
public class User extends BaseBean{
	
	@Id(column="id")
	public int id;
	
	/**
	 * 用户ID
	 */
    public String userId;
    /**
     * 社区ID
     */
    public String communityId;
    
    /**
     * 帐号名称
     */
    public String account;
    
    /**
     * 真实姓名
     */
    public String name;
    
    /**
     * 头像地址
     */
    public String avatarUrl;
    
    /**
     * 手机号码
     */
    public String phone;
    
    /**
     * 用户密码
     */
    public String pssword;
    
    public String communityName;
    
    public String auth;
    
    public String roomNum;
    
	public User() {
		userId = "";
		communityId = "db8eeb11-3e04-4eae-9c05-fd572abb1733";
	}
    
    
    /**
	 * 登录状态 0：为登出 1：为登录
	 */
	public int isLogin;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCommunityId() {
		return communityId;
	}

	public void setCommunityId(String communityId) {
		this.communityId = communityId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public int getIsLogin() {
		return isLogin;
	}

	public void setIsLogin(int isLogin) {
		this.isLogin = isLogin;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPssword() {
		return pssword;
	}

	public void setPssword(String pssword) {
		this.pssword = pssword;
	}	
	
	
}
