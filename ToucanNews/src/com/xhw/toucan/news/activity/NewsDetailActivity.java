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
		// ����webview��javascript����
		settings.setJavaScriptEnabled(true);
		// ��ʾ�Ŵ���С��ť
		settings.setBuiltInZoomControls(true);
		// ����˫������
		settings.setUseWideViewPort(true);
		// ����webview�ļ���
		wv_web.setWebViewClient(new WebViewClient() {

			// ��ҳ��ʼ����
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon)
			{
				super.onPageStarted(view, url, favicon);
				// ��ʾprogressBar
				progressBar.setVisibility(View.VISIBLE);
			}

			// ��ҳ���ؽ���
			@Override
			public void onPageFinished(WebView view, String url)
			{
				super.onPageFinished(view, url);
				// ����progressBar
				progressBar.setVisibility(View.GONE);
			}

			// ������ת��url�����ڴ˷����б��ص�
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url)
			{
				// �е�ǰ��webview���ƿ�ҳ����ת��������ת�������
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
		 //��������
		oks.setTheme(OnekeyShareTheme.SKYBLUE);
		 //�ر�sso��Ȩ
		 oks.disableSSOWhenAuthorize(); 

		// ����ʱNotification��ͼ�������  2.5.9�Ժ�İ汾�����ô˷���
		 //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
		 // title���⣬ӡ��ʼǡ����䡢��Ϣ��΢�š���������QQ�ռ�ʹ��
		 oks.setTitle("���Ǳ���");
		 // titleUrl�Ǳ�����������ӣ�������������QQ�ռ�ʹ��
		 oks.setTitleUrl("http://sharesdk.cn");
		 // text�Ƿ����ı�������ƽ̨����Ҫ����ֶ�
		 oks.setText("���Ƿ����ı�");
		 // imagePath��ͼƬ�ı���·����Linked-In�����ƽ̨��֧�ִ˲���
		 //oks.setImagePath("/sdcard/test.jpg");//ȷ��SDcard������ڴ���ͼƬ
		 oks.setImageUrl("http://pic.nipic.com/2007-11-09/200711912453162_2.jpg");
		 // url����΢�ţ��������Ѻ�����Ȧ����ʹ��
		 oks.setUrl("http://sharesdk.cn");
		 // comment���Ҷ�������������ۣ�������������QQ�ռ�ʹ��
		 oks.setComment("���ǲ��������ı�");
		 // site�Ƿ�������ݵ���վ���ƣ�����QQ�ռ�ʹ��
		 oks.setSite(getString(R.string.app_name));
		 // siteUrl�Ƿ�������ݵ���վ��ַ������QQ�ռ�ʹ��
		 oks.setSiteUrl("http://sharesdk.cn");

		// ��������GUI
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

	// ѡ�������С�Ի���
	private void showTextSizeDialog()
	{
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("��������");
		int defSize = SPUtils.getInt(this, "textsize", 2);
		String[] items = { "��������", "�������", "��������", "С������", "��С����" };
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
		builder.setNegativeButton("ȡ��", null);
		builder.show();
	}
}
