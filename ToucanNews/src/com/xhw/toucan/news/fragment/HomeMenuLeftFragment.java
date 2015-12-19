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
 * �����Fragment(���ţ�ר�⣬���⣬����)
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

	// ��ǰѡ�е�λ��
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
				// ��ȡ�����ݵ�Fragment
				HomeContentFragment homeContentFragment = homeActivity
						.getHomeContentFragment();
				int index = homeContentFragment.getViewPagerCurIndex();
				// ��������ҳ�棬����Ӧ����¼�
				if (index != 0)
				{
					return;
				}

				// ˢ�µ�ǰѡ��λ��
				currentPos = position;
				// ��ʹgetView����ִ�У�ˢ��Menu��ʽ
				adapter.notifyDataSetChanged();

				// ������Content������
				setHomeContent(position);

				// �л�Slidmenu״̬
				toggleSlidingMenu();

			}
		});
	}

	// //�л�Slidingmenu״̬
	protected void toggleSlidingMenu()
	{
		HomeActivity homeActivity = (HomeActivity) mActivity;
		homeActivity.toggleSlidingMenu();
	}

	// ������Content������
	protected void setHomeContent(int position)
	{
		HomeActivity homeActivity = (HomeActivity) mActivity;
		// ��ȡ�����ݵ�Fragment
		HomeContentFragment homeContentFragment = homeActivity
				.getHomeContentFragment();
		// ��ȡ��������ҳ��
		NewsCenterPager newsCenterPager = homeContentFragment
				.getNewsCenterPager();
		newsCenterPager.setDetailPager(position);
	}

	private List<NewsMenuData> dataList;
	private MenuAdapter adapter;
	
	private View view;

	/**
	 * �ⲿ������ȡ�����ݺ����ⲿ����
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

			// �ù�Enable����������ʽ
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
