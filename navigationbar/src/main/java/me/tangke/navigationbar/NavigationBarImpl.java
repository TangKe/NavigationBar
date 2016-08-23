package me.tangke.navigationbar;

import android.app.Activity;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.lang.ref.WeakReference;

/**
 * {@link NavigationBar} implements
 *
 * @author Tank
 */
abstract class NavigationBarImpl implements NavigationBar,
        OnItemSelectedListener, OnNavigationItemClickListener, ViewTreeObserver
                .OnGlobalLayoutListener {
    private ViewGroup mNavigationBarContainer;
    private NavigationBarView mNavigationBarView;
    private ViewGroup mNavigationBarContentContainer;

    private OnNavigationListener mNavigationListener;

    private OnNavigationItemClickListener mOnNavigationItemClickListener;

    private boolean mIsNavigationBarOverlay;
    private boolean mHasNavigationBar;

    private WeakReference<Activity> mContext;
    private LayoutInflater mInflater;

    private CharSequence mDefaultTitle;
    private Drawable mDefaultIcon;

    private TypedValue mValue = new TypedValue();

    private boolean mLastIsVisible;
    private int mLastHeight;

    private boolean mIsTranslucentStatusBar;
    private int mStatusBarHeight;

    public NavigationBarImpl(Activity context) {
        mContext = new WeakReference<>(context);
        mInflater = LayoutInflater.from(context);

        ensureNavigationBarTheme();

        final Resources resources = context.getResources();
        final Theme theme = context.getTheme();
        final TypedValue value = mValue;
        mNavigationBarContainer = (ViewGroup) mInflater.inflate(R.layout.layout_navigation_bar,
                null);

        theme.resolveAttribute(R.attr.windowNavigationBarOverlay, value, true);
        mIsNavigationBarOverlay = 0 != value.data;

        mNavigationBarContentContainer = (ViewGroup) mNavigationBarContainer
                .findViewById(R.id.navigationBarContentContainer);

        final NavigationBarView navigationBarView = mNavigationBarView = (NavigationBarView)
                mNavigationBarContainer.findViewById(R.id.navigationBar);

        theme.resolveAttribute(R.attr.windowNavigationBar, value, true);
        mHasNavigationBar = 0 != value.data;
        navigationBarView.setVisibility(mHasNavigationBar ? View.VISIBLE
                : View.GONE);

        theme.resolveAttribute(android.R.attr.windowTranslucentStatus, value, true);
        mIsTranslucentStatusBar = 0 != value.data;

        int statusRes = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (statusRes > 0) {
            mStatusBarHeight = resources.getDimensionPixelSize(statusRes);
        }

        navigationBarView.getPrimaryNavigationItemGroup()
                .setOnNavigationBarItemListener(this);
        navigationBarView.getTitleNavigationBarItem()
                .setOnNavigationBarItemListener(this);
        navigationBarView.getSecondaryNavigationItemGroup()
                .setOnNavigationBarItemListener(this);
        navigationBarView.getUpNavigationBarItem()
                .setOnNavigationBarItemListener(this);

        navigationBarView.getListNavigation().setOnItemSelectedListener(this);
        navigationBarView.getViewTreeObserver().addOnGlobalLayoutListener(this);
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

    protected abstract boolean onNavigateUp();

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
        mNavigationBarView.setNavigationMode(mode);
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
            return;
        }

        if (null != mOnNavigationItemClickListener) {
            mOnNavigationItemClickListener.onNavigationItemClick(item);
        }

        if (item.id == R.id.upNavigationItem) {
            if (!onNavigateUp()) {
                mContext.get().finish();
            }
        }
    }

    @Override
    public void setNavigationBarColorPrimary(int color) {
        mNavigationBarView.setNavigationBarColorPrimary(color);
    }

    @Override
    public void setNavigationBarColorAccent(int color) {
        mNavigationBarView.setNavigationBarColorAccent(color);
    }

    @Override
    public void setNavigationBarTextColorPrimary(int color) {
        mNavigationBarView.setNavigationBarTextColorPrimary(color);
    }

    @Override
    public NavigationBarItem newNavigationBarItem(int id, CharSequence title,
                                                  int icon, int gravity) {
        return mNavigationBarView
                .newNavigationBarItem(id, title, icon, gravity);
    }

    @Override
    public NavigationBarItem newNavigationBarItem(int id, int title, int icon,
                                                  int gravity) {
        return newNavigationBarItem(id, 0 < title ? mContext.get().getString(title) : null, icon,
                gravity);
    }

    @Override
    public void onGlobalLayout() {
        resolveContentOffset();
    }

    private void resolveContentOffset() {
        final View navigationBarContentContainer = mNavigationBarContentContainer;
        boolean isVisible = View.VISIBLE == mNavigationBarView.getVisibility();
        int height = mNavigationBarView.getHeight();
        boolean changed = isVisible != mLastIsVisible || mLastHeight != height;
        if (changed) {
            int paddingTop = mIsNavigationBarOverlay || !isVisible ? (mIsTranslucentStatusBar ? mStatusBarHeight : 0) :
                    mNavigationBarView.getHeight();
            navigationBarContentContainer.setPadding(navigationBarContentContainer.getPaddingLeft(), paddingTop, navigationBarContentContainer.getPaddingRight(), navigationBarContentContainer.getPaddingBottom());
        }
        mLastIsVisible = isVisible;
        mLastHeight = height;
    }
}
