package com.xhw.toucan.news.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * ���ܻ�����ViewPager
 * @author admin
 *
 */
public class NoScrollViewPager extends ViewPager
{

	public NoScrollViewPager(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public NoScrollViewPager(Context context)
	{
		super(context);
		// TODO Auto-generated constructor stub
	}

	//��������View�Ļ���
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	//��������
	@Override
	public boolean onTouchEvent(MotionEvent ev)
	{
		return false;
	}
	
}
