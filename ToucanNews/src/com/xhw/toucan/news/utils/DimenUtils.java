package com.xhw.toucan.news.utils;

import android.content.Context;

public class DimenUtils
{
	/**
	 * dp转成px
	 * @param context
	 * @param dp
	 * @return
	 */
	public static int dp2px(Context context,int dp)
	{
		float density=context.getResources().getDisplayMetrics().density;
		int px=(int) (dp*density+0.5);//四舍五入
		return px;
	}
	
	/**
	 * px转dp
	 * @param context
	 * @param px
	 * @return
	 */
	public static float px2dp(Context context,int px)
	{
		float density=context.getResources().getDisplayMetrics().density;
		float dp=px/density;
		return dp;
		
	}
}
