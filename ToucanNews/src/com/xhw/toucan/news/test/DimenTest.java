package com.xhw.toucan.news.test;

import android.test.AndroidTestCase;

public class DimenTest extends AndroidTestCase
{
	public void getDensity()
	{
		float density=getContext().getResources().getDisplayMetrics().density;
		System.out.println(density);
	}
}
