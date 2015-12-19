package com.xhw.toucan.news.base.menudetail;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.viewpagerindicator.TabPageIndicator;
import com.xhw.toucan.news.R;
import com.xhw.toucan.news.activity.HomeActivity;
import com.xhw.toucan.news.base.BaseMenuDetailPager;
import com.xhw.toucan.news.base.TabDetailPager;
import com.xhw.toucan.news.base.impl.NewsCenterPager;
import com.xhw.toucan.news.domain.NewsData.NewsTabData;

/**
 * �˵�����ҳ-����
 * 
 * @author admin
 *
 */
public class NewsMenuDetailPager extends BaseMenuDetailPager
{

	private ViewPager vp_news_detail;

	private List<TabDetailPager> detailPagerList;

	private TabPageIndicator indicator;

	private List<NewsTabData> newsTabData;

	public NewsMenuDetailPager(Activity activity)
	{
		super(activity);
	}

	@Override
	public View initView()
	{
		View view = View.inflate(mActivity, R.layout.news_menu_detail, null);
		vp_news_detail = (ViewPager) view.findViewById(R.id.vp_news_detail);
		// ViewPagerָʾ��
		indicator = (TabPageIndicator) view.findViewById(R.id.indicator);
		ViewUtils.inject(this, view);

		return view;
	}

	// ��һҳ��ť�Ĵ���
	@OnClick(R.id.ib_next)
	public void nextPage(View v)
	{
		int curIndex = vp_news_detail.getCurrentItem();
		vp_news_detail.setCurrentItem(++curIndex);
	}

	@Override
	public void initData()
	{
		detailPagerList = new ArrayList<TabDetailPager>();
		HomeActivity homeActivity = (HomeActivity) mActivity;
		// ��ȡ��������ҳ��
		NewsCenterPager newsCenterPager = homeActivity.getHomeContentFragment()
				.getNewsCenterPager();
		// ��ȡ���ű�ǩҳ����
		newsTabData = newsCenterPager.getChildrenData();

		for (int i = 0; i < newsTabData.size(); i++)
		{
			TabDetailPager pager = new TabDetailPager(mActivity);
			// ��������
			pager.setTabDetailMenuItem(newsTabData.get(i));
			detailPagerList.add(pager);
		}

		vp_news_detail.setAdapter(new MenuDetailAdapter());
		// ��ָʾ����ViewPager���󶨵�viwPager����������PageAdapter
		indicator.setViewPager(vp_news_detail);
	}

	@Override
	public void initListener()
	{
		//��ָʾ���󶨺�Ӧ������ָʾ����OnPageChange��������������ԴViewPager�ļ�����
		indicator.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position)
			{
				HomeActivity homeActivity = (HomeActivity) mActivity;
				SlidingMenu slidingMenu = homeActivity.getSlidingMenu();
				// ��һҳ���ܻ��������
				if (position == 0)
				{
					slidingMenu.setSlidingEnabled(true);
				} else
				{
					slidingMenu.setSlidingEnabled(false);
				}
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels)
			{

			}

			@Override
			public void onPageScrollStateChanged(int state)
			{

			}
		});
	}

	class MenuDetailAdapter extends PagerAdapter
	{

		// ָʾ����Ҫ��д�ķ���
		@Override
		public CharSequence getPageTitle(int position)
		{
			return newsTabData.get(position).title;
		}

		@Override
		public int getCount()
		{
			return detailPagerList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object)
		{
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position)
		{
			TabDetailPager detailPager = detailPagerList.get(position);
			detailPager.initData();
			detailPager.initListener();
			container.addView(detailPager.mRootView);
			return detailPagerList.get(position).mRootView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object)
		{
			container.removeView((View) object);
		}

	}
}
