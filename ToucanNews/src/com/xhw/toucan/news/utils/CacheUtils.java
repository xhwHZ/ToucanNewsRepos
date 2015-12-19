package com.xhw.toucan.news.utils;

import android.content.Context;

public class CacheUtils
{
	/**
	 * �����������ݻ���
	 * @param context
	 * @param key ����url
	 * @param value ֵ��json
	 */
	public static void setCache(Context context,String key,String value)
	{
		SPUtils.setString(context, key, value);
	}
	
	/**
	 * ��ȡ����
	 * @param context
	 * @param key ����url
	 * @return
	 */
	public static String getCache(Context context,String key)
	{
		return SPUtils.getString(context, key, null);
	}
}
