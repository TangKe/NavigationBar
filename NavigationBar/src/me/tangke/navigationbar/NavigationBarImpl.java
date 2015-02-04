package me.tangke.navigationbar;

import java.lang.ref.WeakReference;

import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources.Theme;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

/**
 * iPhone风格的{@link NavigationBar}实现
 * 
 * @author Tank
 * 
 */
public class NavigationBarImpl implements NavigationBar, OnClickListener,
		OnItemSelectedListener {
	private ViewGroup mNavigationBarContainer;
	private NavigationBarView mNavigationBarView;
	private ViewGroup mNavigationBarContentContainer;

	private OnNavigationListener mNavigationListener;

	private OnNavigationItemClickListener mOnNavigationItemClickListener;

	private int mNavigationMode;
	private boolean mIsNavigationBarOverlay;
	private boolean mHasNavigationBar;

	private WeakReference<NavigationBarActivity> mActivity;
	private LayoutInflater mInflater;

	private CharSequence mCurrentActivitTitle;

	public NavigationBarImpl(NavigationBarActivity activity) {
		mActivity = new WeakReference<NavigationBarActivity>(activity);
		mInflater = activity.getLayoutInflater();
		mCurrentActivitTitle = activity.getTitle();
		final Theme theme = activity.getTheme();

		TypedValue value = new TypedValue();
		theme.resolveAttribute(R.attr.isNavigationBarTheme, value, true);
		if (0 == value.data) {
			throw new IllegalStateException(
					"your theme must extend from Theme.NavigationBar");
		}

		// 处理是否是覆盖的NavigationBar
		theme.resolveAttribute(R.attr.navigationBarOverlay, value, true);
		mIsNavigationBarOverlay = 0 != value.data;
		mNavigationBarContainer = (ViewGroup) activity
				.getLayoutInflater()
				.inflate(
						mIsNavigationBarOverlay ? R.layout.layout_navigation_bar_overlay
								: R.layout.layout_navigation_bar, null);

		mNavigationBarContentContainer = (ViewGroup) mNavigationBarContainer
				.findViewById(R.id.navigationBarContentContainer);
		final NavigationBarView navigationBarView = mNavigationBarView = (NavigationBarView) mNavigationBarContainer
				.findViewById(R.id.navigationBar);

		theme.resolveAttribute(R.attr.windowNavigationBar, value, true);
		mHasNavigationBar = 0 != value.data;
		navigationBarView.setVisibility(mHasNavigationBar ? View.VISIBLE
				: View.GONE);

		navigationBarView.getPrimaryNavigation().setOnClickListener(this);
		navigationBarView.getSecondaryNavigation().setOnClickListener(this);
		setTitle(mCurrentActivitTitle);
		try {
			setIcon(activity.getPackageManager().getActivityIcon(
					activity.getComponentName()));
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		navigationBarView.getListNavigation().setOnItemSelectedListener(this);

		replaceContentView();
	}

	private void replaceContentView() {
		if (!hasNavigationBar()) {
			return;
		}

		NavigationBarActivity activity = mActivity.get();
		View content = activity.findViewById(android.R.id.content);
		ViewGroup contentParent = (ViewGroup) content.getParent();
		contentParent.removeView(content);
		mNavigationBarContentContainer.addView(content,
				new FrameLayout.LayoutParams(
						FrameLayout.LayoutParams.MATCH_PARENT,
						FrameLayout.LayoutParams.MATCH_PARENT));
		contentParent.addView(mNavigationBarContainer,
				content.getLayoutParams());
	}

	public boolean hasNavigationBar() {
		return mHasNavigationBar;
	}

	public void setContentView(View view) {
		setContentView(view, null);
	}

	public void setContentView(int res) {
		setContentView(mInflater.inflate(res, null), null);
	}

	public void setContentView(View view, ViewGroup.LayoutParams params) {
		params = null == params ? new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.MATCH_PARENT) : params;
		mActivity.get().superSetContentView(view, params);
	}

	public View getNavigationBarView() {
		return mNavigationBarView;
	}

	@Override
	public void show() {
		mNavigationBarView.setVisibility(View.VISIBLE);
	}

	@Override
	public void hide() {
		mNavigationBarView.setVisibility(View.GONE);
	}

	@Override
	public void setPrimaryNavigationIcon(int res) {
		mNavigationBarView.setPrimaryNavigationIcon(res > 0 ? mActivity.get()
				.getResources().getDrawable(res) : null);
	}

	@Override
	public void setPrimaryNavigationIcon(Drawable drawable) {
		mNavigationBarView.setPrimaryNavigationIcon(drawable);
	}

	@Override
	public void setPrimaryNavigationText(int res) {
		mNavigationBarView.setPrimaryNavigationText(res > 0 ? mActivity.get()
				.getText(res) : null);
	}

	@Override
	public void setSecondaryNavigationIcon(int res) {
		mNavigationBarView.setSecondaryNavigationIcon(res > 0 ? mActivity.get()
				.getResources().getDrawable(res) : null);
	}

	@Override
	public void setSecondaryNavigationText(int res) {
		mNavigationBarView.setSecondaryNavigationText(res > 0 ? mActivity.get()
				.getText(res) : null);
	}

	@Override
	public void setTitle(int res) {
		setTitle(res > 0 ? mActivity.get().getString(res) : null);
	}

	@Override
	public void setTitle(CharSequence title) {
		mNavigationBarView.setNavigationTitle(title);
	}

	@Override
	public void setNavigationMode(int mode) {
		mNavigationMode = mode;
		resolveNavigationMode();
	}

	private void resolveNavigationMode() {
		TextView navigationTitle = mNavigationBarView.getNavigationTitleView();
		switch (mNavigationMode) {
		case NAVIGATION_MODE_STANDARD:
			navigationTitle.setOnClickListener(null);
			navigationTitle.setText(mCurrentActivitTitle);
			navigationTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			break;
		case NAVIGATION_MODE_LIST:
			navigationTitle.setOnClickListener(this);
			navigationTitle.setText("");
			navigationTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.list_navigation_indicator, 0);
			break;
		}
	}

	@Override
	public void setListNavigationCallbacks(SpinnerAdapter adapter,
			OnNavigationListener listener) {
		setListNavigationCallbacks(adapter, 0, listener);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.navigationTitle) {
			mNavigationBarView.getListNavigation().performClick();
			return;
		}

		NavigationBarItem item = null;
		final int id = v.getId();
		if (id == R.id.primaryNavigationItem) {
			if ((mNavigationBarView.getDisplayOptions() & NavigationBar.DISPLAY_PRIMARY_NAVIGATION_AS_UP) == NavigationBar.DISPLAY_PRIMARY_NAVIGATION_AS_UP) {
				mActivity.get().finish();
			}
			item = NavigationBarItem.PRIMARY_NAVIGATION_ITEM;
		} else if (id == R.id.secondaryNavigationItem) {
			item = NavigationBarItem.SECONDARY_NAVIGATION_ITEM;
		}
		if (null != mOnNavigationItemClickListener) {
			mOnNavigationItemClickListener.onNavigationItemClick(item);
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		Object object = parent.getItemAtPosition(position);
		mNavigationBarView.setNavigationTitle(object.toString());
		if (null != mNavigationListener) {
			mNavigationListener.onNavigationItemSelected(position, id);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

	public void setOnNavigationItemClickListener(
			OnNavigationItemClickListener listener) {
		mOnNavigationItemClickListener = listener;
	}

	public interface OnNavigationItemClickListener {
		public void onNavigationItemClick(NavigationBarItem item);
	}

	@Override
	public void setNavigationItemEnable(NavigationBarItem item, boolean isEnable) {
		switch (item) {
		case PRIMARY_NAVIGATION_ITEM:
			mNavigationBarView.getPrimaryNavigation().setEnabled(isEnable);
			break;
		case SECONDARY_NAVIGATION_ITEM:
			mNavigationBarView.getSecondaryNavigation().setEnabled(isEnable);
			break;
		}
	}

	@Override
	public void setNavigationItemSelected(NavigationBarItem item,
			boolean isSelected) {
		switch (item) {
		case PRIMARY_NAVIGATION_ITEM:
			mNavigationBarView.getPrimaryNavigation().setSelected(isSelected);
			break;
		case SECONDARY_NAVIGATION_ITEM:
			mNavigationBarView.getSecondaryNavigation().setSelected(isSelected);
			break;
		}
	}

	@Override
	public boolean isNavigationItemSelected(NavigationBarItem item) {
		switch (item) {
		case PRIMARY_NAVIGATION_ITEM:
			return mNavigationBarView.getPrimaryNavigation().isSelected();
		case SECONDARY_NAVIGATION_ITEM:
			return mNavigationBarView.getSecondaryNavigation().isSelected();
		}

		return false;
	}

	@Override
	public void setListNavigationCallbacks(SpinnerAdapter adapter,
			int selectedPosition, OnNavigationListener listener) {
		final Spinner listNavigation = mNavigationBarView.getListNavigation();
		listNavigation.setAdapter(adapter);
		listNavigation.setSelection(selectedPosition);
		mNavigationListener = listener;
	}

	@Override
	public void setBackgroundDrawable(Drawable drawable) {
		mNavigationBarView.setBackgroundDrawable(drawable);
	}

	@Override
	public void setCustomView(int res) {
		setCustomView(0 >= res ? null : mInflater.inflate(res,
				mNavigationBarView, false));
	}

	@Override
	public void setCustomView(View view) {
		setCustomView(view, null);
	}

	@Override
	public void setCustomView(View view, LayoutParams layoutParams) {
		mNavigationBarView.setCustomView(view, layoutParams);
	}

	@Override
	public void setIcon(int resId) {
		mNavigationBarView.setNavigationIcon(resId > 0 ? mActivity.get()
				.getResources().getDrawable(resId) : null);
	}

	@Override
	public void setIcon(Drawable icon) {
		mNavigationBarView.setNavigationIcon(icon);
	}

	@Override
	public void setDisplayOptions(int displayOptions) {
		mNavigationBarView.setDisplayOptions(displayOptions);
	}

	@Override
	public int getDisplayOptions() {
		return mNavigationBarView.getDisplayOptions();
	}
}
