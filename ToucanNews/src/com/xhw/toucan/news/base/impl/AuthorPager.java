package com.xhw.toucan.news.base.impl;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.TypedValue;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;
import com.xhw.toucan.news.R;
import com.xhw.toucan.news.activity.MoreActivity;
import com.xhw.toucan.news.base.BasePager;
import com.xhw.toucan.news.listener.DefaultAnimatorListener;
import com.xhw.toucan.news.utils.APPUtils;
import com.xhw.toucan.news.utils.SPUtils;

public class AuthorPager extends BasePager implements OnClickListener
{

	private ImageView iv_pic;
	private View child;
	private TextView tv_skill;

	public AuthorPager(Activity activity)
	{
		super(activity);
	}

	@Override
	public void initData()
	{
		fl_content.removeAllViews();
		tv_title.setText("关于作者");
		ib_menu.setVisibility(View.INVISIBLE);
		this.setSlidingMenuEnable(false);// 关闭侧边栏的侧拉功能
		child = View.inflate(activity, R.layout.page_author, null);
		initHeader();
		initSkill();
		initQQ();
		initPhone();
		initMore();
		fl_content.addView(child);

	}

	private void initMore()
	{
		LinearLayout ll_more=(LinearLayout) child.findViewById(R.id.ll_more);
		ll_more.setOnClickListener(this);
	}

	private void initPhone()
	{
		LinearLayout ll_phone=(LinearLayout) child.findViewById(R.id.ll_phone);
		ll_phone.setOnClickListener(this);
	}

	private void initQQ()
	{
		LinearLayout ll_qq=(LinearLayout) child.findViewById(R.id.ll_qq);
		ll_qq.setOnClickListener(this);
	}

	private void initSkill()
	{
		LinearLayout ll_skill = (LinearLayout) child
				.findViewById(R.id.ll_skill);
		tv_skill = (TextView) child.findViewById(R.id.tv_skill);
		iv_arrow = (ImageView) child.findViewById(R.id.iv_arrow);
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tv_skill
				.getLayoutParams();
		params.height = getShortMeasureHeight();
		tv_skill.setLayoutParams(params);
		ll_skill.setOnClickListener(this);
	}

	private void initHeader()
	{
		LinearLayout ll_head = (LinearLayout) child.findViewById(R.id.ll_head);
		iv_pic = (ImageView) child.findViewById(R.id.iv_pic);
		// 头像处理
		boolean isLogin = SPUtils.getBoolean(activity, "isLogin", false);
		iv_pic.setBackgroundResource(isLogin ? R.drawable.ic_xhw
				: R.drawable.bg_photo);
		ll_head.setOnClickListener(this);
	}

	// 默认不展开
	private boolean isExpand = false;
	
	private ImageView iv_arrow;

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.ll_more:
			activity.startActivity(new Intent(activity, MoreActivity.class));
		break;
		
		case R.id.ll_phone:
			AlertDialog.Builder builder=new Builder(activity);
			builder.setTitle("温馨提示");
			builder.setMessage("是否确定联系作者?");
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					Intent intent=new Intent();
					intent.setAction(Intent.ACTION_CALL);
					intent.setData(Uri.parse("tel:"+"15018880175"));
					activity.startActivity(intent);
				}
			});
			
			builder.setNegativeButton("取消", null);
			builder.show();
			
			break;
		
		case R.id.ll_qq:
			//打开QQ
			APPUtils.openQQ(activity, "873085151");
			break;
		
		case R.id.ll_head:
			boolean isLogin = SPUtils.getBoolean(activity, "isLogin", false);
			iv_pic.setBackgroundResource(isLogin ? R.drawable.bg_photo
					: R.drawable.ic_xhw);
			SPUtils.setBoolean(activity, "isLogin", !isLogin);
			break;
		case R.id.ll_skill:
			int startHeight;
			int endHeight;
			if (!isExpand)
			{
				// 展开
				startHeight = getShortMeasureHeight();
				endHeight = getLongMeasureHeight();
			} else
			{
				// 缩起
				endHeight = getShortMeasureHeight();
				startHeight = getLongMeasureHeight();
			}
			isExpand = !isExpand;

			ValueAnimator animator = ValueAnimator
					.ofInt(startHeight, endHeight);
			animator.addUpdateListener(new AnimatorUpdateListener() {

				@Override
				public void onAnimationUpdate(ValueAnimator animation)
				{
					int value = (Integer) animation.getAnimatedValue();
					RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tv_skill
							.getLayoutParams();
					params.height = value;
					tv_skill.setLayoutParams(params);
				}
			});

			animator.addListener(new DefaultAnimatorListener() {

				@Override
				public void onAnimationEnd(Animator animation)
				{
					iv_arrow.setBackgroundResource(isExpand ? R.drawable.arrow_up
							: R.drawable.arrow_down);
					//让ScrollView移动到最下面
					//scrollView.scrollTo(0, scrollView.getMeasuredHeight());
				}

			});

			animator.setDuration(500);
			animator.start();
			break;
			
		}
	}

	// 获取展开2行时的高度
	private int getShortMeasureHeight()
	{
		// 复制一个新的TextView进行测量，最好不要在原来的TextView上测量，有可能影响其它代码
		TextView copyTextView = new TextView(activity);
		copyTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);// 字体14dp
		copyTextView.setLines(2);// 强制占2行s
		copyTextView.setMaxLines(2);// 最多2行

		// 开始测量
		int width = copyTextView.getMeasuredWidth();
		int widthMeasureSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.EXACTLY,
				width);
		int heightMeasureSpec = MeasureSpec.makeMeasureSpec(
				MeasureSpec.AT_MOST, 1000);
		copyTextView.measure(widthMeasureSpec, heightMeasureSpec);
		return copyTextView.getMeasuredHeight();

	}

	// 获取全部内容展开时的高度
	private int getLongMeasureHeight()
	{
		int width = tv_skill.getMeasuredWidth();
		int widthMeasureSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.EXACTLY,
				width);

		tv_skill.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
		int heightMeasureSpec = MeasureSpec.makeMeasureSpec(
				MeasureSpec.AT_MOST, 1000);
		tv_skill.measure(widthMeasureSpec, heightMeasureSpec);
		return tv_skill.getMeasuredHeight();
	}

}
