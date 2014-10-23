package com.wb.sc.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wb.sc.R;

public class BillListAdapter extends BaseAdapter{
	
	private Context mContext;
	private List<Map<String, String>> billList;
	   
    public BillListAdapter(Context context, List<Map<String, String>> list) {
       mContext = context;
       billList = list;
    }
 
    @Override
    public int getCount() {
 
       return billList.size();
    }
 
    @Override
    public Object getItem(int position) {
 
       return billList.get(position);
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
           view = inflater.inflate(R.layout.bill_item_layout, null);
           holder = new ViewHolder();
           holder.nameTv = (TextView) view.findViewById(R.id.name);
           holder.valueTv = (TextView) view.findViewById(R.id.value);
           view.setTag(holder);
       } else {
           view = convertView;
           holder = (ViewHolder) view.getTag();
       }
       
       Map<String, String> map = billList.get(position);
       holder.nameTv.setText(map.get("name"));
       holder.valueTv.setText(map.get("value"));
       
       return view;
    }
   
    class ViewHolder {
    	TextView nameTv;
    	TextView valueTv;
    }
}
