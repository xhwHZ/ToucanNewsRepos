package com.xhw.toucan.news.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class TopNewsViewPager extends ViewPager
{

	private int startX;
	private int startY;

	public TopNewsViewPager(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public TopNewsViewPager(Context context)
	{
		super(context);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 1、右滑，而且是轮播图的第一个页面，需要父控件拦截
	 *  2、左滑，而且是轮播图的最后一个页面,需要父控件拦截
	 *   3、上下滑动，需要父控件拦截
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
	{
		switch (ev.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			// 一开始让父控件不要拦截，否则走不到ACTION_MOVE
			getParent().requestDisallowInterceptTouchEvent(true);
			startX = (int) ev.getX();
			startY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			int endX = (int) ev.getX();
			int endY = (int) ev.getY();
			// 左右滑动
			if (Math.abs(endX - startX) > Math.abs(endY - startY))
			{
				// 右滑
				if (endX > startX)
				{
					// 轮播图的第一个页面，需要父控件拦截
					if (this.getCurrentItem() == 0)
					{
						getParent().requestDisallowInterceptTouchEvent(false);
					}
				} else
				{// 左滑
					//轮播图的最后一个页面,需要父控件拦截
					if (this.getCurrentItem() == this.getAdapter().getCount() - 1)
					{
						getParent().requestDisallowInterceptTouchEvent(false);
					}
				}

			} else
			{// 上下滑动
				getParent().requestDisallowInterceptTouchEvent(false);
			}
			break;

		}

		return super.dispatchTouchEvent(ev);
	}

}
