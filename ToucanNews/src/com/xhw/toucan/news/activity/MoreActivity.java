package com.xhw.toucan.news.activity;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;
import com.viewpagerindicator.CirclePageIndicator;
import com.xhw.toucan.news.R;
import com.xhw.toucan.news.domain.SkillJsonInfo;
import com.xhw.toucan.news.domain.SkillJsonInfo.SkillInfo;
import com.xhw.toucan.news.global.GlobalContants;
import com.xhw.toucan.news.utils.BitmapHelper;
import com.xhw.toucan.news.utils.SPUtils;
import com.xhw.toucan.news.utils.ThreadUtils;
import com.xhw.toucan.news.view.BaseListView;
import com.xhw.toucan.news.view.MoreItemView;

public class MoreActivity extends Activity
{
	@ViewInject(R.id.fl_root)
	private FrameLayout fl_root;

	@ViewInject(R.id.progressBar)
	private ProgressBar progressBar;

	@ViewInject(R.id.rl_retry)
	private RelativeLayout rl_retry;

	@ViewInject(R.id.btn_retry)
	private Button btn_retry;

	private List<String> headImgList;

	private List<SkillInfo> skillList;

	private BaseListView listView;

	private ViewPager viewPager;

	private TextView tv_title;

	private CirclePageIndicator indicator;

	private BitmapUtils bitmapUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_more);
		bitmapUtils = BitmapHelper.getBitmapUtils();
		initView();
		initData();
		initListener();
	}

	private void initView()
	{
		ViewUtils.inject(this);
	}

	private void initData()
	{
		String skillsCache = SPUtils.getString(this, "skillsCache", null);
		if (TextUtils.isEmpty(skillsCache))
		{
			getDataFromServer();
		} else
		{
			parseJson(skillsCache);
		}
	}

	private void parseJson(String json)
	{
		Gson gson = new Gson();
		SkillJsonInfo skillJsonInfo = gson.fromJson(json, SkillJsonInfo.class);
		headImgList = skillJsonInfo.headImgList;
		skillList = skillJsonInfo.skillList;
		rl_retry.setVisibility(View.INVISIBLE);
		progressBar.setVisibility(View.INVISIBLE);
		listView = new BaseListView(this);
		initListViewHead();
		MoreAdapter adapter = new MoreAdapter();
		listView.setAdapter(adapter);
		fl_root.removeAllViews();
		fl_root.addView(listView);

	}

	private void initListViewHead()
	{
		View listViewHead = View.inflate(this, R.layout.head_more, null);
		viewPager = (ViewPager) listViewHead.findViewById(R.id.viewPager);
		tv_title = (TextView) listViewHead.findViewById(R.id.tv_title);
		indicator = (CirclePageIndicator) listViewHead
				.findViewById(R.id.indicator);

		viewPager.setAdapter(new MorePageAdapter());
		// 小圆点
		indicator.setViewPager(viewPager);
		indicator.setSnap(true);// 快照显示，一跳一跳显示
		indicator.onPageSelected(0);// 指针默认第0页

		autoTask = new AutoTask();
		autoTask.start();// 开始轮播

		listView.addHeaderView(listViewHead);

	}

	private void getDataFromServer()
	{
		progressBar.setVisibility(View.VISIBLE);
		rl_retry.setVisibility(View.INVISIBLE);
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, GlobalContants.SERVER_URL
				+ "/skills.json", new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				String json = responseInfo.result;
				SPUtils.setString(MoreActivity.this, "skillsCache", json);
				parseJson(json);
			}

			@Override
			public void onFailure(HttpException error, String msg)
			{
				Toast.makeText(MoreActivity.this, "网络连接失败", 0).show();
				progressBar.setVisibility(View.INVISIBLE);
				rl_retry.setVisibility(View.VISIBLE);

			}
		});
	}

	private void initListener()
	{
		btn_retry.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				getDataFromServer();
			}
		});

	}

	private class MoreAdapter extends BaseAdapter
	{

		@Override
		public int getCount()
		{
			return skillList.size();
		}

		@Override
		public SkillInfo getItem(int position)
		{
			return skillList.get(position);
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			bitmapUtils.configDefaultLoadingImage(R.drawable.news_pic_default);
			MoreItemView view=null;
			if (convertView == null)
			{
				view = new MoreItemView(MoreActivity.this);
			} else
			{
				view = (MoreItemView) convertView;
			}
		final	MoreItemView self = view.getSelf();
			convertView=view;
			SkillInfo skillInfo = getItem(position);
			view.setTitle(skillInfo.skillTitle);
			bitmapUtils.display(view.getImageView(), GlobalContants.SERVER_URL
					+ skillInfo.iconUrl);
			ValueAnimator animator=ValueAnimator.ofInt(0,skillInfo.skillValue);
			animator.addUpdateListener(new AnimatorUpdateListener() {
				
				@Override
				public void onAnimationUpdate(ValueAnimator animation)
				{
					int value=(Integer) animation.getAnimatedValue();
					self.setProcess(value);
				}
			});
			animator.setDuration(1000);
			animator.start();
			
			return view;
		}

	}

	private class MorePageAdapter extends PagerAdapter
	{

		private List<ImageView> imgViewList=new LinkedList<ImageView>();
		
		@Override
		public int getCount()
		{
			return headImgList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object)
		{
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position)
		{
			ImageView imageView = null;
			if (imgViewList.size() > 0)
			{
				imageView=imgViewList.remove(0);
			} else
			{
				imageView = new ImageView(MoreActivity.this);
			}
			imageView.setScaleType(ScaleType.FIT_XY);
			bitmapUtils.display(imageView, headImgList.get(position));
			container.addView(imageView);
			
			viewPager.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event)
				{
					switch (event.getAction())
					{
					case MotionEvent.ACTION_DOWN:
						autoTask.stop();
						break;
					case MotionEvent.ACTION_CANCEL:
					case MotionEvent.ACTION_UP:
						autoTask.start();
						break;
					}
					return false;
				}
			});
			
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object)
		{
			imgViewList.add((ImageView) object);
			container.removeView((View) object);
		}

	}

	// 是否开始轮播的标记
	private boolean flag = false;

	private AutoTask autoTask;

	/**
	 * 自动轮播任务类
	 * 
	 * @author admin
	 *
	 */
	private class AutoTask implements Runnable
	{

		@Override
		public void run()
		{
			if (flag)
			{
				int currentItem = viewPager.getCurrentItem();
				int nextIndex;
				if(currentItem>=viewPager.getAdapter().getCount()-1)
				{
					nextIndex=0;
				}else{
					nextIndex=currentItem=currentItem+1;
				}
				viewPager.setCurrentItem(nextIndex,false);
				ThreadUtils.postDelayed(this, 2000);
			}
		}

		// 外部调用这个方法，启动run方法
		public void start()
		{
			if (!flag)
			{
				flag = true;
				ThreadUtils.postDelayed(this, 2000);
			}
		}

		public void stop()
		{
			if (flag)
			{
				flag = false;
				ThreadUtils.cancel(this);
			}
		}
	}

}
