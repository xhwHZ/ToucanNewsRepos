package com.xhw.toucan.news.global;

/**
 * 全局参数类
 * 
 * @author admin
 *
 */
public class GlobalContants
{
	/**
	 * 服务器URL
	 */
	public final static String SERVER_URL = "http://121.42.156.91:8080/ToucansNews";
	//public final static String SERVER_URL = "http://192.168.1.100/ToucansNews";
	
	/**
	 * 分类URL
	 */
	public final static String CATEGORIES_URL = SERVER_URL+"/categories.json";
	
	/**
	 * 组图URL
	 */
	public final static String PHOTOS_URL=SERVER_URL+"/photos/photos_1.json";
}
