package me.tangke.navigationbar;

import java.lang.ref.WeakReference;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

class NavigationBarFragmentImpl extends NavigationBarActivityImpl {
	private WeakReference<Fragment> mFragment;

	public NavigationBarFragmentImpl(Fragment fragment) {
		super(fragment.getActivity());
		mFragment = new WeakReference<Fragment>(fragment);
	}

	@Override
	protected void onReplaceContentView(ViewGroup navigationBarContainer,
			ViewGroup navigationBarContentContainer) {
	}

	@Override
	void setContentView(View view, LayoutParams params) {
		if (null == view) {
			return;
		}
		getNavigationBarContentView().removeAllViews();
		params = null == params ? new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.MATCH_PARENT) : params;
		getNavigationBarContentView().addView(view, params);
	}

	@Override
	public void setDisplayOptions(int displayOptions) {
		// 过滤掉NavigationBar.DISPLAY_PRIMARY_NAVIGATION_AS_UP, Fragment不支持后退
		super.setDisplayOptions(displayOptions);
//		super.setDisplayOptions(displayOptions
//				& ~NavigationBar.DISPLAY_PRIMARY_NAVIGATION_AS_UP);
	}
}
