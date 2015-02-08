package com.wb.sc.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.NetworkImageView.NetworkImageListener;
import com.common.media.BitmapHelper;
import com.common.widget.ToastHelper;
import com.wb.sc.R;
import com.wb.sc.app.SCApp;
import com.wb.sc.bean.Favour;
import com.wb.sc.bean.PostList;
import com.wb.sc.bean.PostList.Item;
import com.wb.sc.config.NetConfig;
import com.wb.sc.config.RespCode;
import com.wb.sc.mk.post.PostDetailActivity;
import com.wb.sc.task.FavourRequest;
import com.wb.sc.util.ImgUrlUtil;
import com.wb.sc.util.ParamsUtil;

public class PostListAdapter extends BaseAdapter implements NetworkImageListener, OnClickListener{
	
	private Activity mActivity;
	private PostList mPostList;
	private int pItemWidth = 75;
	private int rightMargin = 4;
	
	//点赞
	private FavListener favListener;
	   
    public PostListAdapter(Activity activity, PostList list) {
       mActivity = activity;
       mPostList = list;
       
       DisplayMetrics dm = new DisplayMetrics();
       activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
       pItemWidth = (int) (pItemWidth * dm.density);
       rightMargin = (int) (rightMargin * dm.density);
    }
 
    @Override
    public int getCount() {
 
       return mPostList.datas.size();
    }
 
    @Override
    public Object getItem(int position) {
 
       return mPostList.datas.get(position);
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
           view = inflater.inflate(R.layout.post_list_item, null);
           holder = new ViewHolder();
           holder.avatarIv = (NetworkImageView) view.findViewById(R.id.avatar);
           holder.nameTv = (TextView) view.findViewById(R.id.postMaster);
           holder.titleTv = (TextView) view.findViewById(R.id.postTitle);
           holder.timeTv = (TextView) view.findViewById(R.id.postTime);
           holder.descTv = (TextView) view.findViewById(R.id.postName);
           holder.msgNumTv = (TextView) view.findViewById(R.id.msg_num);
           holder.favNumTv = (TextView) view.findViewById(R.id.favourite_num);           
           holder.favVg = (ViewGroup) view.findViewById(R.id.fav_layout);           

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
           
//         holder.imgLv = (HorizontalListView) view.findViewById(R.id.list);
//         holder.imgLv.setVisibility(View.GONE);
           view.setTag(holder);
       } else {
           view = convertView;
           holder = (ViewHolder) view.getTag();
       }
       
       holder.favVg.setOnClickListener(this);
       holder.favVg.setTag(position + "");
       
       Item item = mPostList.datas.get(position);
       holder.avatarIv.setImageUrl(NetConfig.getPictureUrl(item.sourceAvatarUrl), 
    		   SCApp.getInstance().getImageLoader());
       holder.nameTv.setText(item.sourceName);
       holder.titleTv.setText(item.title);
       holder.timeTv.setText(item.time);
       holder.descTv.setText(item.content);
       holder.msgNumTv.setText(item.commentNum);
       holder.favNumTv.setText(item.favNum);
//       item.imgList.clear();
//       item.imgList.add("http://img5.cache.netease.com/photo/0001/2014-11-02/AA2G0LS100AN0001.jpg");
//       item.imgList.add("http://img5.cache.netease.com/photo/0001/2014-11-02/AA2G0LS100AN0001.jpg");
//       item.imgList.add("http://img5.cache.netease.com/photo/0001/2014-11-02/AA2G0LS100AN0001.jpg");
//       item.imgList.add("http://img5.cache.netease.com/photo/0001/2014-11-02/AA2G0LS100AN0001.jpg");
//       item.imgList.add("http://img5.cache.netease.com/photo/0001/2014-11-02/AA2G0LS100AN0001.jpg");
//       item.imgList.add("http://img5.cache.netease.com/photo/0001/2014-11-02/AA2G0LS100AN0001.jpg");
//       holder.imgVg.removeAllViews();
       
       for(NetworkImageView iv : holder.imgIvList) {
    	   iv.setVisibility(View.GONE);
       }
       
       if(item.imgList.size() > 0) {
//    	   PostImgAdapter adapter = new PostImgAdapter(mContext, item.imgList);
//    	   holder.imgLv.setAdapter(adapter);    	   
//    	   holder.imgLv.setVisibility(View.GONE);
    	   holder.imgVg.setVisibility(View.VISIBLE);
    	   for(int i=0; i<item.imgList.size() && i < 4; i++) {    		   
    		   String imgUrl = item.imgList.get(i);
    		   	if(!TextUtils.isEmpty(imgUrl)) {
//    		   		NetworkImageView itemIv = new NetworkImageView(mActivity);    
    		   		holder.imgIvList.get(i).setVisibility(View.VISIBLE);
    		   		NetworkImageView itemIv = holder.imgIvList.get(i);
    		   		String smallImgUrl = ImgUrlUtil.getSmallUrl(imgUrl);
    		   		itemIv.setImageUrl(NetConfig.getPictureUrl(smallImgUrl), SCApp.getInstance().getImageLoader());
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
    	NetworkImageView avatarIv;
    	TextView nameTv;
    	TextView titleTv;
    	TextView timeTv;
    	TextView descTv;
    	TextView msgNumTv;
    	TextView favNumTv;
    	LinearLayout imgVg;
    	NetworkImageView img1Iv;
    	NetworkImageView img2Iv;
    	NetworkImageView img3Iv;
    	NetworkImageView img4Iv;
    	List<NetworkImageView> imgIvList;
    	ViewGroup favVg;
//    	HorizontalListView imgLv;
    }

	@Override
	public void onGetBitmapListener(ImageView imageView, Bitmap bitmap) {
		Bitmap roundBmp = BitmapHelper.toRoundCorner(bitmap, bitmap.getHeight()/2);
		imageView.setImageBitmap(roundBmp);	
	}

	@Override
	public void onClick(View v) {
		try {
			int position = Integer.valueOf(v.getTag().toString());
			if(favListener != null) {
				favListener.onFav(position);
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public interface FavListener {
		public void onFav(int position);
	}
	
	public void setFavListener(FavListener listener) {
		favListener = listener;
	}
}
