package com.xhw.toucan.news.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 不能滑动的ViewPager
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

	//不拦截子View的滑动
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	//不处理滑动
	@Override
	public boolean onTouchEvent(MotionEvent ev)
	{
		return false;
	}
	
}
