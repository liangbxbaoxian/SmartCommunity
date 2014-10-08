package com.wb.sc.dialog;

import com.wb.sc.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * 
 * @描述：确认类型对话框
 * @作者：liang bao xian
 * @时间：2014年8月1日 下午3:18:17
 */
public class ConfirmDialog {
	
	/**
	 * 带确认和取消两个按钮的对话框
	 * @param context
	 * @param titleId 为-1时不显示标题
	 * @param messageId
	 * @param confirmListener
	 * @return
	 */
	public Dialog getDialog(Context context, int titleId, int messageId, DialogInterface.OnClickListener confirmListener) {
		String title = context.getString(titleId);
		String message = context.getString(messageId);
		return getDialog(context, title, message, confirmListener);
	}
	
	/**
	 * 带确认和取消两个按钮的对话框
	 * @param context
	 * @param titleId 为null时不显示标题
	 * @param messageId
	 * @param confirmListener
	 * @return
	 */
	public Dialog getDialog(Context context, String title, String message, DialogInterface.OnClickListener confirmListener) {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
		if(title != null) {
			dialogBuilder.setTitle(title);
		}
		
		return dialogBuilder.setMessage(message)
        .setPositiveButton(R.string.dialog_confirm, confirmListener)
        .setNegativeButton(R.string.dialog_cancle, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            	dialog.dismiss();                
            }
        })
        .create();
	}
		
	/**
	 * 
	 * @描述: 只有确认按钮的对话框
	 * @param context
	 * @param titleId
	 * @param messageId
	 * @return
	 */
	public Dialog getConfirmDialog(Context context, int titleId, int messageId) {
		return getConfirmDialog(context, context.getResources().getString(titleId), context.getResources().getString(messageId));
	}
	
	/**
	 * 
	 * @描述: 只有确认按钮的对话框
	 * @param context
	 * @param titleId
	 * @param messageId
	 * @return
	 */
	public Dialog getConfirmDialog(Context context, String title, String message) {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
		if(title != null) {
			dialogBuilder.setTitle(title);
		}
		
		return dialogBuilder.setMessage(message)
        .setPositiveButton(R.string.dialog_confirm, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            	dialog.dismiss();                
            }
        })
        .create();
	}
}
