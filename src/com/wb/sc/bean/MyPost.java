package com.wb.sc.bean;

import java.util.List;

public class MyPost extends BaseBean {

	public int totalNum;                  // 数据总数	hex(4)	返回列表时，列表的总数
	public boolean hasNextPage;        
	public List<MyPostItem> datas;

	public class MyPostItem { 
		public String postId;            // 帖子id
		public String postTitle;         // 帖子标题
		public String postType;          // 帖子类型
		public String postSupportNum;    // 通知人
		public String postTime;          // 通知时间
		public String postMaster;        // 发帖人
		public String postName;          // 发帖名称 
	}
}
