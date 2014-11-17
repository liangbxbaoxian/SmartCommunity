package com.wb.sc.mk.img;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.wb.sc.R;
import com.wb.sc.activity.base.BaseHeaderActivity;
import com.wb.sc.bean.ImagesItem;
import com.wb.sc.config.IntentExtraConfig;

public class ImageBrowseActivity extends BaseHeaderActivity implements ImageBrowseListener{
	
	private FragmentTabHost fTabHost;
	private ArrayList<ImagesItem> imageList;
	private boolean disTab;
	private MenuItem numItem;
	private int imgPos;
	
	private TextView headerNumTv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_header_img_layout, R.layout.activity_image_browser);
		initHeader(R.string.ac_img_browser);
		getIntentData();
		initView();
	}
	
	@Override
	public void getIntentData() {
		imageList = getIntent().getParcelableArrayListExtra(IntentExtraConfig.IMAGE_BROWSER_DATA);
		disTab = getIntent().getBooleanExtra(IntentExtraConfig.IMAGE_BROWSER_DIS_TAB, true);
		imgPos = getIntent().getIntExtra(IntentExtraConfig.IMAGE_BROWSER_POS, 0);
	}

	@Override
	public void initView() {
		headerNumTv = (TextView) findViewById(R.id.common_header_num);
		headerNumTv.setText((imgPos+1) + "/" + imageList.get(0).imageNum);
		
		fTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
		fTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
		fTabHost.getTabWidget().setDividerDrawable(null);
		
		for(int i=0; i<imageList.size(); i++) {
			ImagesItem imagesItem = imageList.get(i);
			TabSpec tabSpec = fTabHost.newTabSpec("type"+i);
			tabSpec.setIndicator(getTabItemView(imagesItem));
			Bundle bundle = new Bundle();
			bundle.putParcelable(IntentExtraConfig.IMAGE_BROWSER_DATA, imagesItem);
			bundle.putInt(IntentExtraConfig.IMAGE_BROWSER_POS, imgPos);
			fTabHost.addTab(tabSpec, ImageBrowseFragment.class, bundle);
		}
		
		if(!disTab) {
			fTabHost.setVisibility(View.GONE);
		}
		
		fTabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				int index = Integer.parseInt(tabId.substring(tabId.length()-1, tabId.length()));
				int num = imageList.get(index).imageNum;
				numItem.setTitle("1/" + num);
			}			
		});
	}
	
	private View getTabItemView(ImagesItem imagesItem) {
		View v = (View)LayoutInflater.from(this).inflate(R.layout.image_tab_item, null);
		
		TextView iconName = (TextView)v.findViewById(R.id.tab_icon_name);
		iconName.setText(imagesItem.name);
		
		return v;
	}

	@Override
	public void setMenuItem(String content) {
		headerNumTv.setText(content);
	}
}
