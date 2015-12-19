package com.xhw.toucan.news.fragment;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xhw.toucan.news.R;
import com.xhw.toucan.news.activity.HomeActivity;
import com.xhw.toucan.news.base.impl.NewsCenterPager;
import com.xhw.toucan.news.domain.NewsData;
import com.xhw.toucan.news.domain.NewsData.NewsMenuData;

/**
 * 侧边栏Fragment(新闻，专题，主题，互动)
 * 
 * @author admin
 *
 */
public class HomeMenuLeftFragment extends BaseFragment
{
	@ViewInject(R.id.lv_left_menu)
	private ListView lv_left_menu;

	@Override
	public View initView()
	{
	
		view = View.inflate(mActivity, R.layout.fragment_home_menu_left,
				null);
		ViewUtils.inject(this, view);
		return view;
	}

	// 当前选中的位置
	private int currentPos = 0;

	public int getCurrentPos()
	{
		return this.currentPos;
	}

	@Override
	public void initListener()
	{
		lv_left_menu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{

				HomeActivity homeActivity = (HomeActivity) mActivity;
				// 获取主内容的Fragment
				HomeContentFragment homeContentFragment = homeActivity
						.getHomeContentFragment();
				int index = homeContentFragment.getViewPagerCurIndex();
				// 不是新闻页面，不响应点击事件
				if (index != 0)
				{
					return;
				}

				// 刷新当前选中位置
				currentPos = position;
				// 迫使getView方法执行，刷新Menu样式
				adapter.notifyDataSetChanged();

				// 设置主Content的内容
				setHomeContent(position);

				// 切换Slidmenu状态
				toggleSlidingMenu();

			}
		});
	}

	// //切换Slidingmenu状态
	protected void toggleSlidingMenu()
	{
		HomeActivity homeActivity = (HomeActivity) mActivity;
		homeActivity.toggleSlidingMenu();
	}

	// 设置主Content的内容
	protected void setHomeContent(int position)
	{
		HomeActivity homeActivity = (HomeActivity) mActivity;
		// 获取主内容的Fragment
		HomeContentFragment homeContentFragment = homeActivity
				.getHomeContentFragment();
		// 获取新闻中心页面
		NewsCenterPager newsCenterPager = homeContentFragment
				.getNewsCenterPager();
		newsCenterPager.setDetailPager(position);
	}

	private List<NewsMenuData> dataList;
	private MenuAdapter adapter;
	
	private View view;

	/**
	 * 外部从网络取回数据后，由外部调用
	 * 
	 * @param newsData
	 */
	public void setData(NewsData newsData)
	{
			dataList = newsData.data;
			adapter = new MenuAdapter();
			lv_left_menu.setAdapter(adapter);
		
	}

	class MenuAdapter extends BaseAdapter
	{

		@Override
		public int getCount()
		{
			// TODO Auto-generated method stub
			return dataList.size();
		}

		@Override
		public Object getItem(int position)
		{
			// TODO Auto-generated method stub
			return dataList.get(position);
		}

		@Override
		public long getItemId(int position)
		{
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			View view = View.inflate(mActivity, R.layout.item_menu_list, null);
			TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
			String text = dataList.get(position).title;
			tv_title.setText(text);

			// 用过Enable属性设置样式
			if (position == currentPos)
			{
				tv_title.setEnabled(true);

			} else
			{
				tv_title.setEnabled(false);
			}

			return view;
		}

	}
}
