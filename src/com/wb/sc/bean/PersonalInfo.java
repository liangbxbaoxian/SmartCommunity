package com.wb.sc.bean;

public class PersonalInfo extends BaseBean {
	
	public String accountName; // 帐号名称 char(32)
	public String realName; // 真实姓名 char(32)
	public String phoneNum; // 手机号码 char(15)
	public String birthday; // 生日 char(8)
	public String sex; // 性别 char(2)
	public String weixinAccount; // 微信号 char(32)
	public String mail; // 用户邮箱 char(64)
	public String hobby; // 兴趣爱好 char(64)
	public String userStatue; // 用户状态 char(1)
	public String protrait; // 头像图片地址 char(256)
	public String id; // 身份证号码 char(32)
	public String registerTime; // 注册时间 char(14)
	public String notDisturbStatu; // 免打扰状态 char(1)
	public String disturbBeginTime; // 免打扰开始时间 char(14)
	public String disturbEndTime; // 免打扰结束时间 char(14)
	public String localCommunity; // 所在社区 char(64)
	public String communityName; // 社区名称 char(128)
	public String auth;          // 01:已提交认证；
								// 02:认证通过；
								// 03:认证失败；
								// 没值或其他值为未认证
	
	public String buildingNum;  // 楼栋
	
	public String roomNum;      // 房号

	
	public String imei; // 手机imei char(32)
	public String crc; // 校验位 hex(2)

}
