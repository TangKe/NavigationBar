package me.tangke.navigationbar;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

class NavigationBarView extends FrameLayout implements OnGlobalLayoutListener {
    private ViewGroup mNavigationCustomContainer;

    private Spinner mListNavigation;

    private int mDisplayOptions;
    private int mNavigationMode;
    private Drawable mUpIndicator;
    private CharSequence mUpIndicatorText;

    private NavigationBarItemGroup mPrimaryNavigationBarItemGroup;
    private NavigationBarTitle mTitleNavigationBarItem;
    private NavigationBarItemGroup mSecondaryNavigationBarItemGroup;
    private NavigationBarButton mUpNavigationBarItem;

    //主要颜色
    private int mNavigationBarColorPrimary;
    private int mNavigationBarColorAccent;
    private int mNavigationBarTextColorPrimary;

    private int mNavigationTextAppearance;
    private int mNavigationBarTitleTextStyle;

    public NavigationBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final Theme theme = context.getTheme();
        final Resources resources = context.getResources();
        TypedValue value = new TypedValue();

        theme.resolveAttribute(R.attr.navigationBarUpIndicatorText, value, true);
        mUpIndicatorText = 0 < value.resourceId ? resources.getText(value.resourceId) : null;

        theme.resolveAttribute(R.attr.navigationBarUpIndicator, value, true);
        mUpIndicator = 0 < value.resourceId ? resources.getDrawable(value.resourceId) : null;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NavigationBar,
                defStyleAttr, R.style.Widget_NavigationBar);

        mNavigationTextAppearance = a.getResourceId(R.styleable.NavigationBar_navigationTextStyle,
                R.style.TextAppearance_NavigationBar_Navigation);
        mNavigationBarTitleTextStyle = a.getResourceId(R.styleable
                .NavigationBar_navigationBarTitleTextStyle, R.style
                .TextAppearance_NavigationBar_Title);

        prepareNavigationBarContent();
        theme.resolveAttribute(R.attr.navigationBarColorPrimary, value, true);
        setNavigationBarColorPrimary(value.data);

        theme.resolveAttribute(R.attr.navigationBarColorAccent, value, true);
        setNavigationBarColorAccent(value.data);

        theme.resolveAttribute(R.attr.navigationBarTextColorPrimary, value, true);
        setNavigationBarTextColorPrimary(value.data);


        mDisplayOptions = a.getInteger(R.styleable.NavigationBar_navigationBarDisplayOptions,
                NavigationBar.DISPLAY_SHOW_TITLE);
        applyDisplayOptions();
        a.recycle();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    public NavigationBarView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.navigationTextStyle);
    }

    public NavigationBarView(Context context) {
        this(context, null);
    }

    private void prepareNavigationBarContent() {
        final Context context = getContext();
        final LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.layout_navigation_bar_content, this);
        ViewGroup primaryNavigationItemContainer = (ViewGroup) findViewById(R.id
                .primaryNavigationItemContainer);
        mPrimaryNavigationBarItemGroup = new NavigationBarItemGroup(context, R.id
                .primaryNavigationItemContainer,
                primaryNavigationItemContainer, Gravity.LEFT);
        mSecondaryNavigationBarItemGroup = new NavigationBarItemGroup(context, R.id
                .secondaryNavigationItemContainer,
                (ViewGroup) findViewById(R.id.secondaryNavigationItemContainer), Gravity.END);
        mTitleNavigationBarItem = new NavigationBarTitle(context, R.id.navigationTitle,
                (TextView) findViewById(R.id.navigationTitle), Gravity.LEFT,
                mNavigationBarTitleTextStyle);

        mListNavigation = (Spinner) findViewById(R.id.listNavigation);
        mNavigationCustomContainer = (ViewGroup) findViewById(R.id.navigationCustomContainer);

        mUpNavigationBarItem = new NavigationBarButton(context, R.id.upNavigationItem,
                (TextView) inflater.inflate(R.layout.layout_navigation_bar_item,
                        primaryNavigationItemContainer, false), Gravity.LEFT,
                mNavigationTextAppearance);
        mUpNavigationBarItem.setIcon(mUpIndicator);
        mUpNavigationBarItem.setTitle(mUpIndicatorText);
        mUpNavigationBarItem.setVisible(false);
        mPrimaryNavigationBarItemGroup.addNavigationBarItem(mUpNavigationBarItem, 0);
    }

    @Override
    @TargetApi(16)
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (Build.VERSION_CODES.JELLY_BEAN <= Build.VERSION.SDK_INT) {
            getViewTreeObserver().removeOnGlobalLayoutListener(this);
        } else {
            getViewTreeObserver().removeGlobalOnLayoutListener(this);
        }
    }

    public void setDisplayOptions(int displayOptions) {
        mDisplayOptions = displayOptions;
        applyDisplayOptions();
    }

    public void setNavigationMode(int navigationMode) {
        mNavigationMode = navigationMode;
        resolveNavigationMode();
    }

    private void resolveNavigationMode() {
        switch (mNavigationMode) {
            case NavigationBar.NAVIGATION_MODE_STANDARD:
                mTitleNavigationBarItem.setVisible(true);
                mListNavigation.setVisibility(View.GONE);
                break;
            case NavigationBar.NAVIGATION_MODE_LIST:
                mTitleNavigationBarItem.setVisible(false);
                mListNavigation.setVisibility(View.VISIBLE);
                break;
        }
    }

    public int getDisplayOptions() {
        return mDisplayOptions;
    }

    private void applyDisplayOptions() {
        final int displayOptions = mDisplayOptions;
        boolean isPrimaryNavigationAsUp = isPrimaryNavigationAsUp();
        mUpNavigationBarItem.setVisible(isPrimaryNavigationAsUp);
        mTitleNavigationBarItem
                .setVisible((displayOptions & NavigationBar.DISPLAY_SHOW_TITLE) == NavigationBar
                        .DISPLAY_SHOW_TITLE && mNavigationMode != NavigationBar
                        .NAVIGATION_MODE_LIST);

        mTitleNavigationBarItem
                .setIconVisible((displayOptions & NavigationBar.DISPLAY_SHOW_LOGO) ==
                        NavigationBar.DISPLAY_SHOW_LOGO);

        mNavigationCustomContainer
                .setVisibility((displayOptions & NavigationBar.DISPLAY_SHOW_CUSTOM) ==
                        NavigationBar.DISPLAY_SHOW_CUSTOM
                        ? View.VISIBLE : View.GONE);
    }

    private boolean isPrimaryNavigationAsUp() {
        return (mDisplayOptions
                & NavigationBar.DISPLAY_PRIMARY_NAVIGATION_AS_UP) == NavigationBar
                .DISPLAY_PRIMARY_NAVIGATION_AS_UP;
    }

    public NavigationBarItemGroup getPrimaryNavigationItemGroup() {
        return mPrimaryNavigationBarItemGroup;
    }

    public NavigationBarItemGroup getSecondaryNavigationItemGroup() {
        return mSecondaryNavigationBarItemGroup;
    }

    public NavigationBarItem getUpNavigationBarItem() {
        return mUpNavigationBarItem;
    }

    public NavigationBarItem getTitleNavigationBarItem() {
        return mTitleNavigationBarItem;
    }

    public Spinner getListNavigation() {
        return mListNavigation;
    }

    public void setCustomView(View view, LayoutParams layoutParams) {
        mNavigationCustomContainer.removeAllViews();
        mNavigationCustomContainer.addView(view, layoutParams);
    }

    public NavigationBarItem newNavigationBarItem(int id, CharSequence title, int icon, int
            gravity) {
        final Context context = getContext();
        NavigationBarButton item = new NavigationBarButton(context, id, (TextView) LayoutInflater
                .from(context).inflate(R.layout.layout_navigation_bar_item,
                        getPrimaryNavigationItemGroup().group, false), gravity,
                mNavigationTextAppearance);
        item.setIcon(icon);
        item.setTitle(title);
        return item;
    }

    /**
     * indicate whether the title is overlap by navigation buttons
     */
    private void resolveTitleOverlap() {
        final View titleView = mTitleNavigationBarItem.view;

        int paddingLeft = Math.max(0, mPrimaryNavigationBarItemGroup.view.getRight() - titleView
                .getLeft()),
                paddingRight = Math.max(0, titleView.getRight() -
                        mSecondaryNavigationBarItemGroup.view.getLeft());
        TruncateAt ellipsize = 0 < paddingLeft && 0 < paddingRight ? TruncateAt.MIDDLE
                : (0 < paddingLeft ? TruncateAt.START : TruncateAt.END);
        mTitleNavigationBarItem.text.setEllipsize(ellipsize);

        titleView.setPadding(paddingLeft, titleView.getPaddingTop(), paddingRight, titleView
                .getPaddingBottom());
    }

    @Override
    public void onGlobalLayout() {
        resolveTitleOverlap();
    }

    public void setNavigationBarColorPrimary(int color) {
        mNavigationBarColorPrimary = color;
        //处理背景
        Drawable background = getBackground();
        if (null != background && color != Color.TRANSPARENT) {
            if (background instanceof ColorDrawable && Build.VERSION.SDK_INT >= Build
                    .VERSION_CODES.HONEYCOMB) {
                ((ColorDrawable) background).setColor(color);
            } else {
                background.setColorFilter(new PorterDuffColorFilter
                        (mNavigationBarColorPrimary, PorterDuff.Mode.SRC_IN));
            }
        }
    }

    public void setNavigationBarColorAccent(int color) {
        mNavigationBarColorAccent = color;
        //处理导航
        mPrimaryNavigationBarItemGroup.setTintColor(color);
        mSecondaryNavigationBarItemGroup.setTintColor(color);
    }

    public void setNavigationBarTextColorPrimary(int color) {
        mNavigationBarTextColorPrimary = color;
        mTitleNavigationBarItem.setTintColor(color);

        //处理列表导航
        Drawable listBackground = mListNavigation.getBackground();
        if (null != listBackground) {
            listBackground.setColorFilter(new PorterDuffColorFilter
                    (mNavigationBarTextColorPrimary, PorterDuff.Mode.SRC_IN));
        }
    }

    public int getNavigationBarColorPrimary() {
        return mNavigationBarColorPrimary;
    }

    public int getNavigationBarColorAccent() {
        return mNavigationBarColorAccent;
    }

    public int getNavigationBarTextColorPrimary() {
        return mNavigationBarTextColorPrimary;
    }
}
