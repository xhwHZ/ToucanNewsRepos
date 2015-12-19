package com.xhw.toucan.news.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class HorizontalViewPager extends ViewPager
{

	public HorizontalViewPager(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public HorizontalViewPager(Context context)
	{
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
	{
		//不在第一页时，拦截掉侧边栏的滑动
		if(this.getCurrentItem()!=0)
		{
		getParent().requestDisallowInterceptTouchEvent(true);
		}else{//在第一页时，响应侧边栏滑动
			getParent().requestDisallowInterceptTouchEvent(false);
		}
		return super.dispatchTouchEvent(ev);
	}
	
}
