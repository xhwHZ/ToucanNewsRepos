package com.xhw.toucan.news.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.widget.Toast;

public class APPUtils
{
	public static void openQQ(Context context, String qqNum)
	{
		String qqPackageName = "com.tencent.mobileqq";
		if (isApkInstalled(context, qqPackageName))
		{
			String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + qqNum;
			context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
		} else
		{
			Toast.makeText(context, "���Ȱ�װ��׿�ֻ�QQ", 0).show();
		}
	}

	public static boolean isApkInstalled(Context context, String packageName)
	{
		try
		{
			context.getPackageManager().getApplicationInfo(packageName,
					PackageManager.GET_UNINSTALLED_PACKAGES);
			return true;
		} catch (NameNotFoundException e)
		{
			e.printStackTrace();
			return false;
		}
	}

	// ��ȡ�汾��
	public static String getVersionName(Context context)
	{
		// ��ȡ��������
		PackageManager packageManager = context.getPackageManager();

		try
		{
			// ��ȡ����Ϣ
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			return packageInfo.versionName;
		} catch (NameNotFoundException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	// ��ȡ�汾���
	public static int getVersionCode(Context context)
	{
		PackageManager packageManager = context.getPackageManager();
		try
		{
			PackageInfo info = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			return info.versionCode;
		} catch (NameNotFoundException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
}
