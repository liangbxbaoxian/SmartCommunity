package com.wb.sc.config;

/**
 * 
 * @描述：定义网络接口方法名称
 * @作者：liang bao xian
 * @时间：2014年8月6日 下午5:37:51
 */
public interface NetInterface {
	
	/***************************** 资讯  **********************************************/
	public static final String METHOD_NEWS_LIST = "newsList.do";
	public static final String METHOD_NEWS_DETAIL= "newsDetail.do";
	
	/***************************** 评论  **********************************************/
	public static final String METHOD_COMMENT_LIST = "commentList.do";
	public static final String METHOD_COMMENT = "comment.do";
	
	/***************************** 登录注册  **********************************************/
	public static final String METHOD_LOGIN = "login.do";
	public static final String METHOD_REGISTER = "register.do";
	public static final String METHOD_MODIFY_AVATAR = "modifyAvatar.do";
	public static final String METHOD_MODIFY_NICKNAME = "modifyNickname.do";	
	public static final String METHOD_MODIFY_PASSWORD = "modifyPwd.do";
	public static final String METHOD_MODIFY_GENDER = "modifyGender.do";
	
	/***************************** 栏目  **********************************************/
	public static final String METHOD_CHANNEL = "channel.do";
	
	/***************************** 广告  **********************************************/
	public static final String METHOD_SCROLL_NEWS = "scrollNews.do";
	public static final String METHOD_WELCOME_ADV = "advertisement.do";
	
	/***************************** 收藏  **********************************************/
	public static final String METHOD_COLLECT = "collect.do";
	public static final String METHOD_MY_COLLECT = "myCollected.do";
	
	/***************************** 搜索 **********************************************/
	public static final String METHOD_SEARCH = "search.do";
	
	/***************************** 点赞  **********************************************/
	public static final String METHOD_FAVOUR = "favour.do";
	
	/***************************** 投票  **********************************************/
	public static final String METHOD_VOTE_LIST = "voteList.do";
	public static final String METHOD_VOTE_DETAIL = "voteDetail.do";
	public static final String METHOD_VOTE_SATISTICS = "voteSatistics.do";
	
	/***************************** 二手市场  **********************************************/
	public static final String METHOD_OLD_INFO_LIST = "oldInfoList.do";
	public static final String METHOD_OLD_INFO_DETAIL = "oldInfoDetail.do";
	public static final String METHOD_PUBLISH_OLD_INFO = "publishOldInfo.do";
	
	/***************************** 随手拍  **********************************************/
	public static final String METHOD_SHOOT_LIST = "shootList.do";
	public static final String METHOD_SHOOT_DETAIL = "shootDetail.do";
	public static final String METHOD_PUBLISH_SHOOT = "publishShootInfo.do";
	
	public static final String METHOD_PHOTO_UPLOAD = "photoUploading.do";
	
	/***************************** 房地产  **********************************************/
	public static final String METHOD_ESTATE_LIST = "estateList.do";
	public static final String METHOD_ESTATE_DETAIL = "estateDetail.do";
	
	/***************************** 删除已发布的二手信息  **************************************/
	public static final String METHOD_DEL_PUSH_INFO = "delPushInfo.do";
	
	/***************************** 关于  **************************************/
	public static final String METHOD_ABOUT = "about.do";
}
