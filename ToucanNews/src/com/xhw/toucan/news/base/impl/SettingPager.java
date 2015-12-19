package com.xhw.toucan.news.base.impl;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;

import com.xhw.toucan.news.R;
import com.xhw.toucan.news.base.BasePager;
import com.xhw.toucan.news.utils.SPUtils;
import com.xhw.toucan.news.view.SettingItemView;

public class SettingPager extends BasePager implements OnClickListener
{

	private View child;
	private SettingItemView siv_update;

	public SettingPager(Activity activity)
	{
		super(activity);
	}

	@Override
	public void initData()
	{
		fl_content.removeAllViews();
		tv_title.setText("设置");
		ib_menu.setVisibility(View.INVISIBLE);
		this.setSlidingMenuEnable(false);//关闭侧边栏的侧拉功能
		child = View.inflate(activity, R.layout.page_setting, null);
		initUpdate();
		fl_content.addView(child);
		
	}

	private void initUpdate()
	{
		siv_update = (SettingItemView) child.findViewById(R.id.siv_update);

		// 通过sp来判断checkbox是否勾选
		boolean status = SPUtils.getBoolean(activity,"auto update", false);
		if (status)
		{
			siv_update.setChecked(true);
		} else
		{
			siv_update.setChecked(false);
		}

		// 点击整个自定义控件，都能取消或勾选checkbox，增加用户体验
		siv_update.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.siv_update:
			//自动更新设置
			if (siv_update.isChecked())
			{
				// 设置不勾选
				siv_update.setChecked(false);
				// siv_update.setDesc("自动更新已经关闭");
				// 本地文件保存更新状态
				SPUtils.setBoolean(activity, "auto update", false);
			} else
			{
				siv_update.setChecked(true);
				// siv_update.setDesc("自动更新已经开启");
				SPUtils.setBoolean(activity, "auto update", true);
			}
			break;
		}
	}
	
}
