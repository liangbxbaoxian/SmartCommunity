package com.wb.sc.dialog;

import com.wb.sc.app.SCApp;
import com.wb.sc.mk.personal.LoginActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

/**
 * 
 * @描述：提示登录对话框
 * @作者：liang bao xian
 * @时间：2014年10月23日 上午10:22:15
 */
public class ToastLoginDialog {
	
	/**
	 * 
	 * @描述: 判断是否已经登录过，未登录，则弹出登录提示框
	 * @param context
	 * @return
	 */
	public static boolean checkLogin(Context context) {
		boolean isLogin = false;
		if(SCApp.getInstance().getUser().isLogin == 1) {
			isLogin = true;		
		} else {
			show(context);
		}
		
		return isLogin;
	}
	
	public static void show(final Context context) {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
		dialogBuilder.setTitle("登录提示");
		dialogBuilder.setMessage("您需要先登录，才可使用该功能");
		dialogBuilder.setNegativeButton("别处逛逛", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            	dialog.dismiss();                
            }
        });
		
		dialogBuilder.setPositiveButton("立即登录", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            	dialog.dismiss();   
            	context.startActivity(new Intent(context, LoginActivity.class));
            }
        });
		dialogBuilder.show();
	}
}
