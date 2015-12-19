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
		tv_title.setText("��������");
		ib_menu.setVisibility(View.INVISIBLE);
		this.setSlidingMenuEnable(false);// �رղ�����Ĳ�������
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
		// ͷ����
		boolean isLogin = SPUtils.getBoolean(activity, "isLogin", false);
		iv_pic.setBackgroundResource(isLogin ? R.drawable.ic_xhw
				: R.drawable.bg_photo);
		ll_head.setOnClickListener(this);
	}

	// Ĭ�ϲ�չ��
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
			builder.setTitle("��ܰ��ʾ");
			builder.setMessage("�Ƿ�ȷ����ϵ����?");
			builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					Intent intent=new Intent();
					intent.setAction(Intent.ACTION_CALL);
					intent.setData(Uri.parse("tel:"+"15018880175"));
					activity.startActivity(intent);
				}
			});
			
			builder.setNegativeButton("ȡ��", null);
			builder.show();
			
			break;
		
		case R.id.ll_qq:
			//��QQ
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
				// չ��
				startHeight = getShortMeasureHeight();
				endHeight = getLongMeasureHeight();
			} else
			{
				// ����
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
					//��ScrollView�ƶ���������
					//scrollView.scrollTo(0, scrollView.getMeasuredHeight());
				}

			});

			animator.setDuration(500);
			animator.start();
			break;
			
		}
	}

	// ��ȡչ��2��ʱ�ĸ߶�
	private int getShortMeasureHeight()
	{
		// ����һ���µ�TextView���в�������ò�Ҫ��ԭ����TextView�ϲ������п���Ӱ����������
		TextView copyTextView = new TextView(activity);
		copyTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);// ����14dp
		copyTextView.setLines(2);// ǿ��ռ2��s
		copyTextView.setMaxLines(2);// ���2��

		// ��ʼ����
		int width = copyTextView.getMeasuredWidth();
		int widthMeasureSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.EXACTLY,
				width);
		int heightMeasureSpec = MeasureSpec.makeMeasureSpec(
				MeasureSpec.AT_MOST, 1000);
		copyTextView.measure(widthMeasureSpec, heightMeasureSpec);
		return copyTextView.getMeasuredHeight();

	}

	// ��ȡȫ������չ��ʱ�ĸ߶�
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
