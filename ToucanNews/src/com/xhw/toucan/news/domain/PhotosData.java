package com.xhw.toucan.news.domain;

import java.util.ArrayList;

public class PhotosData
{
	public int retcode;
	
	public PhotoDetail data;
	
	@Override
	public String toString()
	{
		return "PhotosData [retcode=" + retcode + ", data=" + data + "]";
	}

	public class PhotoDetail
	{
		public String title;
		public ArrayList<PhotoNews> news;
	}
	
	public class PhotoNews
	{
		public String id;
		
		public String listimage;
		
		public String pubdate;
		
		public String title;
		
		public String type;
		
		public String url;

		@Override
		public String toString()
		{
			return "PhotoNews [id=" + id + ", listimage=" + listimage
					+ ", pubdate=" + pubdate + ", title=" + title + ", type="
					+ type + ", url=" + url + "]";
		}
		
		
		
	}
}
