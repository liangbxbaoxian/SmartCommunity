package com.wb.sc.mk.main;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.viewpagerindicator.CirclePageIndicator;
import com.wb.sc.R;
import com.wb.sc.activity.base.BaseExtraLayoutFragment;
import com.wb.sc.adapter.AdvAdapter;
import com.wb.sc.adapter.CategoryAdapter;
import com.wb.sc.bean.CategoryTable;

public class ButlerFragment extends BaseExtraLayoutFragment {
	// add test for linyongzhen
	private ViewPager advVp;
	private CirclePageIndicator advIndicator;
	private AdvAdapter advAdapter;
	
	private List<CategoryTable> categoryTableList = new ArrayList<CategoryTable>();
	private CategoryAdapter yipayGriAdapter;
	
	//标题栏相关
	private ImageView leftIv;
	private ImageView rightIv;
	private TextView nameIv;
	
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
		return setContentView(inflater, R.layout.fragment_butler);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initData();
		initView(view);
	}
	
	private void initData() {
		categoryTableList.clear();
		int resId [] = {R.drawable.complain_selector, R.drawable.repair_selector, R.drawable.praise_selector,
				R.drawable.business_selector, R.drawable.bill_selector};
		
		String categoryname [] = {"物业投诉", "物业报修", "物业表扬",
				"房屋交易", "物业账单"};
		
		for (int i = 0; i < resId.length; i++) {
			CategoryTable category = new CategoryTable();
			category.setId(resId[i]);
			category.setCategoryname(categoryname[i]);
			categoryTableList.add(category);
		}
	}

	private void initView(View view) {
		advVp = (ViewPager) view.findViewById(R.id.adv_pager);
		advIndicator = (CirclePageIndicator) view.findViewById(R.id.adv_indicator);
		advAdapter = new AdvAdapter(getActivity());
		advVp.setAdapter(advAdapter);
		advIndicator.setViewPager(advVp);
		yipayGriAdapter = new CategoryAdapter(getActivity(), categoryTableList);
		final GridView yipay_server = (GridView) view.findViewById(R.id.yipay_server);
		yipay_server.setSelector(R.color.transparent);
		yipay_server.setAdapter(yipayGriAdapter);
	}
}