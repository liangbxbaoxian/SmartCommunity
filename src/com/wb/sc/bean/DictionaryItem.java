package com.wb.sc.bean;

import java.io.Serializable;

public class DictionaryItem implements Serializable {
	
	private static final long serialVersionUID = 250459421520590675L;
	
	public String id;                   // 字典id- 
	public String dictionaryId;        // 字典类型-
	public String dictionaryCode;      // 字典代码
	public String superDictionaryId;   // 父id
	public String dictionaryName;      // 字典名称
	public String dictionarySort;      // 排序
	public String superId;
	
	public String getDictionaryId() {
		return dictionaryId;
	}
	public void setDictionaryId(String dictionaryId) {
		this.dictionaryId = dictionaryId;
	}
	public String getDictionaryCode() {
		return dictionaryCode;
	}
	public void setDictionaryCode(String dictionaryCode) {
		this.dictionaryCode = dictionaryCode;
	}
	public String getSuperDictionaryId() {
		return superDictionaryId;
	}
	public void setSuperDictionaryId(String superDictionaryId) {
		this.superDictionaryId = superDictionaryId;
	}
	public String getDictionaryName() {
		return dictionaryName;
	}
	public void setDictionaryName(String dictionaryName) {
		this.dictionaryName = dictionaryName;
	}
	public String getDictionarySort() {
		return dictionarySort;
	}
	public void setDictionarySort(String dictionarySort) {
		this.dictionarySort = dictionarySort;
	}
	
}