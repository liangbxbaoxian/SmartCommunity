package com.wb.sc.bean;

import java.io.Serializable;
import java.util.List;

public class Dictionary extends BaseBean {

	public int totalNum;                  // 数据总数	hex(4)	返回列表时，列表的总数
	public boolean hasNextPage;        
	public List<DictionaryItem> datas;

	public class DictionaryItem implements Serializable {
		/**
		 * 
		 */
		public static final long serialVersionUID = 1L;
		public String dictionaryId;        // 字典id-
		public String dictionaryCode;      // 字典代码
		public String superDictionaryId;   // 父id
		public String dictionaryName;      // 字典名称
		public String dictionarySort;      // 排序
	}

}
