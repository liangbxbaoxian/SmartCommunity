package com.wb.sc.config;

/**
 * 此类定义Intent跳转携带的参数名称
 * @author liangbx
 *
 */
public interface IntentExtraConfig {
	
	/***************************** 帖子  **********************************************/
	public static final String POST_TYPE = "post_type";
	public static final int POST_TYPE_SHARE = 0;
	public static final int POST_TYPE_HELPE = 1;
	public static final int POST_TYPE_LOVE = 2;
	public static final String MSG_NUM = "msg_num";
	public static final String FAV_NUM = "fav_num";
		
	/***************************** 详情页  **********************************************/
	public static final String DETAIL_ID = "id";
	public static final String DETAIL_TYPE = "type";
	
	/***************************** 房屋交易类型  **********************************************/
	public static final String HOUSE_TRADE_TYPE = "trade_type";
	/**
	 * 出售
	 */
	public static final int HOUSE_TRADE_TYPE_SALE = 0;
	/**
	 * 出租
	 */
	public static final int HOUSE_TRADE_TYPE_LEASE = 1;
	
	/***************************** 浏览器   **********************************************/
	public static final String BROWSER_TITLE = "title";
	public static final String BROWSER_URL = "url";
	
	/***************************** 图片浏览 ********************************************/
	public static final String IMAGE_BROWSER_DATA = "image";	
	public static final String IMAGE_BROWSER_DIS_TAB = "dis_tab";
	public static final String IMAGE_BROWSER_POS = "img_pos";	
}
