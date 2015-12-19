package com.xhw.toucan.news.base.menudetail;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.xhw.toucan.news.base.BaseMenuDetailPager;

/**
 * �˵�����ҳ-ר��
 * @author admin
 *
 */
public class TopicMenuDetailPager extends BaseMenuDetailPager
{

	public TopicMenuDetailPager(Activity activity)
	{
		super(activity);
	}

	@Override
	public View initView()
	{
		TextView tv=new TextView(mActivity);
		tv.setText("�˵�����ҳ-ר��");
		tv.setTextColor(Color.RED);
		tv.setTextSize(25);
		tv.setGravity(Gravity.CENTER);
		return tv;
	}

}
