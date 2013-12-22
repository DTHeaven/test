package com.example.testapp1.ui;

import com.example.testapp1.R;
import com.example.testapp1.R.drawable;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class MyScrollView extends ScrollView {

	private static final String TAG = "MyScrollView";
	private static final int HEADER_ID = 0x11111111;

	private RelativeLayout mContainer;
	private FrameLayout mContent;
	private ImageView mHeaderImage;
	private int mTouchSlop;
	private int mHeaderHeight;
	private int mHeaderWidth;
	private int mScreenHeight;

	private ScalingRunnalable mScalingRunnalable;

	private static final Interpolator sInterpolator = new Interpolator() {

		@Override
		public float getInterpolation(float t) {
			t -= 1.0f;
			return t * t * t * t * t + 1.0f;
		}
	};

	public MyScrollView(Context context) {
		super(context);
		init(context);
	}

	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		Display display = ((Activity) context).getWindowManager()
				.getDefaultDisplay();
		mScreenHeight = display.getHeight();

		mContainer = new RelativeLayout(context);
		mContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		addView(mContainer);

		mHeaderImage = new ImageView(context);
		mHeaderImage.setId(HEADER_ID);
		mHeaderImage.setScaleType(ScaleType.CENTER_CROP);
		mHeaderImage.setAdjustViewBounds(true);
		mHeaderImage.setBackgroundColor(Color.BLUE);// TODO delete
		mHeaderImage.setImageResource(R.drawable.header);

		RelativeLayout.LayoutParams headerParams = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		// headerParams.leftMargin = 20;
		// headerParams.rightMargin = 20;
		headerParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		mContainer.addView(mHeaderImage, headerParams);

		mContent = new FrameLayout(context);
		RelativeLayout.LayoutParams contentParams = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		contentParams.addRule(RelativeLayout.BELOW, HEADER_ID);

		mContainer.addView(mContent, contentParams);

		mHeaderImage.measure(0, 0);
		mHeaderHeight = mHeaderImage.getHeight();

		Log.i(TAG, "header height:" + mHeaderHeight);

		mScalingRunnalable = new ScalingRunnalable();
		final ViewConfiguration configuration = ViewConfiguration.get(context);
		mTouchSlop = configuration.getScaledTouchSlop();

	}

	@Override
	public void addView(View child) {
		Log.i(TAG, "1111111111111");
		if (getChildCount() > 0) {
			mContent.addView(child);
		} else {
			super.addView(child);
		}
	}

	@Override
	public void addView(View child, int index) {
		Log.i(TAG, "2222222222222222");
		if (getChildCount() > 0) {
			mContent.addView(child, index);
		} else {
			super.addView(child, index);
		}
	}

	@Override
	public void addView(View child, android.view.ViewGroup.LayoutParams params) {
		Log.i(TAG, "3333333333333333");
		if (getChildCount() > 0) {
			mContent.addView(child, params);
		} else {
			super.addView(child, params);
		}
	}

	@Override
	public void addView(View child, int width, int height) {
		Log.i(TAG, "4444444444444444");
		if (getChildCount() > 0) {
			mContent.addView(child, width, height);
		} else {
			super.addView(child, width, height);
		}
	}

	@Override
	public void addView(View child, int index,
			android.view.ViewGroup.LayoutParams params) {
		Log.i(TAG, "5555555555555555");
		if (getChildCount() > 0) {
			mContent.addView(child, index, params);
		} else {
			super.addView(child, index, params);
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);

		if (mHeaderHeight == 0) {
			mHeaderWidth = mHeaderImage.getWidth();
			mHeaderHeight = mHeaderImage.getHeight();
		}
	}

	private static final int INVALID_POINTER = -1;
	int mActivePointerId = INVALID_POINTER;
	int mLastMotionY;
	float mMaxScale;
	float mLastScale = 1.0f;

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			if (!mScalingRunnalable.mIsFinished) {
				mScalingRunnalable.abortAnimation();
			}
			mLastMotionY = (int) ev.getY();
			mActivePointerId = ev.getPointerId(0);

			mMaxScale = ((float) mScreenHeight) / mHeaderHeight;
			mLastScale = ((float) mContent.getTop()) / mHeaderHeight;
			mHeaderImage.setScaleType(ScaleType.CENTER_CROP);
			break;

		case MotionEvent.ACTION_MOVE:
			final int activePointerIndex = ev
					.findPointerIndex(mActivePointerId);
			if (activePointerIndex == -1) {
				Log.e(TAG, "Invalid pointerId=" + mActivePointerId
						+ " in onTouchEvent");
				break;
			}
			// final int y = (int) ev.getY(activePointerIndex);
			// int deltaY = mLastMotionY - y;
			// if (Math.abs(deltaY) > mTouchSlop) {
			// final ViewParent parent = getParent();
			// if (parent != null) {
			// parent.requestDisallowInterceptTouchEvent(true);
			// }
			// if (deltaY > 0) {
			// deltaY -= mTouchSlop;
			// } else {
			// deltaY += mTouchSlop;
			// }
			// }

			if (getScrollY() == 0) {

				ViewGroup.LayoutParams params = mHeaderImage.getLayoutParams();

				float dy = ev.getY(activePointerIndex) - mLastMotionY;
				float scale = (mContent.getTop() + dy) / mHeaderHeight;
				scale = (scale - mLastScale) / 2 + mLastScale;

				if ((mLastScale <= 1.0) && scale < mLastScale) {
					return super.onTouchEvent(ev);
				}
				mLastScale = scale;

				params.height = (int) (mHeaderHeight * mLastScale);

				if (params.height < mScreenHeight) {
					mHeaderImage.setLayoutParams(params);
				}
				mLastMotionY = (int) ev.getY(activePointerIndex);

				return true;
			}

			mLastMotionY = (int) ev.getY(activePointerIndex);

			break;

		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			mActivePointerId = INVALID_POINTER;
			endScraling();
			break;

		case MotionEvent.ACTION_POINTER_DOWN: {
			final int index = ev.getActionIndex();
			mLastMotionY = (int) ev.getY(index);
			mActivePointerId = ev.getPointerId(index);
			break;
		}
		case MotionEvent.ACTION_POINTER_UP:
			onSecondaryPointerUp(ev);
			mLastMotionY = (int) ev.getY(ev.findPointerIndex(mActivePointerId));
			break;

		default:
			break;
		}

		return super.onTouchEvent(ev);
	}

	private void onSecondaryPointerUp(MotionEvent ev) {
		final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
		final int pointerId = ev.getPointerId(pointerIndex);
		if (pointerId == mActivePointerId) {
			// This was our active pointer going up. Choose a new
			// active pointer and adjust accordingly.
			// TODO: Make this decision more intelligent.
			final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
			mLastMotionY = (int) ev.getY(newPointerIndex);
			mActivePointerId = ev.getPointerId(newPointerIndex);
		}
	}

	private void endScraling() {
		if (mContent.getTop() > mHeaderHeight) {
			mScalingRunnalable.startAnimation(200);
		}
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if (getScrollY() > 0 && getScrollY() < mHeaderHeight) {
			int s = (int) (getScrollY() * 0.65);
			mHeaderImage.scrollTo(0, -s);
		} else if (mHeaderImage.getScrollY() != 0) {
			mHeaderImage.scrollTo(0, 0);
		}
	}

	class ScalingRunnalable implements Runnable {
		float mScale;
		long mStartTime;
		long mDuration;
		boolean mIsFinished = true;

		public boolean isFinished() {
			return mIsFinished;
		}

		public void abortAnimation() {
			mIsFinished = true;
		}

		public void startAnimation(long duration) {
			mStartTime = SystemClock.currentThreadTimeMillis();
			mDuration = duration;
			mScale = (float) mContent.getTop() / mHeaderHeight;
			mIsFinished = false;

			mContainer.post(this);
		}

		@Override
		public void run() {
			if (!mIsFinished && mScale > 1.0) {
				float currentTime = SystemClock.currentThreadTimeMillis();
				float normalizedTime = ((float) (currentTime - mStartTime))
						/ (float) mDuration;
				float scale = mScale - (mScale - 1.0f)
						* sInterpolator.getInterpolation(normalizedTime);

				ViewGroup.LayoutParams params = mHeaderImage.getLayoutParams();

				if (scale < 1.0f) {
					mIsFinished = true;
					params.height = mHeaderHeight;
				} else {
					params.height = (int) (mHeaderHeight * scale);
				}

				mHeaderImage.setLayoutParams(params);

				mContainer.post(this);
			}
		}

	}
}
