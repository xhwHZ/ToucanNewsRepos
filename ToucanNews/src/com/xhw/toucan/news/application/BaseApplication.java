package com.xhw.toucan.news.application;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

public class BaseApplication extends Application
{
	private static BaseApplication appliction;
	
	private static int mainThreadId;
	
	private static Handler mainHandler;
	
	
	
	public static int getMainThreadId()
	{
		return mainThreadId;
	}

	public static Handler getMainHandler()
	{
		return mainHandler;
	}

	public static Context getAppliction()
	{
		return appliction;
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
		appliction=this;
		//�����õ�һ�������߳�id
		mainThreadId=android.os.Process.myTid();
		//�����õ�һ�������̵߳�handler
		mainHandler=new Handler();
		
	}
}
