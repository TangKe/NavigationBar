package me.tangke.navigationbar;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.content.res.Resources.Theme;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

/**
 * iPhone风格的{@link NavigationBar}实现
 * 
 * @author Tank
 * 
 */
abstract class NavigationBarImpl implements NavigationBar,
		OnItemSelectedListener, OnNavigationItemClickListener {
	private ViewGroup mNavigationBarContainer;
	private NavigationBarView mNavigationBarView;
	private ViewGroup mNavigationBarContentContainer;

	private OnNavigationListener mNavigationListener;

	private OnNavigationItemClickListener mOnNavigationItemClickListener;

	private int mNavigationMode;
	private boolean mIsNavigationBarOverlay;
	private boolean mHasNavigationBar;

	private WeakReference<Context> mContext;
	private LayoutInflater mInflater;

	private CharSequence mDefaultTitle;
	private Drawable mDefaultIcon;

	private TypedValue mValue = new TypedValue();

	public NavigationBarImpl(Context context) {
		mContext = new WeakReference<Context>(context);
		mInflater = LayoutInflater.from(context);

		ensureNavigationBarTheme();

		final Theme theme = mContext.get().getTheme();
		final TypedValue value = mValue;
		// 处理是否是覆盖的NavigationBar
		theme.resolveAttribute(R.attr.navigationBarOverlay, value, true);
		mIsNavigationBarOverlay = 0 != value.data;
		mNavigationBarContainer = (ViewGroup) mInflater
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

		navigationBarView.getPrimaryNavigationItemGroup()
				.setOnNavigationBarItemListener(this);
		navigationBarView.getTitleNavigationBarItem()
				.setOnNavigationBarItemListener(this);
		navigationBarView.getSecondaryNavigationItemGroup()
				.setOnNavigationBarItemListener(this);
		navigationBarView.getUpNavigationBarItem()
				.setOnNavigationBarItemListener(this);

		navigationBarView.getListNavigation().setOnItemSelectedListener(this);
	}

	public void ensureNavigationBarTheme() {
		final Theme theme = mContext.get().getTheme();
		theme.resolveAttribute(R.attr.isNavigationBarTheme, mValue, true);
		if (0 == mValue.data) {
			throw new IllegalStateException(
					"your theme must extend from Theme.NavigationBar");
		}
	}

	void setContentView(View view) {
		setContentView(view, null);
	}

	void setContentView(int res) {
		setContentView(mInflater.inflate(res, null), null);
	}

	abstract void setContentView(View view, ViewGroup.LayoutParams params);

	public void onCreate() {
		mDefaultTitle = getDefaultTitle();
		mDefaultIcon = getDefaultIcon();
		setTitle(mDefaultTitle);
		setIcon(mDefaultIcon);
		onReplaceContentView(mNavigationBarContainer,
				mNavigationBarContentContainer);
	}

	public abstract CharSequence getDefaultTitle();

	public abstract Drawable getDefaultIcon();

	protected abstract void onReplaceContentView(
			ViewGroup navigationBarContainer,
			ViewGroup navigationBarContentContainer);

	protected abstract void onNavigationUp();

	public boolean hasNavigationBar() {
		return mHasNavigationBar;
	}

	ViewGroup getNavigationBarView() {
		return mNavigationBarView;
	}

	ViewGroup getNavigationBarContainerView() {
		return mNavigationBarContainer;
	}

	ViewGroup getNavigationBarContentView() {
		return mNavigationBarContentContainer;
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
	public void setTitle(int res) {
		setTitle(res > 0 ? mContext.get().getString(res) : null);
	}

	@Override
	public void setTitle(CharSequence title) {
		mNavigationBarView.getTitleNavigationBarItem().setTitle(title);
	}

	@Override
	public void setNavigationMode(int mode) {
		mNavigationMode = mode;
		resolveNavigationMode();
	}

	private void resolveNavigationMode() {
		NavigationBarItem titleNavigationBarItem = mNavigationBarView
				.getPrimaryNavigationItemGroup();
		switch (mNavigationMode) {
		case NAVIGATION_MODE_STANDARD:
			titleNavigationBarItem.setTitle(mDefaultTitle);
			titleNavigationBarItem.setEnabled(false);
			break;
		case NAVIGATION_MODE_LIST:
			titleNavigationBarItem.setTitle("");
			titleNavigationBarItem.setEnabled(true);
			// TODO 修复重构后列表导航
			break;
		}
	}

	@Override
	public void setListNavigationCallbacks(SpinnerAdapter adapter,
			OnNavigationListener listener) {
		setListNavigationCallbacks(adapter, 0, listener);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		Object object = parent.getItemAtPosition(position);
		NavigationBarItem titleNavigationBarItem = mNavigationBarView
				.getTitleNavigationBarItem();
		titleNavigationBarItem.setTitle(object.toString());
		if (null != mNavigationListener) {
			mNavigationListener.onNavigationItemSelected(position, id);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

	void setOnNavigationItemClickListener(OnNavigationItemClickListener listener) {
		mOnNavigationItemClickListener = listener;
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
		setCustomView(view, (LayoutParams) view.getLayoutParams());
	}

	@Override
	public void setCustomView(View view, LayoutParams layoutParams) {
		layoutParams = null == layoutParams ? new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
				: layoutParams;
		mNavigationBarView.setCustomView(view, layoutParams);
	}

	@Override
	public void setIcon(int resId) {
		setIcon(resId > 0 ? mContext.get().getResources().getDrawable(resId)
				: null);
	}

	@Override
	public void setIcon(Drawable icon) {
		mNavigationBarView.getTitleNavigationBarItem().setIcon(icon);
	}

	@Override
	public void setDisplayOptions(int displayOptions) {
		mNavigationBarView.setDisplayOptions(displayOptions);
	}

	@Override
	public int getDisplayOptions() {
		return mNavigationBarView.getDisplayOptions();
	}

	@Override
	public NavigationBarItemGroup getPrimaryNavigationBarItemGroup() {
		return mNavigationBarView.getPrimaryNavigationItemGroup();
	}

	@Override
	public NavigationBarItemGroup getSecondaryNavigationBarItemGroup() {
		return mNavigationBarView.getSecondaryNavigationItemGroup();
	}

	@Override
	public void onNavigationItemClick(NavigationBarItem item) {
		final int id = item.id;
		if (id == R.id.navigationTitle) {
			mNavigationBarView.getListNavigation().performClick();
			return;
		}

		if (item.id == R.id.upNavigationItem) {
			onNavigationUp();
		}
		if (null != mOnNavigationItemClickListener) {
			mOnNavigationItemClickListener.onNavigationItemClick(item);
		}
	}

	@Override
	public NavigationBarItem newNavigationBarItem(int id, CharSequence title,
			int icon, int gravity) {
		return mNavigationBarView
				.newNavigationBarItem(id, title, icon, gravity);
	}
}
