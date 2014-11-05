package com.wb.sc.util;

public class ImgUrlUtil {
	
	public static String getSmallUrl(String imgUrl) {
		int index = imgUrl.lastIndexOf(".");
		if(index != -1) {
			String part1 = imgUrl.substring(0, index);
			String part2 = imgUrl.substring(index);
			return part1 + "_small" + part2;
		} else {
			return imgUrl;
		}
	}
}
