package me.tangke.navigationbar;

import java.lang.ref.WeakReference;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

/**
 * 针对Activity的NavigationBar实现
 * 
 * @author Tank
 * 
 */
class NavigationBarActivityImpl extends NavigationBarImpl {
	private WeakReference<Activity> mActivity;

	public NavigationBarActivityImpl(Activity activity) {
		super(activity);
		mActivity = new WeakReference<Activity>(activity);
	}

	@Override
	public CharSequence getDefaultTitle() {
		return mActivity.get().getTitle();
	}

	@Override
	public Drawable getDefaultIcon() {
		final Activity activity = mActivity.get();
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
		if (!hasNavigationBar()) {
			return;
		}
		Activity activity = mActivity.get();
		View content = activity.findViewById(android.R.id.content);
		ViewGroup contentParent = (ViewGroup) content.getParent();
		contentParent.removeView(content);
		navigationBarContentContainer.addView(content,
				new FrameLayout.LayoutParams(
						FrameLayout.LayoutParams.MATCH_PARENT,
						FrameLayout.LayoutParams.MATCH_PARENT));
		contentParent
				.addView(navigationBarContainer, content.getLayoutParams());
	}

	@SuppressLint("NewApi")
	@Override
	protected void onNavigationUp() {
		final Activity activity = mActivity.get();
		activity.finish();
		if (Build.VERSION_CODES.JELLY_BEAN <= Build.VERSION.SDK_INT) {
			activity.onNavigateUp();
		}
	}

	@Override
	void setContentView(View view, LayoutParams params) {
		params = null == params ? new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.MATCH_PARENT) : params;
		mActivity.get().setContentView(view, params);
	}
}
