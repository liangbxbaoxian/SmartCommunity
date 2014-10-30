package com.wb.sc.app;

import java.security.PrivateKey;
import java.security.PublicKey;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.utils.Utils;
import android.app.Application;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.common.format.HexStringBytes;
import com.common.net.volley.VolleyX509TrustManager;
import com.common.net.volley.cache.VolleyImageCache;
import com.wb.sc.bean.User;
import com.wb.sc.config.DbConfig;
import com.wb.sc.config.DebugConfig;
import com.wb.sc.config.NetConfig;
import com.wb.sc.db.DbUpdateHandler;
import com.wb.sc.security.RSA;

public class SCApp extends Application {

	// APP实例
	private static SCApp mApp;

	// 网络
	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;
	private VolleyImageCache mImageCache;
	private long requestTag = 0;

	// 数据库
	private FinalDb mFinalDb;	
	// 公钥
	private PublicKey mPublicKey;
	// 私钥
	private PrivateKey mPrivateKey;
	// 3DES密钥
	private String des3KeyStr = "37313358324234596561343467353131";
	private byte[] mDes3Key = null;
	
	private User mUser;
	
	
	//百度地图
	public LocationClient mLocationClient;
	public GeofenceClient mGeofenceClient;
	public MyLocationListener mMyLocationListener;
	
	@Override
	public void onCreate() {
		super.onCreate();
		mApp = this;

		// Volley请求队列、图片类实例化
		mRequestQueue = Volley.newRequestQueue(getApplicationContext(), Utils
				.getDiskCacheDir(getApplicationContext(), "volley")
				.getAbsolutePath(), null);
		float density = getResources().getDisplayMetrics().density;
		mImageCache = new VolleyImageCache((int) (density * 4 * 1024 * 1024));
		mImageLoader = new ImageLoader(mRequestQueue, mImageCache);
		
		if(NetConfig.HTTPS_ENABLE) {
			//开启HTTPS功能
			VolleyX509TrustManager.allowAllSSL();
		}

		// 创建数据库
		mFinalDb = FinalDb.create(this, DbConfig.DB_NAME,
				DebugConfig.SHOW_DEBUG_MESSAGE, DbConfig.DB_VERSION,
				new DbUpdateHandler());		
		RSA rsa = new RSA();
		try {
			mPublicKey = rsa.getPublicKeyByCertificate(getApplicationContext(), "publicKey.cer");
			DebugConfig.showLog("SC_RSA", mPublicKey.toString());
			mPrivateKey = rsa.getCertInfoByKeyStore(getApplicationContext(), "privateKey.p12", "123456");
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		mDes3Key = HexStringBytes.String2Bytes(des3KeyStr);
		
		initMap();
	}

	/**
	 * 获取APP实例
	 * 
	 * @return
	 */
	public synchronized static SCApp getInstance() {
		return mApp;
	}

	/**
	 * 获取网络请求队列
	 * 
	 * @return
	 */
	public RequestQueue getRequestQueue() {
		return mRequestQueue;
	}

	/**
	 * 获取图片加载者
	 * 
	 * @return
	 */
	public ImageLoader getImageLoader() {
		return mImageLoader;
	}

	/**
	 * 获取数据库对象
	 * 
	 * @return
	 */
	public FinalDb getDb() {
		return mFinalDb;
	}

	/**
	 * 获取请求标签(保证每次返回的都不重复)
	 * 
	 * @return
	 */
	public synchronized String getRequestTag() {
		requestTag++;
		return requestTag + "";
	}
	
	/**
	 * 获取公钥
	 * @描述: 
	 * @return
	 */
	public PublicKey getPublicKey() {
		return mPublicKey;
	}
	
	/**
	 * 获取私钥
	 */
	public PrivateKey getPrivateKey() {
		return mPrivateKey;
	}
	
	/**
	 * 获取3DES密钥
	 */
	public byte[] getDes3Key() {
		return mDes3Key;
	}

	public User getUser() {
		if(mUser == null) {
			mUser = new User();
//			mUser.userId = "5de7e797-664a-49f9-843b-d79a98ec3b0d"; //测试
		}
		return mUser;
	}

	public void setUser(User mUser) {
		this.mUser = mUser;
	}		
	
	//初始化百度地图
	private void initMap() {
		mLocationClient = new LocationClient(this.getApplicationContext());
		mMyLocationListener = new MyLocationListener();
//		mLocationClient.registerLocationListener(mMyLocationListener);
		mGeofenceClient = new GeofenceClient(getApplicationContext());
	}
	
	/**
	 * 实现实位回调监听
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			//Receive Location 
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation){
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
				sb.append("\ndirection : ");
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				sb.append(location.getDirection());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				//运营商信息
				sb.append("\noperationers : ");
				sb.append(location.getOperators());
			}
			Log.i("BaiduLocationApiDem", sb.toString());
		}


	}
}
