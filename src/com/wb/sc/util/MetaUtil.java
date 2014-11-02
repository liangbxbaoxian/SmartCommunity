package com.wb.sc.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class MetaUtil {

	public static String readMeta(Context ctx, String key) {
		ApplicationInfo appInfo;
		try {
			appInfo = ctx.getPackageManager()
					.getApplicationInfo(ctx.getPackageName(),
							PackageManager.GET_META_DATA);
			String data = "0000"  + appInfo.metaData.getInt("APP_CHANNEL");  //此处有做特殊处理
			return data;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

}
