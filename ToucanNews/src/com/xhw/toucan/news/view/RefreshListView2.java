package com.xhw.toucan.news.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.xhw.toucan.news.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


public class RefreshListView2 extends ListView
{

	/**
	 * ����ˢ��״̬
	 */
	private final static int PULL_REFRESH = 0;
	/**
	 * �ɿ�ˢ��״̬
	 */
	private final static int RELEASE_REFRESH = 1;
	/**
	 * ����ˢ��״̬
	 */
	private final static int REFRESHING = 2;

	/**
	 * ��ǰˢ��״̬
	 */
	private int currentState = PULL_REFRESH;

	/**
	 * ����View
	 */
	private View headerView;

	private ImageView iv_arrow;

	private ProgressBar pb_rotate;

	private TextView tv_state;

	private TextView tv_time;

	/**
	 * �ײ�View
	 */
	private View footerView;

	// Animation
	private RotateAnimation down2UpAnimation;

	private RotateAnimation up2DownAnimation;

	/**
	 * ����View�ĸ߶�
	 */
	private int headerViewHeight;

	/**
	 * �ײ�View�ĸ߶�
	 */
	private int footerViewHeight;

	public RefreshListView2(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	public RefreshListView2(Context context)
	{
		super(context);
		init();
	}

	private void init()
	{
		initHeadView();
		initAnimation();
		initFooterView();
		initListener();
	}

	/**
	 * ��ǰ�Ƿ��ڼ��ظ���״̬�ı��
	 */
	private boolean isLoadingMore = false;

	/**
	 * ��ʼ��������
	 */
	private void initListener()
	{
		// ���ñ��ؼ��Ĺ���������(���ô˼�����Ϊ�˴���ײ�View)
		this.setOnScrollListener(new OnScrollListener() {

			/**
			 * ����״̬�ı䣬�˷������� ������scrollState������ȡֵ SCROLL_STATE_IDLE :����״̬��������ָ�ɿ�
			 * SCROLL_STATE_TOUCH_SCROLL : ��ָ�������������ǰ��Ż��� SCROLL_STATE_FLING :
			 * ���ٻ������ɿ������Ի���
			 */
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState)
			{
				// ����ָ�ɿ������һ�����ListView�����һ����Ŀʱ����Ҫ��ʾFooterView
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
						&& (RefreshListView2.this.getLastVisiblePosition() == RefreshListView2.this
								.getCount() - 1))
				{
					if (!isLoadingMore)// �����ظ�ִ��
					{
						isLoadingMore = true;
						// (RefreshListView.this.getLastVisiblePosition() ==
						// RefreshListView.this.getCount() - 1)
						// �����Ǹ��߼��жϵ�ʵ����:��Ȼ��ʼ��ʱ��footerView�����ˣ������Ҿ��ò�û����ȫ���صģ�����һ����¶������ֻ���������ۿ�����
						// �����������һ����ͨitem�ٹ�һ����λ��ʱ��getLastVisiblePosition�õ���λ�þ���FooterView��λ�ã�getCount�õ�������ListView����Ŀ��������HeaderView��footerView
						// ����ܽ���Ϊʲô���һ����ͨ��Ŀ�ճ���ʱ�����ܽ�������߼��ж���

						// �ı�padding,��footerView��ʾ����
						footerView.setPadding(0, 0, 0, 0);
						// ѡ�����һ��
						RefreshListView2.this.setSelection(getCount() - 1);
						
						//��¶�ӿڣ����ⲿ�Ĵ��������������
						if(onLoadingMoreListener!=null)
						{
							onLoadingMoreListener.onLoadingMore();
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

	/**
	 * ��ʼ���ײ�View
	 */
	private void initFooterView()
	{
		footerView = View.inflate(getContext(),
				R.layout.refresh_listview_footer, null);
		footerView.measure(0, 0);// ǿ��ϵͳȥ������view����������û���壬������ɺ�����õ�getMeasuredHeight
		footerViewHeight = footerView.getMeasuredHeight();
		// ����footerView��padding-topΪ����footerView�ĸ߶ȣ��ﵽ��������Ŀ��
		footerView.setPadding(0, -footerViewHeight, 0, 0);
		this.addFooterView(footerView);
	}

	/**
	 * ��ʼ����ͷ��ת����
	 */
	private void initAnimation()
	{
		down2UpAnimation = new RotateAnimation(0, -180,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		down2UpAnimation.setDuration(300);
		down2UpAnimation.setFillAfter(true);
		up2DownAnimation = new RotateAnimation(-180, -360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		up2DownAnimation.setDuration(300);
		up2DownAnimation.setFillAfter(true);
	}

	/**
	 * ��ʼ������View padding-top Ϊ -1*headerViewHeightʱ��ȫ������ padding-top Ϊ
	 * 0ʱ��HeaderViewȫ����ʾ
	 */
	private void initHeadView()
	{
		headerView = View.inflate(this.getContext(),
				R.layout.refresh_listview_header, null);
		iv_arrow = (ImageView) headerView.findViewById(R.id.iv_arrow);
		pb_rotate = (ProgressBar) headerView.findViewById(R.id.pb_rotate);
		tv_state = (TextView) headerView.findViewById(R.id.tv_state);
		tv_time = (TextView) headerView.findViewById(R.id.tv_time);
		headerView.measure(0, 0);// ǿ��ϵͳȥ������view����������û���壬������ɺ�����õ�getMeasuredHeight
		headerViewHeight = headerView.getMeasuredHeight();
		// ����HeaderView��padding-topΪ����HeaderView�ĸ߶ȣ��ﵽ��������Ŀ��
		headerView.setPadding(0, -headerViewHeight, 0, 0);
		this.addHeaderView(headerView);
	}

	/**
	 * ���ڼ�¼��ָÿ�ΰ��µĴ�ֱ����λ��(��ViewΪ�ο�ϵ)
	 */
	private int startY;

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		switch (event.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			startY = (int) event.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			// �������ˢ�£�����������
			if (currentState == REFRESHING)
			{
				break;
			}
			// �������֧���棬startY�Ǹ�����
			// ��ȡ�ƶ�������仯��
			int detalY = (int) (event.getY() - startY);
			// ��̬�ı�HeaderView��padding-top
			// padding-top ������ȡֵ��Χ : [-headerViewHeight,������)
			// �ֱ��ӦHeaderViewȫ�����أ���ȫ����ʾ����
			int paddingTop = -1 * headerViewHeight + detalY;
			// padding-top�ĺ���Χ�����ȫ����������HeaderView���������item��������
			// this.getFirstVisiblePosition()==0:��ʾ��һ���ɼ�item��������0ʱ����ִ�����أ���Ϊ���������item�ڵ�һ���ɼ���λ�ã���������Ҫ��ʾHeaderView��������Ҫ����ListView
			if (paddingTop >= -1 * headerViewHeight
					&& this.getFirstVisiblePosition() == 0)
			{
				// ����HeaderView��padding-top
				headerView.setPadding(0, paddingTop, 0, 0);

				// ���߽��������������HeaderView�϶�������������

				// ����������ߴ�������HeaderView�������������ˣ���ʾ�ɿ�ˢ��
				if (paddingTop > 0 && currentState == PULL_REFRESH)// ����һ��������״̬�����仯������
				{
					// �ɿ�ˢ��״̬
					currentState = RELEASE_REFRESH;
					refreshHeaderView();

				} else if (paddingTop <= 0 && currentState == RELEASE_REFRESH)
				{// ���С������HeaderView�����������ˣ���ʾ����ˢ��
					// ����ˢ��״̬
					currentState = PULL_REFRESH;
					refreshHeaderView();
				}

				// HeaderView���������ˣ��͸���ListView���¼���Ӧ�����ListView�Ļ����¼���HeaderView��Padding-top�仯ͬʱ���������һ�ֻ������Ҹ�
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
			// ����ɿ���ָ���Ǵ�������ˢ��״̬������HeaderView���ؾͿ�����
			if (currentState == PULL_REFRESH)
			{
				headerView.setPadding(0, -1 * headerViewHeight, 0, 0);
			} else if (currentState == RELEASE_REFRESH)// ����ɿ���ָ���Ǵ����ɿ�ˢ��״̬,����״̬Ϊˢ����״̬
			{
				// HeaderView��ȫ��ʾ
				headerView.setPadding(0, 0, 0, 0);
				currentState = REFRESHING;
				refreshHeaderView();
				// �������ˢ��״̬�������ⲿ���������һЩ����
				if (onRefreshingListener != null)
				{
					onRefreshingListener.onRefreshing();
				}
			}
			break;
		}
		return super.onTouchEvent(event);
	}

	/**
	 * ����currentState��״̬��ˢ��HeaderView
	 */
	private void refreshHeaderView()
	{
		switch (currentState)
		{
		case PULL_REFRESH:// ����ˢ��
			iv_arrow.startAnimation(up2DownAnimation);
			tv_state.setText("����ˢ��");
			break; 
		case RELEASE_REFRESH:// �ɿ�ˢ��
			iv_arrow.startAnimation(down2UpAnimation);
			tv_state.setText("�ɿ�ˢ��");
			break;
		case REFRESHING:// ˢ����
			iv_arrow.clearAnimation();// �������
			iv_arrow.setVisibility(View.INVISIBLE);
			pb_rotate.setVisibility(View.VISIBLE);
			tv_state.setText("����ˢ��...");
			break;
		}
	}

	/**
	 * �������ˢ�²���������HeaderView״̬ �ڻ�ȡ�������ݲ�����adapter�����ⲿ���ã�ע��Ҫ��UI�߳��е���
	 */
	public void completeRefresh()
	{
		// ����״̬
		currentState = PULL_REFRESH;
		// ����HeaderView
		headerView.setPadding(0, -1 * headerViewHeight, 0, 0);
		// ������ת������
		pb_rotate.setVisibility(View.INVISIBLE);
		// ��ʾ������ͷ
		iv_arrow.setVisibility(View.VISIBLE);
		// ����״̬�ı�
		tv_state.setText("����ˢ��");
		// ����ˢ��ʱ���ı�
		tv_time.setText("������:" + getCurrentTime());
	}

	
	/**
	 * ��ɼ��ظ������������FooterView״̬ �ڻ�ȡ�������ݲ�����adapter�����ⲿ���ã�ע��Ҫ��UI�߳��е���
	 */
	public void completeLoadingMore()
	{
		//����footerView
		footerView.setPadding(0, -footerViewHeight, 0, 0);
		//ˢ��״̬
		this.isLoadingMore=false;
	}
	
	/**
	 * ��ȡ��ǰʱ��
	 * 
	 * @return ��ǰʱ��
	 */
	private String getCurrentTime()
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		return dateFormat.format(new Date());
	}

	/**
	 * ����ˢ�¿ؼ���������ˢ��״̬�ļ�����
	 */
	private OnRefreshingListener onRefreshingListener;

	/**
	 * ��������ˢ�¿ؼ���������ˢ��״̬�ļ�����
	 * 
	 * @param onRefreshingListener
	 *            ��������ˢ�¿ؼ���������ˢ��״̬�ļ�����
	 */
	public void setOnRefreshingListener(
			OnRefreshingListener onRefreshingListener)
	{
		this.onRefreshingListener = onRefreshingListener;
	}

	/**
	 * ����ˢ�¿ؼ���������ˢ��״̬�ļ������ӿ�
	 * 
	 * @author admin
	 *
	 */
	public interface OnRefreshingListener
	{
		/**
		 * �����ڴ˷������������磬������������ݲ��������ݣ�����Adapter��Ӧ�õ���completeRefresh���������ö���View��״̬
		 */
		public void onRefreshing();
	}

	// ===============���ظ��ಿ��================================

	/**
	 * ����ˢ�¿ؼ�������ظ���״̬�ļ�����
	 */
	private OnLoadingMoreListener onLoadingMoreListener;

	/**
	 * ��������ˢ�¿ؼ�������ظ���״̬�ļ�����
	 * 
	 * @param onRefreshingListener
	 *            ��������ˢ�¿ؼ�������ظ���״̬�ļ�����
	 */
	public void setOnLoadingMoreListener(
			OnLoadingMoreListener onLoadingMoreListener)
	{
		this.onLoadingMoreListener = onLoadingMoreListener;
	}

	/**
	 * ����ˢ�¿ؼ�������ظ���״̬�ļ������ӿ�
	 * 
	 * @author admin
	 *
	 */
	public interface OnLoadingMoreListener
	{
		//�����ڴ˷������������磬������������ݲ��������ݣ�����Adapter��Ӧ�õ���completeLoadingMore���������õײ�View��״̬
		public void onLoadingMore();
	}
}
