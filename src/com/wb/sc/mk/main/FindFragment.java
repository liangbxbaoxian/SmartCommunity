package com.wb.sc.mk.main;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.viewpagerindicator.CirclePageIndicator;
import com.wb.sc.R;
import com.wb.sc.activity.base.BaseExtraLayoutFragment;
import com.wb.sc.adapter.AdvAdapter;

public class FindFragment extends BaseExtraLayoutFragment {
	
	private ViewPager advVp;
	private CirclePageIndicator advIndicator;
	private AdvAdapter advAdapter;
	
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
		return setContentView(inflater, R.layout.fragment_find);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		initView(view);
	}

	private void initView(View view) {
		advVp = (ViewPager) view.findViewById(R.id.adv_pager);
		advIndicator = (CirclePageIndicator) view.findViewById(R.id.adv_indicator);
		advAdapter = new AdvAdapter(getActivity());
		advVp.setAdapter(advAdapter);
		advIndicator.setViewPager(advVp);
	}
}
