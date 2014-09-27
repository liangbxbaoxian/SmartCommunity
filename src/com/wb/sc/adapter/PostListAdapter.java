package com.wb.sc.adapter;

import com.wb.sc.R;
import com.wb.sc.bean.PostList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
           holder.avatarIv = (ImageView) view.findViewById(R.id.avatar);
           holder.nameTv = (TextView) view.findViewById(R.id.name);
           holder.remarkTv = (TextView) view.findViewById(R.id.remark);
           holder.timeTv = (TextView) view.findViewById(R.id.time);
           holder.descTv = (TextView) view.findViewById(R.id.desc);
           holder.msgNumTv = (TextView) view.findViewById(R.id.msg_num);
           holder.favouriteNumTv = (TextView) view.findViewById(R.id.favour_num);
           view.setTag(holder);
       } else {
           view = convertView;
           holder = (ViewHolder) view.getTag();
       }
       
       return view;
    }
   
    class ViewHolder {
    	ImageView avatarIv;
    	TextView nameTv;
    	TextView remarkTv;
    	TextView timeTv;
    	TextView descTv;
    	TextView msgNumTv;
    	TextView favouriteNumTv;
    }
}
