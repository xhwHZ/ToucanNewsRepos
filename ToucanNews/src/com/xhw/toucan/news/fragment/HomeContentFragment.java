package com.xhw.toucan.news.fragment;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xhw.toucan.news.R;
import com.xhw.toucan.news.base.BasePager;
import com.xhw.toucan.news.base.impl.AuthorPager;
import com.xhw.toucan.news.base.impl.NewsCenterPager;
import com.xhw.toucan.news.base.impl.SettingPager;

/**
 * 主页内容
 * @author admin
 *
 */
public class HomeContentFragment extends BaseFragment
{

	private List<BasePager> pageList=new ArrayList<BasePager>();
	
	@ViewInject(R.id.rg_group)
	private RadioGroup rg_group;
	
	@ViewInject(R.id.vp_content)
	private ViewPager vp_content;
	
	@Override
	public View initView()
	{
		View view = View.inflate(mActivity, R.layout.fragment_home_content, null);
		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	public void initData()
	{
		//设置默认选中首页
		rg_group.check(R.id.rb_news);
		//初始化5个页面
		//pageList.add(new HomePager(mActivity));
		pageList.add(new NewsCenterPager(mActivity));
		pageList.add(new AuthorPager(mActivity));
		pageList.add(new SettingPager(mActivity));
		ContentAdapter adapter=new ContentAdapter();
		vp_content.setAdapter(adapter);
		//手动初始化首页数据
		pageList.get(0).initData();
	}
	
	@Override
	public void initListener()
	{
		//监听RadioGroup的选择事件
		rg_group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId)
			{
				int index=0;
				switch (checkedId)
				{
//				case R.id.rb_home:
//					index=0;
//					break;
				case R.id.rb_news:
					index=0;
					break;
				case R.id.rb_gov:
					index=1;
					break;
				case R.id.rb_setting:
					index=2;
					break;
				}
				//false去掉切换页面的动画
				vp_content.setCurrentItem(index,false);
				//初始化数据
				pageList.get(index).initData();
			}
		});
	}
	
	/**
	 * 获取ViewPager当前索引
	 * @return
	 */
	public int getViewPagerCurIndex()
	{
		return vp_content.getCurrentItem();
	}
	
	/**
	 * 获取新闻中心页面
	 * @return
	 */
	public NewsCenterPager getNewsCenterPager()
	{
		return (NewsCenterPager) pageList.get(0);
	}
	
	class ContentAdapter extends PagerAdapter
	{

		@Override
		public Object instantiateItem(ViewGroup container, int position)
		{
			BasePager pager=pageList.get(position);
			//pager.initData(); 不要放在这个地方，否则预加载机制可能导致bug
			container.addView(pager.baseView);
			return pager.baseView;
		}
		
		@Override
		public int getCount()
		{
			return pageList.size();
		}

		@Override
		public boolean isViewFromObject(View v, Object obj)
		{
			return v==obj;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object)
		{
			container.removeView((View) object);
		}
		
		 
	}
}
