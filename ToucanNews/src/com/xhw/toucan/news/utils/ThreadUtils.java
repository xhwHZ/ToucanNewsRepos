package com.xhw.toucan.news.utils;

import android.os.Handler;

import com.xhw.toucan.news.application.BaseApplication;

public class ThreadUtils
{
	public static void runOnUiThread(Runnable runnable)
	{
		//�����ǰ���е������߳�
		if(android.os.Process.myTid()==BaseApplication.getMainThreadId())
		{
			runnable.run();
		}else{//���߳�
			Handler mainHandler = BaseApplication.getMainHandler();
			mainHandler.post(runnable);
		}
	}
	
	public static boolean isRunOnMainThread() {
		return android.os.Process.myTid() == getMainThreadTid();
	}

	private static int getMainThreadTid() {
		return BaseApplication.getMainThreadId();
	}
	
	public static void postDelayed(Runnable runnable,long delayTime)
	{
		BaseApplication.getMainHandler().postDelayed(runnable, delayTime);
	}
	public static void post(Runnable runnable)
	{
		BaseApplication.getMainHandler().post(runnable);
	}
	
	public static void cancel(Runnable runnable)
	{
		BaseApplication.getMainHandler().removeCallbacks(runnable);
	}
}
