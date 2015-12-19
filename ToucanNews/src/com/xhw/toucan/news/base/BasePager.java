package com.xhw.toucan.news.base;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.xhw.toucan.news.R;
import com.xhw.toucan.news.activity.HomeActivity;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

public class BasePager
{
	protected Activity activity;
	
	public TextView tv_title;
	
	public ImageButton ib_menu;
	
	public FrameLayout fl_content;
	
	public View baseView;

	private ImageButton ib_photo;

	public ImageButton getIbPhoto()
	{
		return ib_photo;
	}
	
	public BasePager(Activity activity)
	{
		this.activity = activity;
		initView();
		
	}

	private void initView()
	{
		baseView = View.inflate(activity, R.layout.base_pager, null);
		tv_title=(TextView) baseView.findViewById(R.id.tv_title);
		ib_menu=(ImageButton) baseView.findViewById(R.id.ib_menu);
		ib_photo = (ImageButton) baseView.findViewById(R.id.ib_photo);
		fl_content=(FrameLayout) baseView.findViewById(R.id.fl_content);
		
		//切换左侧开关按钮
		ib_menu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v)
			{
				HomeActivity homeActivity= (HomeActivity) activity;
				homeActivity.toggleSlidingMenu();
			}
		});
	}
	
	/**
	 * 设置侧边栏是否可用
	 */
	public void setSlidingMenuEnable(boolean enable)
	{
		HomeActivity homeActivity=(HomeActivity) activity;
		SlidingMenu slidingMenu = homeActivity.getSlidingMenu();
		slidingMenu.setSlidingEnabled(enable);
		
	}
	
	public void setPhotoButtonVisiable(int isVisible)
	{
		this.ib_photo.setVisibility(isVisible);
	}
	
	public void initData(){}
}
