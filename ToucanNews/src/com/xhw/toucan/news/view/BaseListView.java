package com.xhw.toucan.news.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xhw.toucan.news.R;
import com.xhw.toucan.news.application.BaseApplication;

public class BaseListView extends ListView
{

	public BaseListView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init();
	}

	public BaseListView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	public BaseListView(Context context)
	{
		super(context);
		init();
	}

	private void init()
	{
		// setSelector 点击显示的颜色
		// setCacheColorHint 拖拽的颜色
		// setDivider 每个条目的间隔的分割线
		this.setSelector(R.drawable.nothing); // 透明图片
		this.setCacheColorHint(R.drawable.nothing);
		this.setDivider(BaseApplication.getAppliction().getResources()
				.getDrawable(R.drawable.nothing));
	}
	
	//使得position的计数方式是以除开HeaderView来计数的
	@Override
	public void setOnItemClickListener(
			final android.widget.AdapterView.OnItemClickListener listener)
	{
		super.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				listener.onItemClick(parent, view, position-getHeaderViewsCount(), id);
			}
		});
	}

}
