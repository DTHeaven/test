package com.example.testapp1;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;

public class ContentPagerAdapter extends PagerAdapter {

	private Context mContext;
	private ArrayList<String> mData;
	
	private OnItemClickListener mClickListener;
	
	private int mTouchSlop;
	private float mLastMotionX;
	private float mLastMotionY;
	private boolean mIsScrolling;
	
	public ContentPagerAdapter(Context context, ArrayList<String> data) {
		mContext = context;
		mData = data;
		
		final ViewConfiguration configuration = ViewConfiguration.get(context);
		mTouchSlop = configuration.getScaledTouchSlop();
	}
	
	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View)object);
	}
	
	@Override
	public int getItemPosition(Object object)
	{
		return POSITION_NONE;
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		
		final View view = View.inflate(mContext, R.layout.page_item, null);
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!mIsScrolling && mClickListener != null) {
					mClickListener.onItemClick(view, position);
				}
			}
		});
		
		view.setOnTouchListener(new OnTouchListener()
		{
			
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				switch (event.getAction())
				{
					case MotionEvent.ACTION_DOWN:
						mIsScrolling = false;
						mLastMotionX = event.getX();
						mLastMotionY = event.getY();
						break;
					
					case MotionEvent.ACTION_UP:
						mIsScrolling = isScrolling(mLastMotionX, event.getX());
						mIsScrolling |= isScrolling(mLastMotionY, event.getY());
						break;
					default:
						break;
				}
				return false;
			}
		});
		
		container.addView(view);
		return view;
	}

	private boolean isScrolling(float lastY, float curY)
	{
		return Math.abs(lastY - curY) > mTouchSlop;
	}
	
	public interface OnItemClickListener {
		public void onItemClick(View view, int position);
	}
	
	public void setOnItemClickListener(OnItemClickListener l) {
		mClickListener = l;
	}
}
