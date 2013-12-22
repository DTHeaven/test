package com.example.testapp1;

import java.util.ArrayList;

import com.example.testapp1.ContentPagerAdapter.OnItemClickListener;
import com.example.testapp1.ui.HorizontalListView;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainFragment extends Fragment {

	private static final String TAG = "MainFragment";

	private ViewPager mViewPager;
	private int mLastTouchedPosition;
	private boolean mChangePositionByFooter;

	private Handler mHandler = new Handler();
	private DisplayMetrics mMetrics = new DisplayMetrics();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.main_fragme, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		init();
	}

	private void init() {
		getActivity().getWindowManager().getDefaultDisplay()
				.getMetrics(mMetrics);

		mViewPager = (ViewPager) getView().findViewById(R.id.view_pager);
		ArrayList<String> data = new ArrayList<String>();
		for (int i = 0; i < 20; i++) {
			data.add("1");
		}

		ContentPagerAdapter adapter = new ContentPagerAdapter(getActivity(),
				data);
		mViewPager.setAdapter(adapter);

		adapter.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(View view, int position) {
				// Toast.makeText(getActivity(), "CurPage:" +
				// mViewPager.getCurrentItem(), Toast.LENGTH_SHORT).show();
				FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				// ft.setCustomAnimations(android.R.anim.fade_in,
				// android.R.anim.fade_out);
				ft.setCustomAnimations(android.R.anim.fade_in,
						android.R.anim.fade_out, android.R.anim.slide_in_left,
						android.R.anim.slide_out_right);
				ft.replace(R.id.main_frame, new ContentFragment());
				ft.addToBackStack(null);
				ft.commit();
			}
		});

		final HorizontalListView footerGallery = (HorizontalListView) getView()
				.findViewById(R.id.footer_banner);
		ArrayList<String> data2 = new ArrayList<String>();
		for (int i = 0; i < 20; i++) {
			data2.add("1");
		}
		MyGalleryAdapter adapter2 = new MyGalleryAdapter(getActivity(), data2);
		footerGallery.setAdapter(adapter2);
		for (int i = footerGallery.getFirstVisiblePosition(); i <= footerGallery
				.getLastVisiblePosition(); i++) {
			footerGallery.getChildAt(i).scrollTo(
					0,
					footerGallery.getSelectedItemPosition() == i ? -30
							: -20 * 6 - 30);
		}

		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(final int arg0) {
				if (!mChangePositionByFooter) {
					mLastTouchedPosition = arg0;
					final int positonInListView = arg0 > 2 ? arg0 - 3 : 0;
					footerGallery.setSelection(positonInListView);
				}

				mChangePositionByFooter = false;

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		footerGallery.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int firstPosition = footerGallery.getFirstVisiblePosition();
				int lastPosition = footerGallery.getLastVisiblePosition();
				int action = event.getAction();
				if (action == MotionEvent.ACTION_DOWN
						|| action == MotionEvent.ACTION_MOVE) {
					if (action == MotionEvent.ACTION_DOWN) {
						((MainActivity) getActivity()).mSlidingMenu
								.setSlidingEnabled(false);
					}

					for (int i = firstPosition; i <= lastPosition; i++) {

						if (isEventWithinView(event, footerGallery.getChildAt(i - firstPosition))) {
							footerGallery.getChildAt(i - firstPosition).scrollTo(0, -30);

							if (mLastTouchedPosition != i) {
								mLastTouchedPosition = i;

								computeScroll(footerGallery, mLastTouchedPosition);
							}

							break;
						}
					}

				} else {
					
//					footerGallery.getChildAt(
//							mLastTouchedPosition - firstPosition).scrollTo(0, -40);
//					for (int i = firstPosition; i <= lastPosition; i++) {
//						if (i != mLastTouchedPosition) {
//							// reset height to default
//							footerGallery.getChildAt(i - firstPosition)
//									.scrollTo(0, -15 * 6 - 30);
//						}
//					}
					
					resetScroll(footerGallery, mLastTouchedPosition);
					mChangePositionByFooter = true;
					mViewPager.setCurrentItem(mLastTouchedPosition);
					((MainActivity) getActivity()).mSlidingMenu.setSlidingEnabled(true);
				}

				return false;
			}
		});

		getView().findViewById(R.id.back_to_first).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						mViewPager.setCurrentItem(0);
					}
				});

		ImageView menu = (ImageView) getView().findViewById(R.id.menu);
		menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((MainActivity) getActivity()).getSlidingMenu().toggle();
			}
		});

	}

	private void resetScroll(HorizontalListView listView, int selectedPosition) {
		int firstPosition = listView.getFirstVisiblePosition();
		int lastPosition = listView.getLastVisiblePosition();
		
		listView.getChildAt(selectedPosition - firstPosition).scrollTo(0, -40);
		for (int i = firstPosition; i <= lastPosition; i++) {
			if (i != selectedPosition) {
				// reset height to default
				listView.getChildAt(i - firstPosition).scrollTo(0, -15 * 6 - 30);
			}
		}
	}
	
	private void computeScroll(HorizontalListView listView, int selectedPosition) {
		int firstPosition = listView.getFirstVisiblePosition();
		int lastPosition = listView.getLastVisiblePosition();

		for (int i = selectedPosition - 1; i >= firstPosition; i--) {
			int scrollY = -15 * (selectedPosition - i) - 30;
			View view = listView.getChildAt(i - firstPosition);
			if (view != null) {
				view.scrollTo(0, scrollY);
			} else {
				Log.e(TAG, "view == null");
			}
		}

		for (int j = selectedPosition + 1; j <= lastPosition; j++) {
			int scrollY = -15 * (j - selectedPosition) - 30;
			View view = listView.getChildAt(j - firstPosition);
			if (view != null) {
				view.scrollTo(0, scrollY);
			} else {
				Log.e(TAG, "view == null");
			}

		}
	}

	private ViewGroup.LayoutParams generateLayoutParams(View container) {
		int screenWidth = mMetrics.widthPixels;
		int width = (screenWidth) / 7;
		return new ViewGroup.LayoutParams(width,
				LinearLayout.LayoutParams.MATCH_PARENT);
	}

	private boolean isEventWithinView(MotionEvent e, View child) {
		Rect viewRect = new Rect();
		int[] childPosition = new int[2];
		child.getLocationOnScreen(childPosition);
		int left = childPosition[0];
		int right = left + child.getWidth();
		int top = childPosition[1];
		int bottom = top + child.getHeight();
		viewRect.set(left, top, right, bottom);
		return viewRect.contains((int) e.getRawX(), (int) e.getRawY());
	}

	class MyGalleryAdapter extends BaseAdapter {

		private Context mContext;
		private ArrayList<String> mData;

		public MyGalleryAdapter(Context context, ArrayList<String> data) {
			mContext = context;
			mData = data;
		}

		@Override
		public int getCount() {
			return mData.size();
		}

		@Override
		public Object getItem(int position) {
			return mData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			Log.d(TAG, "getView:" + (convertView == null));
			if (convertView == null) {
				convertView = View
						.inflate(mContext, R.layout.footer_item, null);
				convertView.setLayoutParams(generateLayoutParams(convertView));
				convertView.scrollTo(0, position == mLastTouchedPosition ? -40
						: -15 * 6 - 30);

				ImageView imageView = (ImageView) convertView
						.findViewById(R.id.footer_icon);
				holder = new ViewHolder();
				holder.icon = imageView;

				convertView.setTag(holder);
			}
			holder = (ViewHolder) convertView.getTag();
			holder.icon.setImageResource(getIconResID(position));

			return convertView;
		}

		private int getIconResID(int position) {
			int[] resId = new int[] { R.drawable.ic_1, R.drawable.ic_2,
					R.drawable.ic_3, R.drawable.ic_4, R.drawable.ic_5,
					R.drawable.ic_6, R.drawable.ic_7, R.drawable.ic_8,
					R.drawable.ic_9, R.drawable.ic_10, R.drawable.ic_11,
					R.drawable.ic_12, R.drawable.ic_13, R.drawable.ic_14,
					R.drawable.ic_15, R.drawable.ic_16, R.drawable.ic_17,
					R.drawable.ic_18, R.drawable.ic_19, R.drawable.ic_20, };

			return resId[position];
		}

		class ViewHolder {
			ImageView icon;
		}
	}
}
