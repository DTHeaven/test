package com.example.testapp1;

import java.util.ArrayList;

import com.example.testapp1.ui.PullToZoomListView;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ContentFragment extends Fragment implements OnScrollListener {

	private static final String TAG = "ContentFragment";
	
	private ImageView mShare, mShareView, mBack;
	private TextView mShareText;
	
	private Activity mActivity;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.content_fragme, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mActivity = getActivity();
		
		init();
	}
	
	private void init()
	{
		ArrayList<String> data = new ArrayList<String>();
		data.add("1");
		data.add("2");
		data.add("3");
		data.add("4");
		data.add("5");
		
		PullToZoomListView listView = (PullToZoomListView) getView().findViewById(R.id.list_view);
		View headerView = View.inflate(getActivity(), R.layout.header, null);
		((ImageView)headerView.findViewById(R.id.icon)).setImageResource(R.drawable.icon);
		((TextView)headerView.findViewById(R.id.title)).setText("Clumsy Ninja");
		((TextView)headerView.findViewById(R.id.subhead)).setText("调戏呆萌忍者");
		((TextView)headerView.findViewById(R.id.share_txt)).setText("分享");
		((TextView)headerView.findViewById(R.id.short_content)).setText("听说这个小忍者最近很火呀？！还曾经做过水果家的御用演示游戏呀？！ 据说是用某技术实现了人工智能，是有智商有生命力的存在啦！P.S. 听说还有一只能学人动作的小松鼠啊，果断加分。于是小美兴冲冲地装上了这个游戏，开始了调戏某忍之旅——");
		
		mBack = (ImageView)getView().findViewById(R.id.back);
		mShare = (ImageView)getView().findViewById(R.id.share);
		
		mShareText = (TextView)headerView.findViewById(R.id.share_txt);
		
		mShareView = (ImageView)headerView.findViewById(R.id.share_icon);
		mShareView.setImageResource(R.drawable.share_icon);
		mShareView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), "Share", Toast.LENGTH_SHORT).show();
			}
		});
		
		mBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getFragmentManager().popBackStack();
			}
		});
		
		listView.addHeaderView(headerView);
		
		MyAdapter adapter = new MyAdapter(getActivity(), data);
		listView.setAdapter(adapter);
		listView.setHeaderImage(R.drawable.header);
		listView.setOnScrollListener(this);
	}

	class MyAdapter extends BaseAdapter
	{
		private Context mContext;
		private ArrayList<String> mDataList;
		
		public MyAdapter(Context context, ArrayList<String> data)
		{
			mContext = context;
			mDataList = data;
		}
		
		@Override
		public int getCount()
		{
			return mDataList.size();
		}

		@Override
		public Object getItem(int position)
		{
			return mDataList.get(position);
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			if (convertView == null)
			{
				convertView = View.inflate(mContext, R.layout.item, null);
			}
			
			((TextView)convertView.findViewById(R.id.content)).setText(R.string.content);
			return convertView;
		}
		
	}

	int mStatusBarHeight;
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (mStatusBarHeight == 0) {
			Rect frame = new Rect();  
			getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);  
			mStatusBarHeight = frame.top;  
		}
		
	}

	boolean mIsAnimating = false;
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (!mIsAnimating) {
			int[] shareLocal = new int[2];
			int[] backLocal = new int[2];
			mShareView.getLocationInWindow(shareLocal);
			mBack.getLocationInWindow(backLocal);
			Log.i(TAG, "share view x:" + backLocal[0] + " y:" + backLocal[1]);
			
			if (shareLocal[1] - mStatusBarHeight <= backLocal[1] - mStatusBarHeight 
					&& mShareView.getVisibility() == View.VISIBLE) {
				Log.w(TAG, "Share View...........");
				TranslateAnimation animation = new TranslateAnimation(shareLocal[0] - 110, backLocal[0] - 20, 
						shareLocal[1] - mStatusBarHeight - 20, backLocal[1] - mStatusBarHeight - 20);
				animation.setDuration(200);
				animation.setAnimationListener(new AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation animation) {
						mShare.setVisibility(View.VISIBLE);
						mShareView.setVisibility(View.INVISIBLE);
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						mIsAnimating = false;
					}
				});
				
				ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.0f, 1.0f, 1.0f);
				scaleAnimation.setDuration(200);
				scaleAnimation.setFillAfter(true);
				
				mShare.startAnimation(animation);
				mShareText.startAnimation(scaleAnimation);

				mIsAnimating = true;
			}
			else if (shareLocal[1] - mStatusBarHeight > backLocal[1] - mStatusBarHeight 
					&& mShareView.getVisibility() == View.INVISIBLE) {
				
				TranslateAnimation animation = new TranslateAnimation(backLocal[0] - 20, shareLocal[0] - 110, 
						backLocal[1] - mStatusBarHeight - 20, shareLocal[1] - mStatusBarHeight - 20);
				animation.setDuration(100);
				animation.setAnimationListener(new AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation animation) {
						
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						mIsAnimating = false;
						mShare.setVisibility(View.INVISIBLE);
						mShareView.setVisibility(View.VISIBLE);
						
					}
				});
				
				ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 1.0f, 1.0f);
				scaleAnimation.setDuration(200);
				scaleAnimation.setFillAfter(true);
				
				mShare.startAnimation(animation);
				mShareText.startAnimation(scaleAnimation);
				
				mIsAnimating = true;
			}
		}
		
	}
}
