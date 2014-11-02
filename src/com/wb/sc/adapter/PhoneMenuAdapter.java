package com.wb.sc.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wb.sc.R;
import com.wb.sc.bean.Item;

public class PhoneMenuAdapter extends BaseAdapter {

    public interface MenuListener {

        void onActiveViewChanged(View v);
    }

    private Context mContext;

    private List<Object> mItems;

    private MenuListener mListener;

    private int mActivePosition = -1;
    
    LayoutInflater layoutinflator; // 布局解析器

    public PhoneMenuAdapter(Context context, List<Object> items) {
        mContext = context;
        layoutinflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mItems = items;
    }

    public void setListener(MenuListener listener) {
        mListener = listener;
    }

    public void setActivePosition(int activePosition) {
        mActivePosition = activePosition;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position) instanceof Item ? 0 : 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public boolean isEnabled(int position) {
        return getItem(position) instanceof Item;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        Object item = getItem(position);
        ViewHolder viewholder = null;
        
		if (convertView == null) {
			viewholder = new ViewHolder();			
			convertView = LayoutInflater.from(mContext).inflate(R.layout.phone_item_layout, parent, false);
			viewholder.type = (TextView) convertView.findViewById(R.id.type);
			viewholder.phone = (TextView) convertView.findViewById(R.id.phone);
			convertView.setTag(viewholder);	
		} else {
			viewholder = (ViewHolder) convertView.getTag();
		}
		
		Item i = (Item)item;
		viewholder.type.setText(i.name);
		viewholder.phone.setText(i.phone);
		
        if (position == mActivePosition) {
            mListener.onActiveViewChanged(v);
        }

        return convertView;
    }
    
	class ViewHolder {
		TextView type;
		TextView phone;
	}
	
}
