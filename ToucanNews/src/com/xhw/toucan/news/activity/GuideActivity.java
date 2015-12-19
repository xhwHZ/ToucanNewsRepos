package com.xhw.toucan.news.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.xhw.toucan.news.R;
import com.xhw.toucan.news.utils.DimenUtils;
import com.xhw.toucan.news.utils.SPUtils;

public class GuideActivity extends Activity
{
	private ViewPager vp_guide;

	private LinearLayout ll_point_group;

	private Button btn_start;

	/**
	 * 红色圆点
	 */
	private View point_red;

	// 两个相邻灰点左边侧相距的距离
	private int grayPointDistance;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_guide);
		initView();
		initData();
		initListener();
	}

	// 引导页图片id
	private static final int[] imgIds = { R.drawable.guide_1,
			R.drawable.guide_2, R.drawable.guide_3 };

	// 引导页图片控件
	List<ImageView> imageViewList = new ArrayList<ImageView>();

	private GuideAdapter adapter;


	private void initView()
	{
		vp_guide = (ViewPager) this.findViewById(R.id.vp_guide);
		btn_start = (Button) this.findViewById(R.id.btn_start);
		point_red = this.findViewById(R.id.point_red);
		ll_point_group = (LinearLayout) this.findViewById(R.id.ll_point_group);
		// 用代码初始化ViewPage的item
		for (int i = 0; i < imgIds.length; i++)
		{
			ImageView imageView = new ImageView(this);
			imageView.setBackgroundResource(imgIds[i]);
			imageViewList.add(imageView);
		}

		// 初始化底部滑动点的灰点
		for (int i = 0; i < imgIds.length; i++)
		{
			View child = new View(this);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					DimenUtils.dp2px(this, 10), DimenUtils.dp2px(this, 10));
			if (i > 0)
			{
				params.leftMargin = DimenUtils.dp2px(this, 10);
			}
			child.setLayoutParams(params);
			child.setBackgroundResource(R.drawable.shape_point_gray);
			ll_point_group.addView(child);
		}

	}

	private void initData()
	{
		adapter = new GuideAdapter();
		vp_guide.setAdapter(adapter);
	}

	private void initListener()
	{
		/**
		 * 下方放置原点布局的布局完成时的监听
		 */
		ll_point_group.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					/**
					 * 当layout()结束后会回调此方法
					 */
					@Override
					public void onGlobalLayout()
					{
						// 获取两个相邻灰点左边侧相距的距离
						grayPointDistance = ll_point_group.getChildAt(1)
								.getLeft()
								- ll_point_group.getChildAt(0).getLeft();
						// 没用了，删掉把
						ll_point_group.getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
					}
				});

		/**
		 * ViewPager的滑动监听
		 */
		vp_guide.setOnPageChangeListener(new OnPageChangeListener() {

			/**
			 * 滑动事件 position 当前页面的索引 positionOffset 当前页面移动的百分比 [0,1)
			 * 移动了100%就到下个下面去了 positionOffsetPixels 当前页面移动的像素
			 * 如果移动到了下一个页面，position加1，positionOffset和positionOffsetPixels清零
			 */
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels)
			{
				
		
				int left = (int) (grayPointDistance * positionOffset)
						+ position * grayPointDistance;
				// 不能用layout方法，位置会重置回去，原因不明
				// point_red.layout(left, 0, left+point_red.getWidth(),
				// point_red.getHeight());
				RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) point_red
						.getLayoutParams();
				layoutParams.leftMargin = left;
				point_red.setLayoutParams(layoutParams);
			}

			/**
			 * 被选中事件
			 */
			@Override
			public void onPageSelected(int position)
			{
				//最后一页的按钮
				if(position==imgIds.length-1)
				{
					btn_start.setVisibility(View.VISIBLE);
				}else
				{
					btn_start.setVisibility(View.INVISIBLE);
					
				}
				
			}

			/**
			 * 滑动状态改变
			 */
			@Override
			public void onPageScrollStateChanged(int state)
			{

			}
		});
		
		//开始体验
		btn_start.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v)
			{
				startActivity(new Intent(GuideActivity.this,HomeActivity.class));
				SPUtils.setBoolean(GuideActivity.this, "showGuide", false);
				finish();
			}
		});
		
	}

	class GuideAdapter extends PagerAdapter
	{

		@Override
		public int getCount()
		{
			return imageViewList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object obj)
		{
			return view == obj;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object)
		{
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position)
		{
			container.addView(imageViewList.get(position));
			return imageViewList.get(position);
		}

	}
}
