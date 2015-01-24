

package com.wb.sc.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.wb.sc.R;
import com.wb.sc.app.SCApp;
import com.wb.sc.bean.ImagesItem;
import com.wb.sc.bean.SaleHouseList;
import com.wb.sc.config.IntentExtraConfig;
import com.wb.sc.config.NetConfig;
import com.wb.sc.dialog.ConfirmDialog;
import com.wb.sc.mk.img.ImageBrowseActivity;

public class SaleHouseListAdapter extends BaseAdapter implements OnClickListener{
	
	private Activity mActivity;
	private SaleHouseList mSaleList;
	
	private CallPhoneListener phoneListener;
	   
    public SaleHouseListAdapter(Activity activity, SaleHouseList list) {
       mActivity = activity;
       mSaleList = list;
       phoneListener = new CallPhoneListener();
    }
 
    @Override
    public int getCount() {
 
       return mSaleList.datas.size();
    }
 
    @Override
    public Object getItem(int position) {
 
       return mSaleList.datas.get(position);
    }
 
    @Override
    public long getItemId(int position) {
 
       return position;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      
       View view = null;
       ViewHolder holder;
       if (convertView == null) {
           LayoutInflater inflater = LayoutInflater.from(mActivity);
           view = inflater.inflate(R.layout.house_sale_item, null);
           holder = new ViewHolder();
           holder.totalPriceTv = (TextView) view.findViewById(R.id.total_price);
           holder.priceDescTv = (TextView) view.findViewById(R.id.price_desc);
           holder.typeTv = (TextView) view.findViewById(R.id.type);
           holder.areaTv = (TextView) view.findViewById(R.id.area);
           holder.yearTv = (TextView) view.findViewById(R.id.year);
           holder.orientationTv = (TextView) view.findViewById(R.id.orientation);
           holder.floorTv = (TextView) view.findViewById(R.id.floor);
           holder.structureTv = (TextView) view.findViewById(R.id.structure);
           holder.finishTv = (TextView) view.findViewById(R.id.finish);
           holder.categoryTv = (TextView) view.findViewById(R.id.category);
           holder.propertiesTv = (TextView) view.findViewById(R.id.properties);
           holder.timeTv = (TextView) view.findViewById(R.id.time);
           holder.configurationTv = (TextView) view.findViewById(R.id.configuration);
           holder.phoneTv = (TextView) view.findViewById(R.id.phone);
           holder.phoneVg = (ViewGroup) view.findViewById(R.id.phone_layout);
           holder.phoneVg.setOnClickListener(phoneListener);

           holder.imgVg = (LinearLayout) view.findViewById(R.id.imgs);
           holder.img1Iv = (NetworkImageView) view.findViewById(R.id.img1);
           holder.img2Iv = (NetworkImageView) view.findViewById(R.id.img2);
           holder.img3Iv = (NetworkImageView) view.findViewById(R.id.img3);
           holder.img4Iv = (NetworkImageView) view.findViewById(R.id.img4);
           holder.imgIvList = new ArrayList<NetworkImageView>();
           holder.imgIvList.add(holder.img1Iv);
           holder.imgIvList.add(holder.img2Iv);
           holder.imgIvList.add(holder.img3Iv);
           holder.imgIvList.add(holder.img4Iv);
          
           holder.img1Iv.setOnClickListener(this);           
           holder.img2Iv.setOnClickListener(this);           
           holder.img3Iv.setOnClickListener(this);           
           holder.img4Iv.setOnClickListener(this);
           holder.imgLineV = view.findViewById(R.id.img_line);
           
           view.setTag(holder);
       } else {
           view = convertView;
           holder = (ViewHolder) view.getTag();
       }
       
       holder.img1Iv.setTag(position+"&1");
       holder.img2Iv.setTag(position+"&2");
       holder.img3Iv.setTag(position+"&3");
       holder.img4Iv.setTag(position+"&4");
       
       holder.phoneVg.setTag(position+"");
       
       com.wb.sc.bean.SaleHouseList.Item item = mSaleList.datas.get(position);
       holder.totalPriceTv.setText(item.totalPrice);
       holder.priceDescTv.setText(item.priceDesc);
       holder.typeTv.setText(item.type);
       holder.areaTv.setText(item.area + "㎡");
       holder.yearTv.setText(item.year);
       holder.orientationTv.setText(item.orientation);
       holder.floorTv.setText(item.floor);
       holder.structureTv.setText(item.structure);
       holder.finishTv.setText(item.finish);
       if(item.equals("1")) {
    	   holder.propertiesTv.setText("有产权");
       } else if(item.equals("0")) {
    	   holder.propertiesTv.setText("无产权");
       } else {
    	   holder.propertiesTv.setText(item.properties);
       }
       holder.timeTv.setText(item.time);
       holder.configurationTv.setText(item.configuration);
       holder.phoneTv.setText(item.phone);

//       item.imgList.clear();
//       item.imgList.add("http://img3.cache.netease.com/cnews/2014/11/9/201411091912085e32c.jpg");
//       item.imgList.add("http://img5.cache.netease.com/photo/0001/2014-11-02/AA2G0LS100AN0001.jpg");
//       item.imgList.add("http://img5.cache.netease.com/photo/0001/2014-11-02/AA2G0LS100AN0001.jpg");
//       item.imgList.add("http://img5.cache.netease.com/photo/0001/2014-11-02/AA2G0LS100AN0001.jpg");
//       item.imgList.add("http://img5.cache.netease.com/photo/0001/2014-11-02/AA2G0LS100AN0001.jpg");
//       item.imgList.add("http://img5.cache.netease.com/photo/0001/2014-11-02/AA2G0LS100AN0001.jpg");
//       holder.imgVg.removeAllViews();
       if(item.imgList.size() > 0) {
//    	   PostImgAdapter adapter = new PostImgAdapter(mContext, item.imgList);
//    	   holder.imgLv.setAdapter(adapter);    	   
//    	   holder.imgLv.setVisibility(View.GONE);
    	   for(int i=0; i<item.imgList.size() && i < 4; i++) {    		   
    		   String imgUrl = item.imgList.get(i);
    		   	if(!TextUtils.isEmpty(imgUrl)) {
//    		   		NetworkImageView itemIv = new NetworkImageView(mActivity);    
    		   		NetworkImageView itemIv = holder.imgIvList.get(i);
//    		   		String smallImgUrl = ImgUrlUtil.getSmallUrl(imgUrl);
    		   		itemIv.setImageUrl(NetConfig.getPictureUrl(imgUrl), SCApp.getInstance().getImageLoader());
//    		   		holder.imgVg.addView(itemIv);
//    		   		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)itemIv.getLayoutParams();
//    		   		lp.width = pItemWidth;
//    		   		lp.height = pItemWidth; 
//    		   		lp.rightMargin = rightMargin;
    		   	}
    	   }
       } else {
//    	   holder.imgLv.setVisibility(View.GONE);
    	   holder.imgLineV.setVisibility(View.GONE);
    	   holder.imgVg.setVisibility(View.GONE);
       }
       
       return view;
    }
   
