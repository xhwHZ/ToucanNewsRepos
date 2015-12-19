package com.xhw.toucan.news.activity;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xhw.toucan.news.R;
import com.xhw.toucan.news.domain.VersionInfo;
import com.xhw.toucan.news.utils.APPUtils;
import com.xhw.toucan.news.utils.SPUtils;

public class SplashActivity extends Activity
{

	@ViewInject(R.id.rl_root)
	private RelativeLayout rl_root;

	@ViewInject(R.id.tv_version)
	private TextView tv_version;

	@ViewInject(R.id.tv_progress)
	private TextView tv_progress;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		ViewUtils.inject(this);
		// 设置显示版本号
		tv_version.setText("版本号:" + APPUtils.getVersionName(this));
		startAnim();
	}

	private String versionName;
	private int versionCode;
	private String versionDes;
	private String downloadUrl;
	
	private void checkVersion()
	{
		HttpUtils httpUtils = new HttpUtils(5000);
		httpUtils.send(HttpMethod.GET,
				"http://121.42.156.91:8080/ToucansNews/update.json",
				new RequestCallBack<String>() {

			

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo)
					{
						// 从输入流中读取文本
						String text = responseInfo.result;
						VersionInfo versionInfo = new Gson().fromJson(text,
								VersionInfo.class);
						versionName = versionInfo.versionName;
						versionCode = versionInfo.versionCode;
						versionDes = versionInfo.versionDes;
						downloadUrl = versionInfo.downloadUrl;

						// 如果服务器中的版本号大于本地的版本号
						if (versionCode > APPUtils
								.getVersionCode(SplashActivity.this))
						{
							// 有更新
							// 弹出升级对话框
							 showUpdateDialog(); 
						} else
						{
							jump2NextPage();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg)
					{
						Toast.makeText(SplashActivity.this, "网络连接失败!", 1).show();
						jump2NextPage();
					}
				});
	}

	/**
	 * SplashActivity的动画
	 */
	private void startAnim()
	{
		AnimationSet set = new AnimationSet(false);// 不共享插补器
		// 旋转动画
		RotateAnimation ra = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		ra.setDuration(1000);
		ra.setFillAfter(true);
		// 缩放
		ScaleAnimation sa = new ScaleAnimation(0, 1, 0, 1,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		sa.setDuration(1000);
		sa.setFillAfter(true);
		// 透明度
		AlphaAnimation aa = new AlphaAnimation(0, 1);
		aa.setDuration(2000);
		aa.setFillAfter(true);

		set.addAnimation(ra);
		set.addAnimation(sa);
		set.addAnimation(aa);

		rl_root.startAnimation(set);

		/**
		 * 设置动画监听，动画结束后，跳到新手引导也
		 */
		set.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation)
			{

			}

			@Override
			public void onAnimationRepeat(Animation animation)
			{

			}

			@Override
			public void onAnimationEnd(Animation animation)
			{
				boolean status = SPUtils.getBoolean(SplashActivity.this,
						"auto update", false);
				if (status)
				{
					// 检测更新
					checkVersion();

				} else
				{
					jump2NextPage();
				}
			}
		});
	}

	/**
	 * 跳转到下一个页面
	 */
	private void jump2NextPage()
	{
		boolean showGuide = SPUtils.getBoolean(this, "showGuide", true); // sp.getBoolean("showGuide",
																			// true);
		if (showGuide)
		{
			startActivity(new Intent(SplashActivity.this, GuideActivity.class));
		} else
		{
			startActivity(new Intent(SplashActivity.this, HomeActivity.class));
		}
		finish();
	}

	// 弹出升级对话框
		private void showUpdateDialog()
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("最新版本:" + versionName);
			builder.setMessage(versionDes);
			// 不让用户点返回，用户体验不好，不推荐使用
			// builder.setCancelable(false);
			// 确认更新按钮
			builder.setPositiveButton("立即更新", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					// System.out.println("立刻更新。。。");
					downloadApk();
				}
			});
			// 取消更新按钮
			builder.setNegativeButton("以后再说", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					jump2NextPage();
				}
			});

			// 设置取消监听，用户点击返回键时触发
			builder.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog)
				{
					jump2NextPage();// 跳到主界面
				}
			});

			builder.show();
		};

		// 下载新版本的apk文件
		public void downloadApk()
		{

			// 判断SD卡是否存在
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED))
			{
				// 显示下载进度框
				tv_progress.setVisibility(View.VISIBLE);
				// 下载目标文件夹
				File appDir = new File(Environment.getExternalStorageDirectory(),
						"BigMouseBird");
				if (!appDir.exists())
				{
					appDir.mkdirs();
				}
				HttpUtils httpUtils = new HttpUtils();
				httpUtils.download(this.downloadUrl, appDir + "/update.apk",
						new RequestCallBack<File>() {

							@Override
							public void onLoading(long total, long current,
									boolean isUploading)
							{
								super.onLoading(total, current, isUploading);
								// System.out.println("下载进度：" + current + "/" +
								// total);
								tv_progress.setText("下载进度:" + current * 100 / total
										+ "%");
							}

							@Override
							public void onSuccess(ResponseInfo<File> responseInfo)
							{
								Toast.makeText(SplashActivity.this, "下载成功", 0)
										.show();
								// 跳转到系统安装页面
								Intent intent = new Intent(Intent.ACTION_VIEW);
								intent.addCategory(Intent.CATEGORY_DEFAULT);
								intent.setDataAndType(
										Uri.fromFile(responseInfo.result),
										"application/vnd.android.package-archive");
								// startActivity(intent);
								// 如果用户取消安装，会返回结果，回调onActivityResult方法
								startActivityForResult(intent, 0);
								// 系统下载安装页面，如果点击了取消，则会调用setResult方法，激活启动它的那个Activity，然后onActivityResult方法调用，而点确定却不会调用setResult方法，所以上一个Activity的onActivityResult方法不会触发，（猜测）
							}

							@Override
							public void onFailure(HttpException err, String msg)
							{
								Toast.makeText(SplashActivity.this, "下载失败!", 0)
										.show();
								jump2NextPage();
							}
						});
			} else
			{
				Toast.makeText(SplashActivity.this, "没有找到SD卡", 0).show();
				jump2NextPage();
			}

		}

		// 用户取消安装，回到主页面
		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data)
		{
			super.onActivityResult(requestCode, resultCode, data);
			jump2NextPage();
		}
}
