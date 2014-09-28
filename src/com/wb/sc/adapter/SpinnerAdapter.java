package com.wb.sc.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wb.sc.R;

public class SpinnerAdapter extends ArrayAdapter<String> {
	
	private Context context;
	private String[] objects;

	public SpinnerAdapter(Context context, int textViewResourceId, String[] objects) {
		  super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.objects = objects;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}

	public View getCustomView(int position, View convertView, ViewGroup parent) {

		Activity activity = (Activity) context;
		LayoutInflater inflater = activity.getLayoutInflater();
		View row = inflater.inflate(R.layout.custom_spinner, parent, false);
		TextView label = (TextView) row.findViewById(R.id.textView1);
		label.setText(objects[position]);


		return row;
	}

}