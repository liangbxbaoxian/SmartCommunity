package com.wb.sc.mk.main;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.viewpagerindicator.CirclePageIndicator;
import com.wb.sc.R;
import com.wb.sc.activity.base.BaseExtraLayoutFragment;
import com.wb.sc.adapter.AdvAdapter;
import com.wb.sc.adapter.PostsTypeAdapter;

public class HomeFragment extends BaseExtraLayoutFragment {
	
	//标题栏相关
	private ImageView phoneIv;
	private ImageView msgIv;
	private TextView nameIv;
	
	//广告
	private ViewPager advVp;
	private CirclePageIndicator advIndicator;
	private AdvAdapter advAdapter;
	
	//帖子栏
	private ListView postsTypeLv;
	private PostsTypeAdapter typeAdapter;
	
	
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
		return setContentView(inflater, R.layout.fragment_home);
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
		
		postsTypeLv = (ListView) view.findViewById(R.id.list);
		typeAdapter = new PostsTypeAdapter(getActivity());
		postsTypeLv.setAdapter(typeAdapter);
	}
}
