package com.xhw.toucan.news.domain;

import java.util.ArrayList;

/**
 * 页签详情页数据
 * 
 * @author admin
 *
 */
public class TabData
{
	public int retcode;

	public TabDetail data;

	public class TabDetail
	{
		public String title;
		public String more;
		public ArrayList<TabNewsData> news;
		public ArrayList<TopNewsData> topnews;

	}

	/**
	 * 新闻列表
	 * 
	 * @author admin
	 *
	 */
	public class TabNewsData
	{
		public String id;
		public String listimage;
		public String pubdate;
		public String title;
		public String type;
		public String url;

	}

	/**
	 * 头条新闻(ViewPager)
	 * 
	 * @author admin
	 *
	 */
	public class TopNewsData
	{
		public String id;
		public String topimage;
		public String pubdate;
		public String title;
		public String type;
		public String url;


	}
}
