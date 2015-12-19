package com.xhw.toucan.news.base.impl;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.xhw.toucan.news.activity.HomeActivity;
import com.xhw.toucan.news.base.BaseMenuDetailPager;
import com.xhw.toucan.news.base.BasePager;
import com.xhw.toucan.news.base.menudetail.InteractMenuDetailPager;
import com.xhw.toucan.news.base.menudetail.NewsMenuDetailPager;
import com.xhw.toucan.news.base.menudetail.PhotoMenuDetailPager;
import com.xhw.toucan.news.base.menudetail.TopicMenuDetailPager;
import com.xhw.toucan.news.domain.NewsData;
import com.xhw.toucan.news.domain.NewsData.NewsTabData;
import com.xhw.toucan.news.fragment.HomeMenuLeftFragment;
import com.xhw.toucan.news.global.GlobalContants;
import com.xhw.toucan.news.utils.CacheUtils;

public class NewsCenterPager extends BasePager
{

	public NewsCenterPager(Activity activity)
	{
		super(activity);
	}

	@Override
	public void initData()
	{
		tv_title.setText("新闻中心");
		this.setSlidingMenuEnable(true);// 开启侧边栏的侧拉功能
		// 将新View添加进FrameLayout
		// TextView child = new TextView(activity);
		// child.setText("新闻");
		// child.setTextColor(Color.RED);
		// child.setTextSize(25);
		// child.setGravity(Gravity.CENTER);
		// fl_content.addView(child);

		// 获取缓存
		String cache = CacheUtils.getCache(activity,
				GlobalContants.CATEGORIES_URL);
		if (!TextUtils.isEmpty(cache))// 有
		{
			parseJsonData(cache);
		} 
		//不管有没有缓存，都要请求服务器,保证数据最新
		getDataFromServer();

	}

	// 网络请求返回的数据对象
	private NewsData newsData;

	// 四个新闻内容详情页
	private List<BaseMenuDetailPager> detailPagerList;

	private void getDataFromServer()
	{
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, GlobalContants.CATEGORIES_URL,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo)
					{
						String result = responseInfo.result;
						// 解析json数据
						parseJsonData(result);
						//设置缓存
						CacheUtils.setCache(activity, GlobalContants.CATEGORIES_URL, result);
					}

					@Override
					public void onFailure(HttpException error, String msg)
					{
						Toast.makeText(activity, "网络连接错误", 0).show();
					}
				});
	}

	// 解析json数据
	private void parseJsonData(String data)
	{
		Gson gson = new Gson();
		newsData = gson.fromJson(data, NewsData.class);
		// 强转回HomeActivity
		HomeActivity homeActivity = (HomeActivity) activity;
		// 获取左边菜单栏的Fragment
		HomeMenuLeftFragment homeMenuLeftFragment = homeActivity
				.getHomeMenuLeftFragment();
		// 将数据传给左边菜单栏Fragment展示
		homeMenuLeftFragment.setData(newsData);
		// 初始化四个新闻内容详情页
		detailPagerList = new ArrayList<BaseMenuDetailPager>();
		detailPagerList.add(new NewsMenuDetailPager(activity));
		detailPagerList.add(new TopicMenuDetailPager(activity));
		detailPagerList.add(new PhotoMenuDetailPager(activity));
		detailPagerList.add(new InteractMenuDetailPager(activity));
		// 设置默认选中的详情页
		setDetailPager(homeMenuLeftFragment.getCurrentPos());
	}

	// 获取新闻标签页数据
	public List<NewsTabData> getChildrenData()
	{
		return newsData.data.get(0).children;
	}

	// 设置新闻详情页面到fl_content中
	public void setDetailPager(int position)
	{
		fl_content.removeAllViews();// 清除原来的内容
		BaseMenuDetailPager baseMenuDetailPager = detailPagerList.get(position);
		fl_content.addView(baseMenuDetailPager.mRootView);
		// 初始化数据
		baseMenuDetailPager.initData();
		baseMenuDetailPager.initListener();
		// 设置标题
		String title = newsData.data.get(position).title;
		tv_title.setText(title);
		if(position==2)
		{
			this.setPhotoButtonVisiable(View.VISIBLE);
		}else{
			this.setPhotoButtonVisiable(View.GONE);
		}
	}

}
