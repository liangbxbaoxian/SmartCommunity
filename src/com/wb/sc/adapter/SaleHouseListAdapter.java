

package com.wb.sc.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.wb.sc.R;
import com.wb.sc.app.SCApp;
import com.wb.sc.bean.SaleHouseList;
import com.wb.sc.config.NetConfig;
import com.wb.sc.util.ImgUrlUtil;

public class SaleHouseListAdapter extends BaseAdapter{
	
	private Activity mActivity;
	private SaleHouseList mSaleList;
	   
    public SaleHouseListAdapter(Activity activity, SaleHouseList list) {
       mActivity = activity;
       mSaleList = list;
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
           
           view.setTag(holder);
       } else {
           view = convertView;
           holder = (ViewHolder) view.getTag();
       }
       
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
       holder.propertiesTv.setText(item.properties);
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
//    	HorizontalListView imgLv;
    }
}
