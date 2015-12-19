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
		// ������ʾ�汾��
		tv_version.setText("�汾��:" + APPUtils.getVersionName(this));
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
						// ���������ж�ȡ�ı�
						String text = responseInfo.result;
						VersionInfo versionInfo = new Gson().fromJson(text,
								VersionInfo.class);
						versionName = versionInfo.versionName;
						versionCode = versionInfo.versionCode;
						versionDes = versionInfo.versionDes;
						downloadUrl = versionInfo.downloadUrl;

						// ����������еİ汾�Ŵ��ڱ��صİ汾��
						if (versionCode > APPUtils
								.getVersionCode(SplashActivity.this))
						{
							// �и���
							// ���������Ի���
							 showUpdateDialog(); 
						} else
						{
							jump2NextPage();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg)
					{
						Toast.makeText(SplashActivity.this, "��������ʧ��!", 1).show();
						jump2NextPage();
					}
				});
	}

	/**
	 * SplashActivity�Ķ���
	 */
	private void startAnim()
	{
		AnimationSet set = new AnimationSet(false);// ������岹��
		// ��ת����
		RotateAnimation ra = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		ra.setDuration(1000);
		ra.setFillAfter(true);
		// ����
		ScaleAnimation sa = new ScaleAnimation(0, 1, 0, 1,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		sa.setDuration(1000);
		sa.setFillAfter(true);
		// ͸����
		AlphaAnimation aa = new AlphaAnimation(0, 1);
		aa.setDuration(2000);
		aa.setFillAfter(true);

		set.addAnimation(ra);
		set.addAnimation(sa);
		set.addAnimation(aa);

		rl_root.startAnimation(set);

		/**
		 * ���ö�������������������������������Ҳ
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
					// ������
					checkVersion();

				} else
				{
					jump2NextPage();
				}
			}
		});
	}

	/**
	 * ��ת����һ��ҳ��
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

	// ���������Ի���
		private void showUpdateDialog()
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("���°汾:" + versionName);
			builder.setMessage(versionDes);
			// �����û��㷵�أ��û����鲻�ã����Ƽ�ʹ��
			// builder.setCancelable(false);
			// ȷ�ϸ��°�ť
			builder.setPositiveButton("��������", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					// System.out.println("���̸��¡�����");
					downloadApk();
				}
			});
			// ȡ�����°�ť
			builder.setNegativeButton("�Ժ���˵", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					jump2NextPage();
				}
			});

			// ����ȡ���������û�������ؼ�ʱ����
			builder.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog)
				{
					jump2NextPage();// ����������
				}
			});

			builder.show();
		};

		// �����°汾��apk�ļ�
		public void downloadApk()
		{

			// �ж�SD���Ƿ����
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED))
			{
				// ��ʾ���ؽ��ȿ�
				tv_progress.setVisibility(View.VISIBLE);
				// ����Ŀ���ļ���
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
								// System.out.println("���ؽ��ȣ�" + current + "/" +
								// total);
								tv_progress.setText("���ؽ���:" + current * 100 / total
										+ "%");
							}

							@Override
							public void onSuccess(ResponseInfo<File> responseInfo)
							{
								Toast.makeText(SplashActivity.this, "���سɹ�", 0)
										.show();
								// ��ת��ϵͳ��װҳ��
								Intent intent = new Intent(Intent.ACTION_VIEW);
								intent.addCategory(Intent.CATEGORY_DEFAULT);
								intent.setDataAndType(
										Uri.fromFile(responseInfo.result),
										"application/vnd.android.package-archive");
								// startActivity(intent);
								// ����û�ȡ����װ���᷵�ؽ�����ص�onActivityResult����
								startActivityForResult(intent, 0);
								// ϵͳ���ذ�װҳ�棬��������ȡ����������setResult�������������������Ǹ�Activity��Ȼ��onActivityResult�������ã�����ȷ��ȴ�������setResult������������һ��Activity��onActivityResult�������ᴥ�������²⣩
							}

							@Override
							public void onFailure(HttpException err, String msg)
							{
								Toast.makeText(SplashActivity.this, "����ʧ��!", 0)
										.show();
								jump2NextPage();
							}
						});
			} else
			{
				Toast.makeText(SplashActivity.this, "û���ҵ�SD��", 0).show();
				jump2NextPage();
			}

		}

		// �û�ȡ����װ���ص���ҳ��
		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data)
		{
			super.onActivityResult(requestCode, resultCode, data);
			jump2NextPage();
		}
}
