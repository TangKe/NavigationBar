package me.tangke.navigationbar;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

class NavigationBarFragmentImpl extends NavigationBarImpl {
	private WeakReference<Fragment> mFragment;

	public NavigationBarFragmentImpl(Fragment fragment) {
		super(fragment.getActivity());
		mFragment = new WeakReference<Fragment>(fragment);
	}

	@Override
	public CharSequence getDefaultTitle() {
		return mFragment.get().getActivity().getTitle();
	}

	@Override
	public Drawable getDefaultIcon() {
		final Activity activity = mFragment.get().getActivity();
		try {
			return activity.getPackageManager().getActivityIcon(
					activity.getComponentName());
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onReplaceContentView(ViewGroup navigationBarContainer,
			ViewGroup navigationBarContentContainer) {
	}

	@Override
	protected void onNavigationUp() {

	}

	@Override
	void setContentView(View view, LayoutParams params) {
		params = null == params ? new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.MATCH_PARENT) : params;
		getNavigationBarContentView().addView(view, params);
	}
}
