package com.wb.sc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.common.widget.hzlib.HorizontalListView;
import com.wb.sc.R;
import com.wb.sc.app.SCApp;
import com.wb.sc.bean.PostList;
import com.wb.sc.bean.PostList.Item;
import com.wb.sc.config.NetConfig;

public class PostListAdapter extends BaseAdapter{
	
	private Context mContext;
	private PostList mPostList;
	   
    public PostListAdapter(Context context, PostList list) {
       mContext = context;
       mPostList = list;
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
           LayoutInflater inflater = LayoutInflater.from(mContext);
           view = inflater.inflate(R.layout.post_list_item, null);
           holder = new ViewHolder();
           holder.avatarIv = (NetworkImageView) view.findViewById(R.id.avatar);
           holder.nameTv = (TextView) view.findViewById(R.id.postMaster);
           holder.titleTv = (TextView) view.findViewById(R.id.postTitle);
           holder.timeTv = (TextView) view.findViewById(R.id.postTime);
           holder.descTv = (TextView) view.findViewById(R.id.postName);
           holder.msgNumTv = (TextView) view.findViewById(R.id.msg_num);
           holder.favNumTv = (TextView) view.findViewById(R.id.favourite_num);
           holder.imgLv = (HorizontalListView) view.findViewById(R.id.list);
           view.setTag(holder);
       } else {
           view = convertView;
           holder = (ViewHolder) view.getTag();
       }
       
       Item item = mPostList.datas.get(position);
       holder.avatarIv.setImageUrl(NetConfig.getPictureUrl(item.sourceAvatarUrl), 
    		   SCApp.getInstance().getImageLoader());
       holder.nameTv.setText(item.sourceName);
       holder.titleTv.setText(item.title);
       holder.timeTv.setText(item.time);
       holder.descTv.setText(item.content);
       holder.msgNumTv.setText(item.commentNum);
       holder.favNumTv.setText(item.favNum);
       if(item.imgList.size() > 0) {
//    	   PostImgAdapter adapter = new PostImgAdapter(mContext, item.imgList);
//    	   holder.imgLv.setAdapter(adapter);
    	   holder.imgLv.setVisibility(View.GONE);
       } else {
    	   holder.imgLv.setVisibility(View.GONE);
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
    	HorizontalListView imgLv;
    }
}
