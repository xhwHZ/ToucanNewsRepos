package com.xhw.toucan.news.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferencesπ§æﬂ¿‡
 * @author admin
 *
 */
public class SPUtils
{
	private SPUtils(){}
	
	private final static String SP_NAME="config";
	
	public static boolean getBoolean(Context context,String key,boolean defaultValue)
	{
		SharedPreferences sp=context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		return sp.getBoolean(key, defaultValue);
	}
	
	public static void setBoolean(Context context,String key,boolean value)
	{
		SharedPreferences sp=context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		sp.edit().putBoolean(key, value).commit();
	}
	
	public static String getString(Context context,String key,String defaultValue)
	{
		SharedPreferences sp=context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		return sp.getString(key, defaultValue);
	}
	
	public static void setString(Context context,String key,String value)
	{
		SharedPreferences sp=context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		sp.edit().putString(key, value).commit();
	}
	
	public static int getInt(Context context,String key,int defaultValue)
	{
		SharedPreferences sp=context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		return sp.getInt(key, defaultValue);
	}
	
	public static void setInt(Context context,String key,int value)
	{
		SharedPreferences sp=context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		sp.edit().putInt(key, value).commit();
	}
}
