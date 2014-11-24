package com.wb.sc.mk.main;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;

import com.wb.sc.R;
import com.wb.sc.activity.base.BaseExtraLayoutFragment;
import com.wb.sc.adapter.FindAdapter;
import com.wb.sc.bean.CategoryTable;

public class FindFragment extends BaseExtraLayoutFragment implements OnClickListener {
	
	private List<CategoryTable> categoryTableList = new ArrayList<CategoryTable>();
	private FindAdapter yipayGriAdapter;
		
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return setContentView(inflater, R.layout.fragment_find);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initData();
		initView(view);
	}
	
	private void initData() {
		categoryTableList.clear();
		int resId [] = {R.drawable.huishenghuo, R.drawable.driver_selector, R.drawable.tuan_selector,  R.drawable.coupon_selector/*,
		R.drawable.trade_selector, R.drawable.together_selector, R.drawable.car_selector*/};

        String categoryname [] = {"汇.商城", "一公里", "天天团", "捡便宜", 
		"惠商城", "一起玩", "来拼车"};
		
		for (int i = 0; i < resId.length; i++) {
			CategoryTable category = new CategoryTable();
			category.setId(resId[i]);
			category.setCategoryname(categoryname[i]);
			categoryTableList.add(category);
		}
	}
	
	private void initView(View view) {
		yipayGriAdapter = new FindAdapter(getActivity(), categoryTableList);
		final GridView yipay_server = (GridView) view.findViewById(R.id.yipay_server);
		yipay_server.setSelector(R.color.transparent);
		yipay_server.setAdapter(yipayGriAdapter);
	}
}
