package com.xhw.toucan.news.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Fragment�Ļ���
 * @author admin
 *
 */
public abstract class BaseFragment extends Fragment
{
	public Activity mActivity;

	//Fragment����
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mActivity = getActivity();
	}
	
	//����Fragment�Ĳ���
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		return initView();
	}
	
	//��������Activity������
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		initData();
		initListener();
	}
	
	/**
	 * �������ʵ�ֵĳ�ʼ�����ֵķ���
	 */
	public abstract View initView();
	
	//��ʼ������
	public void initData(){}
	
	//��ʼ��������
	public void initListener(){}
}
