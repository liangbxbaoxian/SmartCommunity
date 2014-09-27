package com.wb.sc.mk.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.viewpagerindicator.CirclePageIndicator;
import com.wb.sc.R;
import com.wb.sc.activity.base.BaseExtraLayoutFragment;
import com.wb.sc.adapter.AdvAdapter;
import com.wb.sc.adapter.PostTypeAdapter;
import com.wb.sc.mk.post.PostListActivity;

public class HomeFragment extends BaseExtraLayoutFragment implements OnClickListener,
	OnItemClickListener{
	
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
	private PostTypeAdapter typeAdapter;
	
	//快捷入口
	private ImageButton shortcutIn;
	private ImageButton shortcutOut;
	private RelativeLayout shortcutLayout;
	
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
		typeAdapter = new PostTypeAdapter(getActivity());
		postsTypeLv.setAdapter(typeAdapter);
		postsTypeLv.setOnItemClickListener(this);
		
		shortcutIn = (ImageButton) view.findViewById(R.id.shortcut_in);
		shortcutIn.setOnClickListener(this);
		shortcutOut = (ImageButton) view.findViewById(R.id.shortcut_out);
		shortcutOut.setOnClickListener(this);
		shortcutLayout = (RelativeLayout) view.findViewById(R.id.shortcut_layout);
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		
		switch(v.getId()) {
		case R.id.shortcut_in:
			shortcutIn.setVisibility(View.GONE);
			shortcutLayout.setVisibility(View.VISIBLE);
			break;
			
		case R.id.shortcut_out:
			shortcutIn.setVisibility(View.VISIBLE);
			shortcutLayout.setVisibility(View.GONE);
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent =  new Intent(getActivity(), PostListActivity.class);
		startActivity(intent);
	}
}
