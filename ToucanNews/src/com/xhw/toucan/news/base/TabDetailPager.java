package com.xhw.toucan.news.base;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.CirclePageIndicator;
import com.xhw.toucan.news.R;
import com.xhw.toucan.news.activity.NewsDetailActivity;
import com.xhw.toucan.news.domain.NewsData.NewsTabData;
import com.xhw.toucan.news.domain.TabData;
import com.xhw.toucan.news.domain.TabData.TabNewsData;
import com.xhw.toucan.news.domain.TabData.TopNewsData;
import com.xhw.toucan.news.global.GlobalContants;
import com.xhw.toucan.news.utils.BitmapHelper;
import com.xhw.toucan.news.utils.CacheUtils;
import com.xhw.toucan.news.utils.SPUtils;
import com.xhw.toucan.news.view.RefreshListView;
import com.xhw.toucan.news.view.RefreshListView.OnRefreshListener;

public class TabDetailPager extends BaseMenuDetailPager implements
		OnPageChangeListener
{

	public TabDetailPager(Activity activity)
	{
		super(activity);
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg)
		{
			if (vp_news.getAdapter() != null)
			{
				int currentIndex = vp_news.getCurrentItem();
				int nextIndex = currentIndex + 1;

				if (nextIndex == vp_news.getAdapter().getCount())
				{
					nextIndex = 0;
				}
				vp_news.setCurrentItem(nextIndex);
			}
			handler.sendMessageDelayed(Message.obtain(), 3000);
		}

	};

	@Override
	public View initView()
	{
		View view = View.inflate(mActivity, R.layout.tab_detail_pager, null);
		View headerView = View.inflate(mActivity, R.layout.list_header_topnews,
				null);
		ViewUtils.inject(this, view);
		ViewUtils.inject(this, headerView);
		// headerview方式添加轮播图
		lv_list.addHeaderView(headerView);
		// 下拉刷新监听
		lv_list.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh()
			{
				getDataFromServer();
			}

			@Override
			public void onLoadingMore()
			{
				if (moreUrl == null)
				{
					Toast.makeText(mActivity, "已经是最后一页", 0).show();
					lv_list.completeLoadingMore();
				} else
				{
					getMoreDataFromServer();
				}
			}
		});

		// listview点击侦听
		lv_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				RefreshListView rl=(RefreshListView) parent;
				if(rl.getCurrentState()==RefreshListView.STATE_REFRESHING)
				{
					return;
				}
				
				String ids = SPUtils.getString(mActivity, "read_ids", "");
				String currentId = newsList.get(position).id;
				if (!ids.contains(currentId))
				{
					ids += currentId + ",";
					SPUtils.setString(mActivity, "read_ids", ids);
				}
				// 改变点击后的样式
				// adapter.notifyDataSetChanged();//getView见，性能差
				changeReadState(view);

				// 页面跳转
				Intent intent = new Intent(mActivity, NewsDetailActivity.class);
				// 携带url过去
				intent.putExtra("url", newsList.get(position).url);
				mActivity.startActivity(intent);
			}
		});
		return view;
	}

	// 改变样式，标记已读状态
	protected void changeReadState(View view)
	{
		TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
		tv_title.setTextColor(Color.GRAY);
	}

	@ViewInject(R.id.vp_news)
	private ViewPager vp_news;

	@ViewInject(R.id.tv_title)
	private TextView tv_title;

	@ViewInject(R.id.lv_list)
	private RefreshListView lv_list;

	@ViewInject(R.id.indicator)
	private CirclePageIndicator indicator;

	private NewsTabData data;

	private TabData tabData;

	private List<TabNewsData> newsList;

	private String moreUrl;

	private NewsAdapter adapter;

	public void setTabDetailMenuItem(NewsTabData data)
	{
		this.data = data;
	}

	@Override
	public void initData()
	{
		String cache = CacheUtils.getCache(mActivity, GlobalContants.SERVER_URL
				+ data.url);
		if (!TextUtils.isEmpty(cache))
		{
			parseData(cache, false);
		}
		// 从服务器获取数据
		getDataFromServer();
		// 开始轮播
		handler.sendMessageDelayed(handler.obtainMessage(), 3000);
	}

	private void getDataFromServer()
	{
		// for 视觉效果
		final long startTime = System.currentTimeMillis();
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, GlobalContants.SERVER_URL + data.url,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(
							final ResponseInfo<String> responseInfo)
					{
						long endTime = System.currentTimeMillis();
						final long detalTime = endTime - startTime;
						new Thread() {
							public void run()
							{
								if (detalTime < 1300)
								{
									try
									{
										Thread.sleep(1300 - detalTime);
									} catch (InterruptedException e)
									{
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}

								mActivity.runOnUiThread(new Runnable() {

									@Override
									public void run()
									{
										String result = responseInfo.result;
										parseData(result, false);
										lv_list.completeRefresh(true);
										// 设置缓存
										CacheUtils.setCache(mActivity,
												GlobalContants.SERVER_URL
														+ data.url, result);
									}
								});
							}
						}.start();

					}

					@Override
					public void onFailure(HttpException error, String msg)
					{
						Toast.makeText(mActivity, "网络请求失败", 0).show();
						lv_list.completeRefresh(false);
					}
				});
	}

	// 加载更多的数据
	private void getMoreDataFromServer()
	{
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, moreUrl, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				String result = responseInfo.result;
				parseData(result, true);
				lv_list.completeLoadingMore();
			}

			@Override
			public void onFailure(HttpException error, String msg)
			{
				Toast.makeText(mActivity, "网络请求失败", 0).show();
				lv_list.completeLoadingMore();
			}
		});
	}

	// 解析json数据
	protected void parseData(String result, boolean isLoadingMore)
	{
		Gson gson = new Gson();
		tabData = gson.fromJson(result, TabData.class);
		// 更多页面的逻辑
		String more = tabData.data.more;
		if (TextUtils.isEmpty(more))
		{
			moreUrl = null;
		} else
		{
			moreUrl = GlobalContants.SERVER_URL + more;
		}
		vp_news.setAdapter(new TopNewsAdapter());
		// 初始化轮播图标题
		String title = tabData.data.topnews.get(0).title;
		tv_title.setText(title);
		indicator.setOnPageChangeListener(this);// 换页换标题
		// 小圆点
		indicator.setViewPager(vp_news);
		indicator.setSnap(true);// 快照显示，一跳一跳显示
		indicator.onPageSelected(0);// 指针默认第0页

		if (!isLoadingMore)
		{
			newsList = tabData.data.news;

			if (newsList != null)
			{
				adapter = new NewsAdapter();
				lv_list.setAdapter(adapter);
			}
		} else
		{// 加载更多
			newsList.addAll(tabData.data.news);
			adapter.notifyDataSetChanged();
		}

	}

	class TopNewsAdapter extends PagerAdapter
	{
		private BitmapUtils bitmapUtils;

		public TopNewsAdapter()
		{
			bitmapUtils = BitmapHelper.getBitmapUtils();
			// 设置默认加载时显示的图片
			bitmapUtils
					.configDefaultLoadingImage(R.drawable.topnews_item_default);
		}

		@Override
		public int getCount()
		{
			return tabData.data.topnews.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object)
		{
			// TODO Auto-generated method stub
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position)
		{
			ImageView image = new ImageView(mActivity);
			image.setScaleType(ScaleType.FIT_XY);
			TopNewsData topNewsData = tabData.data.topnews.get(position);
			// BitmapUtils设置网络图片
			bitmapUtils.display(image, topNewsData.topimage);
			container.addView(image);
			// 轮播图的触摸监听
			image.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event)
				{
					switch (event.getAction())
					{
					case MotionEvent.ACTION_DOWN:
						handler.removeCallbacksAndMessages(null);// 移除Handler的所有任务
						break;
					case MotionEvent.ACTION_UP:
						// 开启轮播
						handler.sendMessageDelayed(Message.obtain(), 3000);
						break;
					case MotionEvent.ACTION_CANCEL:
						// 开启轮播
						handler.sendMessageDelayed(Message.obtain(), 3000);
						break;
					}
					return true;
				}
			});
			return image;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object)
		{
			container.removeView((View) object);
		}

	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels)
	{

	}

	@Override
	public void onPageSelected(int position)
	{
		// 设置轮播图标题
		String title = tabData.data.topnews.get(position).title;
		tv_title.setText(title);
	}

	@Override
	public void onPageScrollStateChanged(int state)
	{

	}

	/**
	 * 新闻列表适配器
	 * 
	 * @author admin
	 *
	 */
	class NewsAdapter extends BaseAdapter
	{
		private BitmapUtils bitmapUtils;

		public NewsAdapter()
		{
			bitmapUtils = new BitmapUtils(mActivity);
			bitmapUtils
					.configDefaultLoadingImage(R.drawable.pic_item_list_default);
		}

		@Override
		public int getCount()
		{
			return newsList.size();
		}

		@Override
		public TabNewsData getItem(int position)
		{
			return newsList.get(position);
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			View v = null;
			ViewHolder holder = null;
			if (convertView == null)
			{
				holder = new ViewHolder();
				v = View.inflate(mActivity, R.layout.item_news_list, null);
				holder.iv_pic = (ImageView) v.findViewById(R.id.iv_pic);
				holder.tv_title = (TextView) v.findViewById(R.id.tv_title);
				holder.tv_date = (TextView) v.findViewById(R.id.tv_date);
				v.setTag(holder);
			} else
			{
				v = convertView;
				holder = (ViewHolder) v.getTag();
			}

			TabNewsData item = getItem(position);
			bitmapUtils.display(holder.iv_pic, item.listimage);
			holder.tv_title.setText(item.title);
			holder.tv_date.setText(item.pubdate);

			String ids = SPUtils.getString(mActivity, "read_ids", "");
			if (ids.contains(item.id))// 被点击过
			{
				holder.tv_title.setTextColor(Color.GRAY);
			} else
			{
				holder.tv_title.setTextColor(Color.BLACK);
			}

			return v;
		}

		class ViewHolder
		{
			ImageView iv_pic;
			TextView tv_title;
			TextView tv_date;
		}

	}
}
