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
			String data = appInfo.metaData.getString(key);
			return data;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

}
