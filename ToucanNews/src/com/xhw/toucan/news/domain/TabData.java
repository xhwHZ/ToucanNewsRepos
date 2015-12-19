package com.xhw.toucan.news.domain;

import java.util.ArrayList;

/**
 * ҳǩ����ҳ����
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
	 * �����б�
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
	 * ͷ������(ViewPager)
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
