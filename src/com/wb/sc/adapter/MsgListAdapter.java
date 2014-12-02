package com.wb.sc.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wb.sc.R;
import com.wb.sc.bean.Msg;

public class MsgListAdapter extends BaseAdapter{
	
	private Context mContext;
	private List<Msg.MgItem> mMsgList;
	   
    public MsgListAdapter(Context context, List<Msg.MgItem> list) {
       mContext = context;
       mMsgList = list;
    }
 
    @Override
    public int getCount() {
 
       return mMsgList.size();
    }
 
    @Override
    public Object getItem(int position) {
 
       return mMsgList.get(position);
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
           view = inflater.inflate(R.layout.msg_item_layout, null);
           holder = new ViewHolder();
           holder.nameTv = (TextView) view.findViewById(R.id.name);
           holder.timeTv = (TextView) view.findViewById(R.id.date);
           holder.descTv = (TextView) view.findViewById(R.id.desc);
           view.setTag(holder);
       } else {
           view = convertView;
           holder = (ViewHolder) view.getTag();
       }
       
       Msg.MgItem item = mMsgList.get(position);
       if(!TextUtils.isEmpty(item.msgTitle)) {
    	   holder.nameTv.setText(item.msgTitle);
       } else {
    	   holder.nameTv.setText("");
       }
       
       if(!TextUtils.isEmpty(item.msgCreteTime)) {
    	   holder.timeTv.setText(item.msgCreteTime);
       } else {
    	   holder.timeTv.setText("");
       }
       
       if(!TextUtils.isEmpty(item.msgContent)) {
    	   holder.descTv.setText(item.msgContent);
       } else {
    	   holder.descTv.setText("");
       }
       
       return view;
    }
   
    class ViewHolder {
    	TextView nameTv;
    	TextView timeTv;
    	TextView descTv;
    }
}
