package com.xhw.toucan.news.domain;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * ���������Ϣ�ķ�װ
 * �ֶ����ֱ���Ҫ�ͷ��������ص��ֶ���һ�£�����gson����
 * @author admin
 *
 */
public class NewsData implements Serializable
{
	
	public int retcode;
	
	public ArrayList<NewsMenuData> data;
	
	//public ArrayList extend;
	
	/**
	 * ��������ݶ���
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
	 * ����ҳ���11����ҳǩ����
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
