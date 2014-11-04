package com.wb.sc.adapter;

import com.android.volley.toolbox.NetworkImageView;
import com.wb.sc.R;
import com.wb.sc.app.SCApp;
import com.wb.sc.bean.CommentList;
import com.wb.sc.bean.CommentList.Item;
import com.wb.sc.bean.PostList;
import com.wb.sc.config.NetConfig;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CommentListAdapter extends BaseAdapter{
	
	private Context mContext;
	private CommentList mCommentList;
	   
    public CommentListAdapter(Context context, CommentList list) {
       mContext = context;
       mCommentList = list;
    }
 
    @Override
    public int getCount() {
 
       return mCommentList.datas.size();
    }
 
    @Override
    public Object getItem(int position) {
 
       return mCommentList.datas.get(position);
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
           view = inflater.inflate(R.layout.comment_list_item_layout, null);
           holder = new ViewHolder();
           holder.avatarIv = (NetworkImageView) view.findViewById(R.id.avatar);
           holder.nameTv = (TextView) view.findViewById(R.id.name);
           holder.timeTv = (TextView) view.findViewById(R.id.time);
           holder.commentTv = (TextView) view.findViewById(R.id.comment);
           view.setTag(holder);
       } else {
           view = convertView;
           holder = (ViewHolder) view.getTag();
       }
       
       Item item = mCommentList.datas.get(position);
       if(!TextUtils.isEmpty(item.sourceAvatar)) {
    	   holder.avatarIv.setImageUrl(NetConfig.getPictureUrl(item.sourceAvatar), SCApp.getInstance().getImageLoader());
       }
       holder.nameTv.setText(item.sourceName);
       holder.timeTv.setText(item.time);
       holder.commentTv.setText(item.content);
       
       return view;
    }
   
    class ViewHolder {
    	NetworkImageView avatarIv;
    	TextView nameTv;
    	TextView timeTv;
    	TextView commentTv;
    }
}
