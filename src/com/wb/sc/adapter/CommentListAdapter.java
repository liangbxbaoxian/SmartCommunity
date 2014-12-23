package com.wb.sc.adapter;

import java.util.Date;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.NetworkImageView.NetworkImageListener;
import com.common.date.FormatDateTime;
import com.common.media.BitmapHelper;
import com.wb.sc.R;
import com.wb.sc.app.SCApp;
import com.wb.sc.bean.CommentList;
import com.wb.sc.bean.CommentList.Item;
import com.wb.sc.config.NetConfig;

public class CommentListAdapter extends BaseAdapter implements NetworkImageListener{
	
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
       Date timeDate = FormatDateTime.string2Date(item.time, FormatDateTime.DATETIME_YMDHMS_STR);
       String time = FormatDateTime.date2String(timeDate, FormatDateTime.DATETIME_YMDHMS);
       holder.timeTv.setText(time);
       if(TextUtils.isEmpty(item.parentSourceId)) {
    	   holder.commentTv.setText(item.content);
       } else {
    	   ForegroundColorSpan blueSpan = new ForegroundColorSpan(Color.BLUE);
    	   String content = "回复@"+item.parentSourceName + ":" + item.content;
    	   SpannableStringBuilder builder = new SpannableStringBuilder(content);
    	   builder.setSpan(blueSpan, 2, item.parentSourceName.length()+2+2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    	   holder.commentTv.setText(builder);
       }
       
       return view;
    }
   
    class ViewHolder {
    	NetworkImageView avatarIv;
    	TextView nameTv;
    	TextView timeTv;
    	TextView commentTv;
    }

	@Override
	public void onGetBitmapListener(ImageView imageView, Bitmap bitmap) {
		Bitmap roundBmp = BitmapHelper.toRoundCorner(bitmap, bitmap.getHeight()/2);
		imageView.setImageBitmap(roundBmp);	
	}
}
