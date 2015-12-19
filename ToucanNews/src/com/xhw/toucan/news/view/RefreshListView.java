package com.xhw.toucan.news.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xhw.toucan.news.R;

/**
 * ����ˢ��ListView
 * 
 * @author admin
 *
 */
public class RefreshListView extends ListView
{

	public static final int STATE_PULL_REFRESH = 0;// ����ˢ��
	public static final int STATE_RELEASE_REFRESH = 1;// �ɿ�ˢ��
	public static final int STATE_REFRESHING = 2;// ˢ����

	// ��ǰ״̬
	private int CURRENT_STATE = STATE_PULL_REFRESH;

	public int getCurrentState()
	{
		return CURRENT_STATE;
	}
	
	private int startY;
	private View headerView;
	private int headerHeight;
	private ImageView iv_arr;
	private ProgressBar pb_progress;
	private TextView tv_title;
	private TextView tv_time;
	private RotateAnimation down2up;
	private RotateAnimation up2down;

	public RefreshListView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		initHeaderView();
		initFooterView();
	}

	public RefreshListView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initHeaderView();
		initFooterView();
	}

	public RefreshListView(Context context)
	{
		super(context);
		initHeaderView();
		initFooterView();
	}

	/**
	 * ��ʼ��ͷ����
	 */
	private void initHeaderView()
	{
		headerView = View.inflate(getContext(), R.layout.refresh_header, null);
		// ����headerView
		headerView.measure(0, 0);
		headerHeight = headerView.getMeasuredHeight();
		headerView.setPadding(0, -1 * headerHeight, 0, 0);
		iv_arr = (ImageView) headerView.findViewById(R.id.iv_arr);
		pb_progress = (ProgressBar) headerView.findViewById(R.id.pb_progress);
		tv_title = (TextView) headerView.findViewById(R.id.tv_title);
		tv_time = (TextView) headerView.findViewById(R.id.tv_time);
		this.addHeaderView(headerView);
		// ��ʼ����ͷ����
		initArrowAnim();
	}

	//�Ƿ��ڼ��ظ���
	private boolean isLoadingMore=false;
	
	private void initFooterView()
	{
		footerView = View.inflate(getContext(), R.layout.refresh_footer, null);
		footerView.measure(0, 0);
		footerHeight = footerView.getMeasuredHeight();
		//����footerView
		footerView.setPadding(0, -1*footerHeight, 0, 0);
		this.addFooterView(footerView); 
		this.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState)
			{
				if(scrollState==OnScrollListener.SCROLL_STATE_IDLE)
				{
					//�������һ��
					if(getLastVisiblePosition()==getCount()-1&&!isLoadingMore)
					{
						isLoadingMore=true;
						footerView.setPadding(0, 0, 0, 0);
						//ѡ�����һ��
						setSelection(getCount()-1);
						//�ⲿ����
						if(onRefreshListener!=null)
						{
							onRefreshListener.onLoadingMore();
						}
					}
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount)
			{
				
			}
		});
	}
	
	//ʹ��position�ļ�����ʽ���Գ���HeaderView��������
	@Override
	public void setOnItemClickListener(
			final android.widget.AdapterView.OnItemClickListener listener)
	{
		super.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				listener.onItemClick(parent, view, position-getHeaderViewsCount(), id);
			}
		});
	}
	
	private void initArrowAnim()
	{
		down2up = new RotateAnimation(0, -180,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		down2up.setDuration(300);
		down2up.setFillAfter(true);
		
		up2down = new RotateAnimation(-180, -360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		up2down.setDuration(300);
		
		up2down.setFillAfter(true);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev)
	{
		switch (ev.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			startY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:

			if (CURRENT_STATE == STATE_REFRESHING)
			{
				break;
			}

			int endY = (int) ev.getY();
			int deltaY = endY - startY;
			// ���������������ǰ������б��һ��
			if (deltaY > 0 && getFirstVisiblePosition() == 0)
			{
				int paddingTop = -1 * headerHeight + deltaY;
				headerView.setPadding(0, paddingTop, 0, 0);
				if (paddingTop >= 0 && CURRENT_STATE != STATE_RELEASE_REFRESH)// ����headerView���������ˣ���ʾ�ɿ�ˢ��״̬
				{
					CURRENT_STATE = STATE_RELEASE_REFRESH;
					refreshState();
				} else if (paddingTop < 0
						&& CURRENT_STATE != STATE_PULL_REFRESH)// headerViewδ��ȫ��ʾ����Ϊ����ˢ��״̬s
				{
					CURRENT_STATE = STATE_PULL_REFRESH;
					refreshState();
				}
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
			if (CURRENT_STATE == STATE_RELEASE_REFRESH)// ����ˢ��
			{
				CURRENT_STATE = STATE_REFRESHING;
				// ��ȫ��ʾheaderView
				headerView.setPadding(0, -0, 0, 0);
				refreshState();
			} else if (CURRENT_STATE == STATE_PULL_REFRESH)
			{
				// ���ص�headerView
				headerView.setPadding(0, -1 * headerHeight, 0, 0);
			}
			break;

		}
		return super.onTouchEvent(ev);
	}

	private String getCurrentTime()
	{
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(new Date());
	}
	
	/**
	 * ˢ��״̬
	 */
	private void refreshState()
	{
		switch (CURRENT_STATE)
		{
		// ����ˢ��
		case STATE_PULL_REFRESH:
			tv_title.setText("����ˢ��");
			iv_arr.setVisibility(View.VISIBLE);
			pb_progress.setVisibility(View.INVISIBLE);
			iv_arr.clearAnimation();
			iv_arr.startAnimation(up2down);
			break;
		// �ɿ�ˢ��
		case STATE_RELEASE_REFRESH:
			tv_title.setText("�ɿ�ˢ��");
			iv_arr.setVisibility(View.VISIBLE);
			pb_progress.setVisibility(View.INVISIBLE);
			iv_arr.clearAnimation();
			iv_arr.startAnimation(down2up);
			break;
		// ˢ����
		case STATE_REFRESHING:
			tv_title.setText("ˢ����...");
			iv_arr.clearAnimation();
			iv_arr.setVisibility(View.INVISIBLE);
			pb_progress.setVisibility(View.VISIBLE);
			//�����ⲿ����
			if(onRefreshListener!=null)
			{
				onRefreshListener.onRefresh(); 
			}
			break;

		}
	}
	
	/**
	 * �������ˢ��
	 */
	public void completeRefresh(boolean success)
	{
		headerView.setPadding(0, -1*headerHeight, 0, 0);
		CURRENT_STATE=STATE_PULL_REFRESH;
		tv_title.setText("����ˢ��");
		iv_arr.setVisibility(View.VISIBLE);
		pb_progress.setVisibility(View.INVISIBLE);
		iv_arr.clearAnimation();
		if(success)
		tv_time.setText("���ˢ��ʱ��:"+getCurrentTime());
	}
	
	/**
	 * ��ɼ��ظ���
	 */
	public void completeLoadingMore()
	{
		footerView.setPadding(0, -1*footerHeight, 0, 0);
		isLoadingMore=false;
	}
	
	private OnRefreshListener onRefreshListener;
	private View footerView;
	private int footerHeight;
	
	
	public void setOnRefreshListener(OnRefreshListener onRefreshListener)
	{
		this.onRefreshListener = onRefreshListener;
	}



	public interface OnRefreshListener
	{
		public void onRefresh();
		public void onLoadingMore();
	}
}
