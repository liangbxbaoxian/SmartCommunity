package com.wb.sc.adapter;

import com.wb.sc.R;
import com.wb.sc.bean.CommentList;
import com.wb.sc.bean.PostList;

import android.content.Context;
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
           holder.avatarIv = (ImageView) view.findViewById(R.id.avatar);
           holder.nameTv = (TextView) view.findViewById(R.id.name);
           holder.timeTv = (TextView) view.findViewById(R.id.time);
           holder.commentTv = (TextView) view.findViewById(R.id.comment);
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
    	TextView timeTv;
    	TextView commentTv;
    }
}
