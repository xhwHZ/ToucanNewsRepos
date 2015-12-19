package com.xhw.toucan.news.activity;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.xhw.toucan.news.R;
import com.xhw.toucan.news.fragment.HomeContentFragment;
import com.xhw.toucan.news.fragment.HomeMenuLeftFragment;
import com.xhw.toucan.news.utils.DimenUtils;

public class HomeActivity extends SlidingFragmentActivity
{
	private final static String TAG_HOME_CONTENT="tag_home_content";
	
	private final static String TAGH_HOME_MENU_LEFT="tagh_home_menu_left";

	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_home_content);
		//������߲����
		this.setBehindContentView(R.layout.home_menu_left);
		//��ȡ���������
		SlidingMenu slidingMenu = this.getSlidingMenu();
		//����Ϊȫ������
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		//����������Ԥ�����
		slidingMenu.setBehindOffset(DimenUtils.dp2px(this, 200));
		//��ʼ��Fragment
		initFragment();
	}

	/**
	 * �л������״̬
	 */
	public void toggleSlidingMenu()
	{
		//��ȡ���������
		SlidingMenu slidingMenu = this.getSlidingMenu();
		slidingMenu.toggle();
	}
	
	/**
	 * ��ʼ��Fragment
	 */
	private void initFragment()
	{
		FragmentManager fragmentManager = this.getSupportFragmentManager();
		FragmentTransaction trans = fragmentManager.beginTransaction();
		//����TAGΪ���Ժ����ҵ����Fragment
		trans.replace(R.id.fl_home_menu_left, new HomeMenuLeftFragment(), TAGH_HOME_MENU_LEFT);
		trans.replace(R.id.fl_home_content, new HomeContentFragment(),TAG_HOME_CONTENT);
		trans.commit();
		
	   //fragmentManager.findFragmentByTag(tag)
	}
	
	/**
	 * ��ȡ������Fragment
	 */
	public HomeContentFragment getHomeContentFragment()
	{
		FragmentManager fm=this.getSupportFragmentManager();
		return (HomeContentFragment) fm.findFragmentByTag(TAG_HOME_CONTENT);
	}
	/**
	 * ��ȡ�����Fragment
	 */
	public HomeMenuLeftFragment getHomeMenuLeftFragment()
	{
		FragmentManager fm=this.getSupportFragmentManager();
		return (HomeMenuLeftFragment) fm.findFragmentByTag(TAGH_HOME_MENU_LEFT);
	}
	
	//���鳤�ȱ�ʾҪ����Ĵ���
	long[] hits=new long[2];
	
	@Override
	public void onBackPressed()
	{
		System.arraycopy(hits, 1, hits, 0, hits.length-1);
		hits[hits.length-1]=SystemClock.uptimeMillis();//������ʼ�����ʱ��
		if(hits[0]>=(SystemClock.uptimeMillis()-2000)){//2������ʱ��
			  android.os.Process.killProcess(android.os.Process.myPid());    //��ȡPID 
			  System.exit(0);   //����java��c#�ı�׼�˳���������ֵΪ0���������˳�
		}else{
			Toast.makeText(this, "����˫�������˳�Ӧ��", 0).show();
		}
	}
}
