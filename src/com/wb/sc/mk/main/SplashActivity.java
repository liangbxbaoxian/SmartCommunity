package com.wb.sc.mk.main;

import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import com.wb.sc.R;
import com.wb.sc.activity.base.BaseActivity;
import com.wb.sc.mk.personal.LoginActivity;
import com.wb.sc.util.Constans;
import com.wb.sc.util.PreferencesUtils;
import com.wb.sc.widget.CustomDialog;
import com.wb.sc.widget.CustomDialog.DialogFinish;

public class SplashActivity extends BaseActivity {

	private static int SPLASH_TIME = 3000;

	// private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);

		boolean isCancel = PreferencesUtils.getBoolean(this, Constans.CANCEL_SHORT_CUT_REMIND);
		if (!hasShortCut() && !isCancel) {
			CustomDialog dialog = new CustomDialog(this, R.style.mystyle, R.layout.shortcut_dialog, new DialogFinish(){

				@Override
				public void getFinish() {
					SPLASH_TIME = 100;
					addShortcut();
					goToHome();
				}});
			dialog.show();
			dialog.setOnDismissListener(new OnDismissListener() {       //临时处理
				
				@Override
				public void onDismiss(DialogInterface arg0) {
					SPLASH_TIME = 100;
					goToHome();
					PreferencesUtils.putBoolean(SplashActivity.this, Constans.CANCEL_SHORT_CUT_REMIND, true);
				}
			});
		} else {
			goToHome();
		}
		

	}

	private void goToHome () {
		new Handler().postDelayed(new Runnable() {
			public void run() {
				Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
				startActivity(intent);
				SplashActivity.this.finish();
			}
		}, SPLASH_TIME);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}

	@Override
	public void getIntentData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub

	}

//	private boolean hasShortcut() {
//		boolean isInstallShortcut = false;
//		final ContentResolver cr = this.getContentResolver();
//		final String AUTHORITY = "com.android.launcher.settings";
//		final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
//				+ "/favorites?notify=true");
//		Cursor c = cr
//				.query(CONTENT_URI,
//						new String[] { "title", "iconResource" },
//						"title=?",
//						new String[] { getResources().getString(R.string.app_name).trim() },
//						null);
//		if (c != null && c.getCount() > 0) {
//			isInstallShortcut = true;
//		}
//		return isInstallShortcut;
//	}
	
	public  boolean hasShortCut() { 
		  Uri uri = null; 
		  String spermi =  getAuthorityFromPermission(this,"READ_SETTINGS"); 
		  if(getSystemVersion() < 8){ 
		      uri = Uri.parse("content://"+spermi+"/favorites?notify=true"); 
		  }else{
		      uri = Uri.parse("content://"+spermi+"/favorites?notify=true"); 
		  } 
		  final ContentResolver cr = getContentResolver();
		  Cursor cursor = cr.query(uri,new String[] {"title","iconResource" },"title=?", new String[] {getString(R.string.app_name)}, null); 
		  if (cursor != null&& cursor.getCount() > 0) { 
			  cursor.close(); 
			  return true; 
		  }else {
		      return false; 
		  }
	}

	public  String getAuthorityFromPermission(Context context, String permission){
		
		if (permission == null) return null;  
		List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(PackageManager.GET_PROVIDERS);   
		if (packs != null) {  
			for (PackageInfo pack : packs) {
				
				ProviderInfo[] providers = pack.providers; 
				if (providers != null) {
					for (ProviderInfo provider : providers) {
						if (provider.readPermission != null) {
							if ((provider.readPermission).contains(permission)){
								return provider.authority;
							}
						}
					}					
				}
			}

		}

		return null;

	}

	public int getSystemVersion(){ 
		return android.os.Build.VERSION.SDK_INT; 
	} 

	private void addShortcut(){  
		Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");  

		//快捷方式的名称  
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name));  
		shortcut.putExtra("duplicate", false); //不允许重复创建  
		Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
		shortcutIntent.setClassName(this, this.getClass().getName());
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);

		//快捷方式的图标  
		ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(this, R.drawable.ic_launcher);  
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);  

		sendBroadcast(shortcut);  
	} 

}
