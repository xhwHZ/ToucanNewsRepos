package com.xhw.toucan.news.utils;

import android.content.Context;

public class CacheUtils
{
	/**
	 * 设置网络数据缓存
	 * @param context
	 * @param key 键是url
	 * @param value 值是json
	 */
	public static void setCache(Context context,String key,String value)
	{
		SPUtils.setString(context, key, value);
	}
	
	/**
	 * 获取缓存
	 * @param context
	 * @param key 键是url
	 * @return
	 */
	public static String getCache(Context context,String key)
	{
		return SPUtils.getString(context, key, null);
	}
}
