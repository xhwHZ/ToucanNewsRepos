package com.xhw.toucan.news.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xhw.toucan.news.R;
import com.xhw.toucan.news.utils.SPUtils;

public class NewsDetailActivity extends Activity implements OnClickListener
{
	@ViewInject(R.id.wv_web)
	private WebView wv_web;

	@ViewInject(R.id.ib_back)
	private ImageButton ib_back;

	@ViewInject(R.id.ib_share)
	private ImageButton ib_share;

	@ViewInject(R.id.ib_textsize)
	private ImageButton ib_textsize;

	@ViewInject(R.id.progressBar)
	private ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		initView();
		initData();
		initListener();
	}

	private void initView()
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_newsdetail);
		ViewUtils.inject(this);
		WebSettings settings = wv_web.getSettings();
		// 开启webview的javascript功能
		settings.setJavaScriptEnabled(true);
		// 显示放大缩小按钮
		settings.setBuiltInZoomControls(true);
		// 设置双击缩放
		settings.setUseWideViewPort(true);
		// 设置webview的监听
		wv_web.setWebViewClient(new WebViewClient() {

			// 网页开始加载
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon)
			{
				super.onPageStarted(view, url, favicon);
				// 显示progressBar
				progressBar.setVisibility(View.VISIBLE);
			}

			// 网页加载结束
			@Override
			public void onPageFinished(WebView view, String url)
			{
				super.onPageFinished(view, url);
				// 隐藏progressBar
				progressBar.setVisibility(View.GONE);
			}

			// 所有跳转的url都会在此方法中被回调
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url)
			{
				// 有当前的webview来掌控页面跳转，而不是转到浏览器
				view.loadUrl(url);
				return true;
			}
		});
	}

	private void initData()
	{
		String url = getIntent().getStringExtra("url");
		wv_web.loadUrl(url);
	}

	private void initListener()
	{
		ib_back.setOnClickListener(this);
		ib_textsize.setOnClickListener(this);
		ib_share.setOnClickListener(this);
	}

	private void showShare() {
		 ShareSDK.initSDK(this);
		 OnekeyShare oks = new OnekeyShare();
		 //设置主题
		oks.setTheme(OnekeyShareTheme.SKYBLUE);
		 //关闭sso授权
		 oks.disableSSOWhenAuthorize(); 

		// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
		 //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
		 // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		 oks.setTitle("我是标题");
		 // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		 oks.setTitleUrl("http://sharesdk.cn");
		 // text是分享文本，所有平台都需要这个字段
		 oks.setText("我是分享文本");
		 // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		 //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
		 oks.setImageUrl("http://pic.nipic.com/2007-11-09/200711912453162_2.jpg");
		 // url仅在微信（包括好友和朋友圈）中使用
		 oks.setUrl("http://sharesdk.cn");
		 // comment是我对这条分享的评论，仅在人人网和QQ空间使用
		 oks.setComment("我是测试评论文本");
		 // site是分享此内容的网站名称，仅在QQ空间使用
		 oks.setSite(getString(R.string.app_name));
		 // siteUrl是分享此内容的网站地址，仅在QQ空间使用
		 oks.setSiteUrl("http://sharesdk.cn");

		// 启动分享GUI
		 oks.show(this);
		 }
	
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.ib_back:
			finish();
			break;
		case R.id.ib_textsize:
			showTextSizeDialog();
			break;
		case R.id.ib_share:
			showShare();
			break;
		}
	}

	// 选择字体大小对话框
	private void showTextSizeDialog()
	{
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("字体设置");
		int defSize = SPUtils.getInt(this, "textsize", 2);
		String[] items = { "超大字体", "大号字体", "正常字体", "小号字体", "超小字体" };
		builder.setSingleChoiceItems(items, defSize,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						SPUtils.setInt(NewsDetailActivity.this, "textsize",
								which);
						WebSettings settings = wv_web.getSettings();
						switch (which)
						{
						case 0:
							settings.setTextSize(TextSize.LARGEST);
							break;
						case 1:
							settings.setTextSize(TextSize.LARGER);
							break;
						case 2:
							settings.setTextSize(TextSize.NORMAL);
							break;
						case 3:
							settings.setTextSize(TextSize.SMALLER);
							break;
						case 4:
							settings.setTextSize(TextSize.SMALLEST);
							break;

						}
						dialog.dismiss();
					}

				});
		builder.setNegativeButton("取消", null);
		builder.show();
	}
}
