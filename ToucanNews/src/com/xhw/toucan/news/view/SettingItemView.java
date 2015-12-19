package com.xhw.toucan.news.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xhw.toucan.news.R;

/**
 * 设置中心的自定义组合控件
 * 
 * @author admin
 *
 */
public class SettingItemView extends RelativeLayout
{

	private String NAMESPACE = "http://schemas.android.com/apk/res/com.xhw.toucan.news";

	public SettingItemView(Context context, AttributeSet attrs, int defStyle)
	{

		super(context, attrs, defStyle);
		// 获取指定的自定义属性(根据属性名称获取属性值)
		title = attrs.getAttributeValue(NAMESPACE, "title");
		desc_on = attrs.getAttributeValue(NAMESPACE, "desc_on");
		desc_off = attrs.getAttributeValue(NAMESPACE, "desc_off");
		initView();
	}

	public SettingItemView(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
		// 遍历
		// int attributeCount=attrs.getAttributeCount();
		// for(int i=0;i<attributeCount;i++){
		// String attributeName=attrs.getAttributeName(i);
		// String attributeValue=attrs.getAttributeValue(i);
		// System.out.println(attributeName+"="+attributeValue);
		// }
	}

	public SettingItemView(Context context)
	{
		super(context);
		initView();
	}

	private TextView tv_title;
	private TextView tv_desc;
	private CheckBox cb_status;
	private String title;
	private String desc_on;
	private String desc_off;

	/**
	 * 初始化布局
	 */
	private void initView()
	{
		// 将自定义的布局文件填充到当前的SettingItemView
		View.inflate(this.getContext(), R.layout.view_setting_item, this);
		tv_title = (TextView) this.findViewById(R.id.tv_title);
		tv_desc = (TextView) this.findViewById(R.id.tv_desc);
		cb_status = (CheckBox) this.findViewById(R.id.cb_status);
		// 设置标题
		setTitle(title);
	}

	public void setTitle(String title)
	{
		tv_title.setText(title);
	}

	public void setDesc(String desc)
	{
		tv_desc.setText(desc);
	}

	/**
	 * 返回勾选状态
	 * 
	 * @return
	 */
	public boolean isChecked()
	{
		return cb_status.isChecked();
	}

	/**
	 * 设置勾选状态
	 * 
	 * @param status
	 */
	public void setChecked(boolean status)
	{
		cb_status.setChecked(status);
		// 根据选择的状态，更新文本描述
		if (status)
		{
			setDesc(desc_on);
		} else
		{
			setDesc(desc_off);
		}
	}
}
