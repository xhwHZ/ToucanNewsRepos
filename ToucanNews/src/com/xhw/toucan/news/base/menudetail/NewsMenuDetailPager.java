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
 * 菜单详情页-新闻
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
		// ViewPager指示器
		indicator = (TabPageIndicator) view.findViewById(R.id.indicator);
		ViewUtils.inject(this, view);

		return view;
	}

	// 下一页按钮的处理
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
		// 获取新闻中心页面
		NewsCenterPager newsCenterPager = homeActivity.getHomeContentFragment()
				.getNewsCenterPager();
		// 获取新闻标签页数据
		newsTabData = newsCenterPager.getChildrenData();

		for (int i = 0; i < newsTabData.size(); i++)
		{
			TabDetailPager pager = new TabDetailPager(mActivity);
			// 设置数据
			pager.setTabDetailMenuItem(newsTabData.get(i));
			detailPagerList.add(pager);
		}

		vp_news_detail.setAdapter(new MenuDetailAdapter());
		// 给指示器绑定ViewPager，绑定的viwPager必须设置了PageAdapter
		indicator.setViewPager(vp_news_detail);
	}

	@Override
	public void initListener()
	{
		//跟指示器绑定后，应该设置指示器的OnPageChange监听，不该设置源ViewPager的监听了
		indicator.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position)
			{
				HomeActivity homeActivity = (HomeActivity) mActivity;
				SlidingMenu slidingMenu = homeActivity.getSlidingMenu();
				// 第一页才能滑出侧边栏
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

		// 指示器需要重写的方法
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
