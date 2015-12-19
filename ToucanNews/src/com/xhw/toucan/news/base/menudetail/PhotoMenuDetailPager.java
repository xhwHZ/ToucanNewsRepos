package com.xhw.toucan.news.base.menudetail;

import java.util.List;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.xhw.toucan.news.activity.HomeActivity;
import com.xhw.toucan.news.base.BaseMenuDetailPager;
import com.xhw.toucan.news.base.impl.NewsCenterPager;
import com.xhw.toucan.news.domain.PhotosData;
import com.xhw.toucan.news.domain.PhotosData.PhotoNews;
import com.xhw.toucan.news.global.GlobalContants;
import com.xhw.toucan.news.utils.BitmapHelper;
import com.xhw.toucan.news.utils.CacheUtils;

/**
 * 菜单详情页-组图
 * 
 * @author admin
 *
 */
public class PhotoMenuDetailPager extends BaseMenuDetailPager
{

	public PhotoMenuDetailPager(Activity activity)
	{
		super(activity);
	}

	@ViewInject(R.id.lv_photo)
	private ListView lv_photo;

	@ViewInject(R.id.gv_photo)
	private GridView gv_photo;

	private List<PhotoNews> photoList;

	@Override
	public View initView()
	{
		View v = View.inflate(mActivity, R.layout.photo_menu_pager, null);

		ViewUtils.inject(this, v);
		return v;
	}

	@Override
	public void initData()
	{
		String cache = CacheUtils
				.getCache(mActivity, GlobalContants.PHOTOS_URL);
		if (!TextUtils.isEmpty(cache))
		{
			parseJson(cache);
		}
		// 从服务器获取数据
		getDataFromServer();
	}

	private boolean isShowList=true;//是否以列表显示
	
	@Override
	public void initListener()
	{
		//获取切换按钮
		HomeActivity homeActivity=(HomeActivity) mActivity;
		NewsCenterPager newsCenterPager = homeActivity.getHomeContentFragment().getNewsCenterPager();
		ImageButton ibPhoto = newsCenterPager.getIbPhoto();
		ibPhoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v)
			{
				ImageButton button=(ImageButton) v;
				if(isShowList)//切换到GridView
				{
					gv_photo.setVisibility(View.VISIBLE);
					lv_photo.setVisibility(View.GONE);
					button.setImageResource(R.drawable.icon_pic_list_type);
				}else{
					gv_photo.setVisibility(View.GONE);
					lv_photo.setVisibility(View.VISIBLE);
					button.setImageResource(R.drawable.icon_pic_grid_type);
				}
				isShowList=!isShowList;
			}
		});
	}
	
	private void getDataFromServer()
	{
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, GlobalContants.PHOTOS_URL,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo)
					{
						String result = responseInfo.result;
						parseJson(result);
						CacheUtils.setCache(mActivity,
								GlobalContants.PHOTOS_URL, result);
					}

					@Override
					public void onFailure(HttpException error, String msg)
					{
						Toast.makeText(mActivity, "网络连接失败", 0).show();
					}
				});
	}

	protected void parseJson(String result)
	{
		Gson gson = new Gson();
		PhotosData photosData = gson.fromJson(result, PhotosData.class);
		photoList = photosData.data.news;
		if (photoList != null)
		{
			PhotoAdapter adapter = new PhotoAdapter();
			lv_photo.setAdapter(adapter);
			gv_photo.setAdapter(adapter);
		}
	}

	class PhotoAdapter extends BaseAdapter
	{
		private BitmapUtils bitmapUtils;

		public PhotoAdapter()
		{
			bitmapUtils = BitmapHelper.getBitmapUtils();
			bitmapUtils.configDefaultLoadingImage(R.drawable.news_pic_default);
		}

		@Override
		public int getCount()
		{
			return photoList.size();
		}

		@Override
		public PhotoNews getItem(int position)
		{
			return photoList.get(position);
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			View v=null;
			ViewHolder holder=null;
			if(convertView==null)
			{
				holder=new ViewHolder();
				v=View.inflate(mActivity, R.layout.item_photo_list, null);
				holder.iv_pic=(ImageView) v.findViewById(R.id.iv_pic);
				holder.tv_title=(TextView) v.findViewById(R.id.tv_title);
				v.setTag(holder);
			}else{
				v=convertView;
				holder=(ViewHolder) v.getTag();
			}
			PhotoNews item = getItem(position);
			holder.tv_title.setText(item.title);
			bitmapUtils.display(holder.iv_pic, item.listimage);
			return v;
		}

		class ViewHolder
		{
			ImageView iv_pic;
			TextView tv_title;
		}
	}
}
