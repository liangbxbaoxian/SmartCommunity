package com.wb.sc.mk.main;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.viewpagerindicator.CirclePageIndicator;
import com.wb.sc.R;
import com.wb.sc.activity.base.BaseExtraLayoutFragment;
import com.wb.sc.adapter.AdvAdapter;
import com.wb.sc.adapter.PostTypeAdapter;
import com.wb.sc.bean.Forum;
import com.wb.sc.mk.butler.PropertyComplain;
import com.wb.sc.mk.butler.PropertyRepairsActivity;
import com.wb.sc.mk.personal.MsgCenterActivity;
import com.wb.sc.mk.post.PostListActivity;

public class HomeFragment extends BaseExtraLayoutFragment implements OnClickListener,
	OnItemClickListener{
	
	//标题栏相关
	private View phoneV;
	private View msgV;
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
	private ImageButton complaint;
	private ImageButton repair;
	
	private List<Forum> forums = new ArrayList<Forum>();
	
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
		initData();
		initView(view);
	}

	private void initView(View view) {
		msgV = view.findViewById(R.id.msg);
		msgV.setOnClickListener(this);
		
		advVp = (ViewPager) view.findViewById(R.id.adv_pager);
		advIndicator = (CirclePageIndicator) view.findViewById(R.id.adv_indicator);
		advVp.setAdapter(advAdapter);
		advIndicator.setViewPager(advVp);
		
		postsTypeLv = (ListView) view.findViewById(R.id.list);
		typeAdapter = new PostTypeAdapter(getActivity(), forums);
		postsTypeLv.setAdapter(typeAdapter);
		postsTypeLv.setOnItemClickListener(this);
		
		shortcutIn = (ImageButton) view.findViewById(R.id.shortcut_in);
		shortcutIn.setOnClickListener(this);
		shortcutOut = (ImageButton) view.findViewById(R.id.shortcut_out);
		shortcutOut.setOnClickListener(this);
		shortcutLayout = (RelativeLayout) view.findViewById(R.id.shortcut_layout);
		
		complaint = (ImageButton) view.findViewById(R.id.complaint);
		complaint.setOnClickListener(this);
		repair = (ImageButton) view.findViewById(R.id.repair);
		repair.setOnClickListener(this);
	}
	
	private void initData() {
		forums.clear();
		String [] type = {"[社区分享]", "[邻里互助]", "[社区雷锋]", ""};
		String [] title = {"宝宝爬行大赛", "谁家丢了泰迪？", "泰迪狗失而复得", ""};
		String [] content =  {"宝宝们今天爬啊爬啊可厉害了，照片是天线宝宝冠军", "谁家丢了泰迪？我把它放在放在物业处了", "今天上午在遛狗时不慎走失小泰迪狗，幸亏24号楼305业主……"};
		int resId[] = {R.drawable.test_forum_one, R.drawable.test_forum_two, R.drawable.test_forum_three};
		
		for (int j = 0; j < content.length; j++) {
			Forum forum = new Forum();
			forum.content = content[j];
			forum.type = type[j];
			forum.title = title[j];
			forum.resId = resId[j];
			forums.add(forum);
		}
		
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		
		switch(v.getId()) {
		case R.id.msg:{
			Intent intent = new Intent(getActivity(), MsgCenterActivity.class);
			startActivity(intent);
		}break;
			
		case R.id.shortcut_in:
			shortcutIn.setVisibility(View.GONE);
			shortcutLayout.setVisibility(View.VISIBLE);
			break;
			
		case R.id.shortcut_out:
			shortcutIn.setVisibility(View.VISIBLE);
			shortcutLayout.setVisibility(View.GONE);
			break;
		case R.id.complaint:
			Intent intent = new Intent(getActivity(), PropertyComplain.class);
			startActivity(intent);
			break;
		case R.id.repair:
			Intent repair = new Intent(getActivity(), PropertyRepairsActivity.class);
			startActivity(repair);
			break;
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent =  new Intent(getActivity(), PostListActivity.class);
		startActivity(intent);
	}
}
