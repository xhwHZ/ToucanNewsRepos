package com.xhw.toucan.news.domain;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 网络分类信息的封装
 * 字段名字必须要和服务器返回的字段名一致，便于gson解析
 * @author admin
 *
 */
public class NewsData implements Serializable
{
	
	public int retcode;
	
	public ArrayList<NewsMenuData> data;
	
	//public ArrayList extend;
	
	/**
	 * 侧边栏数据对象
	 * @author admin
	 *
	 */
	public class NewsMenuData{
		public String id;
		public String title;
		public int type;
		public String url;
		public ArrayList<NewsTabData> children;

	}
	
	/**
	 * 新闻页面的11个子页签对象
	 * @author admin
	 *
	 */
	public class NewsTabData{
		public String id;
		public String title;
		public int type;
		public String url;
	}
}
