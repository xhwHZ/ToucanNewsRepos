package com.xhw.toucan.news.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Fragment的基类
 * @author admin
 *
 */
public abstract class BaseFragment extends Fragment
{
	public Activity mActivity;

	//Fragment创建
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mActivity = getActivity();
	}
	
	//处理Fragment的布局
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		return initView();
	}
	
	//所依附的Activity被创建
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		initData();
		initListener();
	}
	
	/**
	 * 子类必须实现的初始化布局的方法
	 */
	public abstract View initView();
	
	//初始化数据
	public void initData(){}
	
	//初始化监听器
	public void initListener(){}
}
