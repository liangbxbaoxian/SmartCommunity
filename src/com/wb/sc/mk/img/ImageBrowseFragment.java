package com.wb.sc.mk.img;

import java.util.ArrayList;
import java.util.List;

import com.wb.sc.R;
import com.wb.sc.bean.ImagesItem;
import com.wb.sc.config.DebugConfig;
import com.wb.sc.config.IntentExtraConfig;
import com.wb.sc.config.NetConfig;

import ru.truba.touchgallery.GalleryWidget.GalleryViewPager;
import ru.truba.touchgallery.GalleryWidget.UrlPagerAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ImageBrowseFragment extends Fragment {
	
	private Activity mActivity;
	private GalleryViewPager gViewPager;
	private UrlPagerAdapter mAdapter;
	private ImagesItem imagesItem;
	private ImageBrowseListener imgListener;
	private int imgPos; 
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
		imgListener = (ImageBrowseListener) mActivity;
		imagesItem = getArguments().getParcelable(IntentExtraConfig.IMAGE_BROWSER_DATA);
		imgPos = getArguments().getInt(IntentExtraConfig.IMAGE_BROWSER_POS);
		DebugConfig.showLog("image_browse", "onAttach");
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.image_browse_layout, container, false);		
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);		
		initView(view);
	}
	
	private void initView(View view) {
		gViewPager = (GalleryViewPager) view.findViewById(R.id.viewer);
		List<String> imagesList = new ArrayList<String>();
		for(int i=0; i<imagesItem.images.length; i++) {
			imagesList.add(NetConfig.getPictureUrl(imagesItem.images[i]));
		}
		
		mAdapter = new UrlPagerAdapter(mActivity, imagesList);
		gViewPager.setOffscreenPageLimit(3);
		gViewPager.setAdapter(mAdapter);
		
		gViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				int index = position + 1;
				imgListener.setMenuItem(index + "/" + imagesItem.imageNum, index);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		
		gViewPager.setCurrentItem(imgPos);
	}
}
