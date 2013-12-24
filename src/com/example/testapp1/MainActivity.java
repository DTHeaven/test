package com.example.testapp1;

import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.TypedValue;
import android.view.Window;

public class MainActivity extends SlidingFragmentActivity {

	private static final String TAG = "1MainActivity";
	
	protected SlidingMenu mSlidingMenu;
	protected ListFragment mFrag;
	protected MainFragment mMainFrag;
	protected ContentFragment mContentFrag;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setBehindContentView(R.layout.menu_frame);
		setContentView(R.layout.activity_main);
		init(savedInstanceState);
	}

	private void init(Bundle savedInstanceState)
	{
		if (savedInstanceState == null) {
			FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
			mFrag = new SampleListFragment();
			mMainFrag = new MainFragment();
			t.replace(R.id.menu_frame, mFrag);
			t.replace(R.id.main_frame, mMainFrag);
			t.commit();
		} else {
			mFrag = (ListFragment)this.getSupportFragmentManager().findFragmentById(R.id.menu_frame);
		}
		
		mSlidingMenu = getSlidingMenu();
		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		mSlidingMenu.setBehindOffset(100);
		mSlidingMenu.setTouchmodeMarginThreshold((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 
				24, getResources().getDisplayMetrics()));
		
		
	}
	
}
