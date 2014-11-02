package com.wb.sc.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wb.sc.R;
import com.wb.sc.bean.Community.CommunityItem;
import com.wb.sc.bean.DictionaryItem;

public class DictionaryAdapter extends BaseAdapter{  //法律法规
	
	private Context mContext;
	private List<?> mMsgList;
	private boolean isShowArrow = true;
	   
    public boolean isShowArrow() {
		return isShowArrow;
	}

	public void setShowArrow(boolean isShowArrow) {
		this.isShowArrow = isShowArrow;
	}

	public DictionaryAdapter(Context context, List<?> list) {
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
      
    	String name = "";
    	if (mMsgList.get(position) instanceof DictionaryItem) {
    		DictionaryItem item = (DictionaryItem) mMsgList.get(position);
    		name = item.dictionaryName;
    	} else {
    		CommunityItem item = (CommunityItem) mMsgList.get(position);
    		name = item.communityName;
    	}
    	
       View view = null;
       ViewHolder holder;
       if (convertView == null) {
           LayoutInflater inflater = LayoutInflater.from(mContext);
           view = inflater.inflate(R.layout.msg_adapter_layout, null);
           holder = new ViewHolder();
           holder.nameTv = (TextView) view.findViewById(R.id.name);
           holder.timeTv = (TextView) view.findViewById(R.id.date);
           holder.descTv = (TextView) view.findViewById(R.id.desc);
           view.setTag(holder);
       } else {
           view = convertView;
           holder = (ViewHolder) view.getTag();
       }
       
       if (!isShowArrow) {
    	   view.findViewById(R.id.show_arrow).setVisibility(View.INVISIBLE);;
       }
       
       holder.nameTv.setText(name);
       
       return view;
    }
   
    class ViewHolder {
    	TextView nameTv;
    	TextView timeTv;
    	TextView descTv;
    }
}
