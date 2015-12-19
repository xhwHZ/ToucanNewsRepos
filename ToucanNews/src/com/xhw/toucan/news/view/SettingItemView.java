package com.xhw.toucan.news.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xhw.toucan.news.R;

/**
 * �������ĵ��Զ�����Ͽؼ�
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
		// ��ȡָ�����Զ�������(�����������ƻ�ȡ����ֵ)
		title = attrs.getAttributeValue(NAMESPACE, "title");
		desc_on = attrs.getAttributeValue(NAMESPACE, "desc_on");
		desc_off = attrs.getAttributeValue(NAMESPACE, "desc_off");
		initView();
	}

	public SettingItemView(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
		// ����
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
	 * ��ʼ������
	 */
	private void initView()
	{
		// ���Զ���Ĳ����ļ���䵽��ǰ��SettingItemView
		View.inflate(this.getContext(), R.layout.view_setting_item, this);
		tv_title = (TextView) this.findViewById(R.id.tv_title);
		tv_desc = (TextView) this.findViewById(R.id.tv_desc);
		cb_status = (CheckBox) this.findViewById(R.id.cb_status);
		// ���ñ���
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
	 * ���ع�ѡ״̬
	 * 
	 * @return
	 */
	public boolean isChecked()
	{
		return cb_status.isChecked();
	}

	/**
	 * ���ù�ѡ״̬
	 * 
	 * @param status
	 */
	public void setChecked(boolean status)
	{
		cb_status.setChecked(status);
		// ����ѡ���״̬�������ı�����
		if (status)
		{
			setDesc(desc_on);
		} else
		{
			setDesc(desc_off);
		}
	}
}
