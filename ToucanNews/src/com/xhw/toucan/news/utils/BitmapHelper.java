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
			// 最后一个参数：加载图片，最多允许消耗本应用的多少内存(0.5f表示百分之五十)
			bitmapUtils = new BitmapUtils(BaseApplication.getAppliction(),
					FileUtils.getImgCacheDir(), 0.5f);
		}
		return bitmapUtils;
	}
}
