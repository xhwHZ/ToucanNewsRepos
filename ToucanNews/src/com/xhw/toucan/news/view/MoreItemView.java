package com.xhw.toucan.news.view;

import com.xhw.toucan.news.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MoreItemView extends FrameLayout
{

	private String NAMESPACE = "http://schemas.android.com/apk/res/com.xhw.toucan.news";
	private int iconId;
	private String skill_title;
	private int skill_value;
	private ImageView iv_icon;
	private TextView tv_title;
	private ProgressBar pb;
	private TextView tv_value;
	

	public MoreItemView getSelf()
	{
		return this;
	}
	
//	  xhw:skill_icon="@drawable/ic_contact"
//		        xhw:skill_title="javascript"
//		        xhw:skill_value="80"
//	
	public MoreItemView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		iconId = attrs.getAttributeResourceValue(NAMESPACE, "skill_icon", 0);
		skill_title = attrs.getAttributeValue(NAMESPACE, "skill_title");
		skill_value = attrs.getAttributeIntValue(NAMESPACE, "skill_value", 0);
		initView();
	}



	public MoreItemView(Context context, AttributeSet attrs)
	{
		this(context, attrs,0);
	}

	public MoreItemView(Context context)
	{
		super(context);
		initView();
	}
	
	private void initView()
	{
		View.inflate(getContext(), R.layout.item_more, this);
		iv_icon = (ImageView) this.findViewById(R.id.iv_icon);
		tv_title = (TextView) this.findViewById(R.id.tv_title);
		pb = (ProgressBar) this.findViewById(R.id.pb);
		tv_value = (TextView) this.findViewById(R.id.tv_value);
		setTitle(skill_title);
		setIcon(iconId);
		setProcess(skill_value);
	}
	
	public void setTitle(String title)
	{
		tv_title.setText(title);
	}
	
	public ImageView getImageView()
	{
		return iv_icon;
	}
	
	public void setIcon(int resId)
	{
		if(resId==0)
		{
			return;
		}
		iv_icon.setBackgroundResource(resId);
	}
	
	public void setProcess(int value)
	{
		pb.setProgress(value);
		tv_value.setText(value+"%");
	}
	
}
