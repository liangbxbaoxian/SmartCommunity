package com.wb.sc.bean;

public class UpdateApp extends BaseBean {
	
	public String newVersionNum; // 最新版本编号	char(64)
	public String updateTime; // 更新时间	char(14)
	public String updateDesc; // 更新说明	char(256)
	public String updateAppUrl; // 下载地址	char(256)
	public String versionSerial; //版本序号	hex(4)
	public String isForce; // 是否强制升级	char(1)	0：否，1：是
}
