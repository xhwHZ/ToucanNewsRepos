package com.xhw.toucan.news.utils;

import com.lidroid.xutils.BitmapUtils;
import com.xhw.toucan.news.application.BaseApplication;

public class BitmapHelper
{
	public static BitmapUtils bitmapUtils;

	public static BitmapUtils getBitmapUtils()
	{
		if (bitmapUtils == null)
		{
			// ���һ������������ͼƬ������������ı�Ӧ�õĶ����ڴ�(0.5f��ʾ�ٷ�֮��ʮ)
			bitmapUtils = new BitmapUtils(BaseApplication.getAppliction(),
					FileUtils.getImgCacheDir(), 0.5f);
		}
		return bitmapUtils;
	}
}
