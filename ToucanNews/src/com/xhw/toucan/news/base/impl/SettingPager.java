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
		tv_title.setText("����");
		ib_menu.setVisibility(View.INVISIBLE);
		this.setSlidingMenuEnable(false);//�رղ�����Ĳ�������
		child = View.inflate(activity, R.layout.page_setting, null);
		initUpdate();
		fl_content.addView(child);
		
	}

	private void initUpdate()
	{
		siv_update = (SettingItemView) child.findViewById(R.id.siv_update);

		// ͨ��sp���ж�checkbox�Ƿ�ѡ
		boolean status = SPUtils.getBoolean(activity,"auto update", false);
		if (status)
		{
			siv_update.setChecked(true);
		} else
		{
			siv_update.setChecked(false);
		}

		// ��������Զ���ؼ�������ȡ����ѡcheckbox�������û�����
		siv_update.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.siv_update:
			//�Զ���������
			if (siv_update.isChecked())
			{
				// ���ò���ѡ
				siv_update.setChecked(false);
				// siv_update.setDesc("�Զ������Ѿ��ر�");
				// �����ļ��������״̬
				SPUtils.setBoolean(activity, "auto update", false);
			} else
			{
				siv_update.setChecked(true);
				// siv_update.setDesc("�Զ������Ѿ�����");
				SPUtils.setBoolean(activity, "auto update", true);
			}
			break;
		}
	}
	
}