    class ViewHolder {
    	TextView totalPriceTv;
    	TextView priceDescTv;
    	TextView typeTv;
    	TextView areaTv;
    	TextView yearTv;
    	TextView orientationTv;
    	TextView floorTv;
    	TextView structureTv;
    	TextView finishTv;
    	TextView categoryTv;
    	TextView propertiesTv;
    	TextView timeTv;
    	TextView configurationTv;
    	TextView phoneTv;
    	LinearLayout imgVg;
    	NetworkImageView img1Iv;
    	NetworkImageView img2Iv;
    	NetworkImageView img3Iv;
    	NetworkImageView img4Iv;
    	List<NetworkImageView> imgIvList;
    	View imgLineV;
    	ViewGroup phoneVg;
    }
    
	@Override
	public void onClick(View v) {
		int position = Integer.valueOf(v.getTag().toString().split("&")[0]);
		int index = Integer.valueOf(v.getTag().toString().split("&")[1]) - 1;
		com.wb.sc.bean.SaleHouseList.Item item = mSaleList.datas.get(position);
		ArrayList<ImagesItem> imgs = new ArrayList<ImagesItem>();
		ImagesItem imgItem = new ImagesItem();
		imgs.add(imgItem);
		imgItem.name = "";
		imgItem.imageNum = item.imgList.size();
		imgItem.images = new String[imgItem.imageNum];
		int i=0;
		for(String imgUrl : item.imgList) {
			imgItem.images[i] = imgUrl;
			i++;
		}		
		
		Intent intent = new Intent(mActivity, ImageBrowseActivity.class);
		intent.putParcelableArrayListExtra(IntentExtraConfig.IMAGE_BROWSER_DATA, imgs);
		intent.putExtra(IntentExtraConfig.IMAGE_BROWSER_DIS_TAB, false);
		intent.putExtra(IntentExtraConfig.IMAGE_BROWSER_POS, index);
		mActivity.startActivity(intent);
	}
	
	class CallPhoneListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			int position = Integer.valueOf(v.getTag().toString());
			final com.wb.sc.bean.SaleHouseList.Item item = mSaleList.datas.get(position);
			new ConfirmDialog().getDialog(mActivity, "呼叫", item.phone, 
					new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+item.phone));  
					mActivity.startActivity(intent);  
				}
			}).show();
		}
		
	}
}
