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
		// setSelector �����ʾ����ɫ
		// setCacheColorHint ��ק����ɫ
		// setDivider ÿ����Ŀ�ļ���ķָ���
		this.setSelector(R.drawable.nothing); // ͸��ͼƬ
		this.setCacheColorHint(R.drawable.nothing);
		this.setDivider(BaseApplication.getAppliction().getResources()
				.getDrawable(R.drawable.nothing));
	}
	
	//ʹ��position�ļ�����ʽ���Գ���HeaderView��������
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
