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
		//设置左边侧边栏
		this.setBehindContentView(R.layout.home_menu_left);
		//获取侧边栏对象
		SlidingMenu slidingMenu = this.getSlidingMenu();
		//设置为全屏滑动
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		//设置主界面预留宽度
		slidingMenu.setBehindOffset(DimenUtils.dp2px(this, 200));
		//初始化Fragment
		initFragment();
	}

	/**
	 * 切换侧边栏状态
	 */
	public void toggleSlidingMenu()
	{
		//获取侧边栏对象
		SlidingMenu slidingMenu = this.getSlidingMenu();
		slidingMenu.toggle();
	}
	
	/**
	 * 初始化Fragment
	 */
	private void initFragment()
	{
		FragmentManager fragmentManager = this.getSupportFragmentManager();
		FragmentTransaction trans = fragmentManager.beginTransaction();
		//设置TAG为了以后能找到这个Fragment
		trans.replace(R.id.fl_home_menu_left, new HomeMenuLeftFragment(), TAGH_HOME_MENU_LEFT);
		trans.replace(R.id.fl_home_content, new HomeContentFragment(),TAG_HOME_CONTENT);
		trans.commit();
		
	   //fragmentManager.findFragmentByTag(tag)
	}
	
	/**
	 * 获取主内容Fragment
	 */
	public HomeContentFragment getHomeContentFragment()
	{
		FragmentManager fm=this.getSupportFragmentManager();
		return (HomeContentFragment) fm.findFragmentByTag(TAG_HOME_CONTENT);
	}
	/**
	 * 获取侧边栏Fragment
	 */
	public HomeMenuLeftFragment getHomeMenuLeftFragment()
	{
		FragmentManager fm=this.getSupportFragmentManager();
		return (HomeMenuLeftFragment) fm.findFragmentByTag(TAGH_HOME_MENU_LEFT);
	}
	
	//数组长度表示要点击的次数
	long[] hits=new long[2];
	
	@Override
	public void onBackPressed()
	{
		System.arraycopy(hits, 1, hits, 0, hits.length-1);
		hits[hits.length-1]=SystemClock.uptimeMillis();//开机后开始计算的时间
		if(hits[0]>=(SystemClock.uptimeMillis()-2000)){//2秒连击时间
			  android.os.Process.killProcess(android.os.Process.myPid());    //获取PID 
			  System.exit(0);   //常规java、c#的标准退出法，返回值为0代表正常退出
		}else{
			Toast.makeText(this, "连续双击两次退出应用", 0).show();
		}
	}
}
