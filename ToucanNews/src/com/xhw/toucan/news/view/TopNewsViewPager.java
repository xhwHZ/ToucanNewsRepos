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
	 * 1���һ����������ֲ�ͼ�ĵ�һ��ҳ�棬��Ҫ���ؼ�����
	 *  2���󻬣��������ֲ�ͼ�����һ��ҳ��,��Ҫ���ؼ�����
	 *   3�����»�������Ҫ���ؼ�����
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
	{
		switch (ev.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			// һ��ʼ�ø��ؼ���Ҫ���أ������߲���ACTION_MOVE
			getParent().requestDisallowInterceptTouchEvent(true);
			startX = (int) ev.getX();
			startY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			int endX = (int) ev.getX();
			int endY = (int) ev.getY();
			// ���һ���
			if (Math.abs(endX - startX) > Math.abs(endY - startY))
			{
				// �һ�
				if (endX > startX)
				{
					// �ֲ�ͼ�ĵ�һ��ҳ�棬��Ҫ���ؼ�����
					if (this.getCurrentItem() == 0)
					{
						getParent().requestDisallowInterceptTouchEvent(false);
					}
				} else
				{// ��
					//�ֲ�ͼ�����һ��ҳ��,��Ҫ���ؼ�����
					if (this.getCurrentItem() == this.getAdapter().getCount() - 1)
					{
						getParent().requestDisallowInterceptTouchEvent(false);
					}
				}

			} else
			{// ���»���
				getParent().requestDisallowInterceptTouchEvent(false);
			}
			break;

		}

		return super.dispatchTouchEvent(ev);
	}

}
