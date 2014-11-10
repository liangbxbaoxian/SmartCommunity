package com.wb.sc.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.common.widget.ToastHelper;
import com.wb.sc.R;
import com.wb.sc.bean.Menu;
import com.wb.sc.dialog.ToastLoginDialog;

public class MenuAdapter extends BaseAdapter {

	private List<Menu> list;

	LayoutInflater layoutinflator; // 布局解析器
	
	private Context context;
	
	private ItemClickListener mListener;
	
	public MenuAdapter(Context context, List<Menu> list) {
		this.list = list;
		this.context = context;
		layoutinflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		Menu menu = list.get(position);
		ViewHolder viewholder = null;
		if (view == null) {
			viewholder = new ViewHolder();
			view = layoutinflator.inflate(R.layout.gridview_item, null);
			viewholder.img_async = (NetworkImageView) view.findViewById(R.id.img_async);
			viewholder.img_async.setTag(position+"");
			viewholder.txt_name = (TextView) view.findViewById(R.id.txt_name);
			view.setTag(viewholder);
		} else {
			viewholder = (ViewHolder) view.getTag();
		}
		
		viewholder.txt_name.setText(menu.name);
		viewholder.img_async.setDefaultImageResId(menu.resId);
		viewholder.img_async.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				jumpToActivity(list, position);
				Animation animation = AnimationUtils.loadAnimation(context, R.anim.img_scale_in);
				v.startAnimation(animation);
				return true;
			}
		});
		viewholder.img_async.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Animation animation = AnimationUtils.loadAnimation(context, R.anim.img_scale_in);
				v.startAnimation(animation);
				jumpToActivity(list, position);
				if(mListener != null) {
					int position = Integer.valueOf(v.getTag().toString());
					mListener.onItemClick(position);
				}
			}
		});
		
		return view;
	}
	
	private void jumpToActivity(List<Menu> list, int position) {
		
			switch (position) {
			
			case 0:
			case 1:
			case 2:{
				if (position == 2) {
					if(ToastLoginDialog.checkLogin(context)) {
						Intent intent = new Intent(context, list.get(position).menuClass);
						context.startActivity(intent);
					}
				} else {
					Intent intent = new Intent(context, list.get(position).menuClass);
					context.startActivity(intent);
				}
	
			}break;
			
			case 3:
				ToastHelper.showToastInBottom(context, "我们还在奔向你的途中，请耐心等待，么么哒~");
				break;
				
			case 4:
				if(ToastLoginDialog.checkLogin(context)) {
					Intent intent = new Intent(context, list.get(position).menuClass);
					context.startActivity(intent);
				}
				break;
			default:
				break;
		}
	}

	class ViewHolder {
		TextView txt_name;
		ImageButton imagebuttom;
		NetworkImageView img_async;
	}
	
	public interface ItemClickListener {
		public void onItemClick(int position);
	}
	
	public void setListener(ItemClickListener listener) {
		this.mListener = listener;
	}
}
