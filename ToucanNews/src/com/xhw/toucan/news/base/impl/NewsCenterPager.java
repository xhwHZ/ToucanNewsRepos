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
		tv_title.setText("��������");
		this.setSlidingMenuEnable(true);// ����������Ĳ�������
		// ����View��ӽ�FrameLayout
		// TextView child = new TextView(activity);
		// child.setText("����");
		// child.setTextColor(Color.RED);
		// child.setTextSize(25);
		// child.setGravity(Gravity.CENTER);
		// fl_content.addView(child);

		// ��ȡ����
		String cache = CacheUtils.getCache(activity,
				GlobalContants.CATEGORIES_URL);
		if (!TextUtils.isEmpty(cache))// ��
		{
			parseJsonData(cache);
		} 
		//������û�л��棬��Ҫ���������,��֤��������
		getDataFromServer();

	}

	// �������󷵻ص����ݶ���
	private NewsData newsData;

	// �ĸ�������������ҳ
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
						// ����json����
						parseJsonData(result);
						//���û���
						CacheUtils.setCache(activity, GlobalContants.CATEGORIES_URL, result);
					}

					@Override
					public void onFailure(HttpException error, String msg)
					{
						Toast.makeText(activity, "�������Ӵ���", 0).show();
					}
				});
	}

	// ����json����
	private void parseJsonData(String data)
	{
		Gson gson = new Gson();
		newsData = gson.fromJson(data, NewsData.class);
		// ǿת��HomeActivity
		HomeActivity homeActivity = (HomeActivity) activity;
		// ��ȡ��߲˵�����Fragment
		HomeMenuLeftFragment homeMenuLeftFragment = homeActivity
				.getHomeMenuLeftFragment();
		// �����ݴ�����߲˵���Fragmentչʾ
		homeMenuLeftFragment.setData(newsData);
		// ��ʼ���ĸ�������������ҳ
		detailPagerList = new ArrayList<BaseMenuDetailPager>();
		detailPagerList.add(new NewsMenuDetailPager(activity));
		detailPagerList.add(new TopicMenuDetailPager(activity));
		detailPagerList.add(new PhotoMenuDetailPager(activity));
		detailPagerList.add(new InteractMenuDetailPager(activity));
		// ����Ĭ��ѡ�е�����ҳ
		setDetailPager(homeMenuLeftFragment.getCurrentPos());
	}

	// ��ȡ���ű�ǩҳ����
	public List<NewsTabData> getChildrenData()
	{
		return newsData.data.get(0).children;
	}

	// ������������ҳ�浽fl_content��
	public void setDetailPager(int position)
	{
		fl_content.removeAllViews();// ���ԭ��������
		BaseMenuDetailPager baseMenuDetailPager = detailPagerList.get(position);
		fl_content.addView(baseMenuDetailPager.mRootView);
		// ��ʼ������
		baseMenuDetailPager.initData();
		baseMenuDetailPager.initListener();
		// ���ñ���
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
