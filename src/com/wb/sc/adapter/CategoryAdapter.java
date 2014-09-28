package com.wb.sc.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.wb.sc.R;
import com.wb.sc.bean.CategoryTable;
import com.wb.sc.mk.main.SentHomeActivity;

public class CategoryAdapter extends BaseAdapter {

	private List<CategoryTable> list;

	LayoutInflater layoutinflator; // 布局解析器
	
	private Context context;
	
	private ItemClickListener mListener;
	
	public CategoryAdapter(Context context, List<CategoryTable> list) {
		this.list = list;
		this.context = context;
		layoutinflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	public long getItemId(int arg0) {
		return arg0;
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		CategoryTable categoryTable = list.get(position);
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
		
		viewholder.txt_name.setText(categoryTable.getCategoryname());
//		viewholder.img_async.setDefaultImageDrawable(viewholder.txt_name.getContext().getResources().getDrawable(R.drawable.icon_def));
		viewholder.img_async.setDefaultImageResId(categoryTable.getId());
//		viewholder.img_async.setPath(categoryTable.getCategoryicon());
		viewholder.img_async.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
//				float[] BT_NOT_SELECTED = new float[] { 1, 0, 0, 0, 0, 0,  
//						1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0 };  
//				AsyncImageView  img_async = (AsyncImageView) v.findViewById(R.id.img_async);
//				img_async.setColorFilter( new ColorMatrixColorFilter(BT_NOT_SELECTED));
				jumpToMerchantsActivity(list, position);
				Animation animation = AnimationUtils.loadAnimation(context, R.anim.img_scale_in);
				v.startAnimation(animation);
				return true;
			}
		});
		viewholder.img_async.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Animation animation = AnimationUtils.loadAnimation(context, R.anim.img_scale_in);
				v.startAnimation(animation);
				jumpToMerchantsActivity(list, position);
				if(mListener != null) {
					int position = Integer.valueOf(v.getTag().toString());
					mListener.onItemClick(position);
				}
			}
		});
		viewholder.img_async.setOnTouchListener(new OnTouchListener() {
			
//		public final float[] BT_SELECTED = new float[] { 1, 0, 0, 0, -50, 0, 1,  
//					0, 0, -50, 0, 0, 1, 0, -50, 0, 0, 0, 1, 0 }; 
//		
//		public final float[] BT_NOT_SELECTED = new float[] { 1, 0, 0, 0, 0, 0,  
//					1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0 };  

			@Override
			public boolean onTouch(View v, MotionEvent event) {
//				Animation animation = AnimationUtils.loadAnimation(context, R.anim.img_scale_in);
//				v.startAnimation(animation);
//				jumpToMerchantsActivity(list, position);
//				AsyncImageView  img_async = (AsyncImageView) v.findViewById(R.id.img_async);
//				if (event.getAction() == MotionEvent.ACTION_DOWN) {
//					if (img_async instanceof ImageView){
//						img_async.setColorFilter( new ColorMatrixColorFilter(BT_SELECTED) );
//						img_async.setSoundEffectsEnabled(true);
//						return false;
//					}
//				} else if (event.getAction() == MotionEvent.ACTION_UP) {
//					if(img_async instanceof ImageView){  
//						img_async.setColorFilter( new ColorMatrixColorFilter(BT_NOT_SELECTED));
//						img_async.playSoundEffect(SoundEffectConstants.CLICK);
//						jumpToMerchantsActivity(list, position);
//						return true;
//					}
//				} else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
//					if(img_async instanceof ImageView){  
//						img_async.setColorFilter( new ColorMatrixColorFilter(BT_NOT_SELECTED));
//						return true;
//					}
//				}

				return false;
			}
		});
		return view;
	}
	
	private void jumpToMerchantsActivity(List<CategoryTable> categoryTableList, int position) {
		switch (position) {
		case 0:
			Intent intent = new Intent(context, SentHomeActivity.class);
			context.startActivity(intent);
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
