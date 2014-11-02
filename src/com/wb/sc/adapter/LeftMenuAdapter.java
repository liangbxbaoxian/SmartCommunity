package com.wb.sc.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wb.sc.R;
import com.wb.sc.bean.Category;
import com.wb.sc.bean.Item;

public class LeftMenuAdapter extends BaseAdapter {

    public interface MenuListener {

        void onActiveViewChanged(View v);
    }

    private Context mContext;

    private List<Object> mItems;

    private MenuListener mListener;

    private int mActivePosition = -1;
    
    LayoutInflater layoutinflator; // 布局解析器

    public LeftMenuAdapter(Context context, List<Object> items) {
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
			
			  if (item instanceof Category) {
				  convertView = LayoutInflater.from(mContext).inflate(R.layout.menu_row_category, parent, false);
				  viewholder.txt_name = (TextView) convertView.findViewById(R.id.txt_name);
			  } else {
				  	convertView = LayoutInflater.from(mContext).inflate(R.layout.menu_row_item, parent, false);
					viewholder.txt_phone = (TextView) convertView.findViewById(R.id.txt_phone);
					viewholder.txt_name = (TextView) convertView.findViewById(R.id.txt_name);
			  }
			  convertView.setTag(viewholder);
			
		} else {
			viewholder = (ViewHolder) convertView.getTag();
		}
		
		 if (item instanceof Category) {
			 Category cate = (Category) item;
			 viewholder.txt_name.setText(cate.name);
		 } else {
			 Item i = (Item)item;
			 viewholder.txt_phone.setText(i.phone);
			 viewholder.txt_name.setText(i.name);
		 }
        
//        if (item instanceof Category) {
//            if (v == null) {
//                v = LayoutInflater.from(mContext).inflate(R.layout.menu_row_category, parent, false);
//                viewholder = new ViewHolder();
//            }
//
//            ((TextView) v).setText(((Category) item).mTitle);
//
//        } else {
//            if (v == null) {
//                v = LayoutInflater.from(mContext).inflate(R.layout.menu_row_item, parent, false);
//            }
//
//            TextView tv = (TextView) v;
//            tv.setText(((Item) item).mTitle);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                tv.setCompoundDrawablesRelativeWithIntrinsicBounds(((Item) item).mIconRes, 0, 0, 0);
//            } else {
//                tv.setCompoundDrawablesWithIntrinsicBounds(((Item) item).mIconRes, 0, 0, 0);
//            }
//        }

//        v.setTag(R.id.mdActiveViewPosition, position);

        if (position == mActivePosition) {
            mListener.onActiveViewChanged(v);
        }

        return convertView;
    }
    
	class ViewHolder {
		TextView txt_name;
		TextView txt_phone;
	}
	
}
