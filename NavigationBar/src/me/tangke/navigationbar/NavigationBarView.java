package me.tangke.navigationbar;

import me.tangke.navigationbar.NavigationBarItem.Callback;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

class NavigationBarView extends FrameLayout implements Callback {
	private ViewGroup mNavigationCustomContainer;

	private Spinner mListNavigation;

	private int mDisplayOptions;
	private Drawable mUpIndicator;
	private CharSequence mUpIndicatorText;
	private CharSequence mPrimaryNavigationText;
	private Drawable mPrimaryNavigationIcon;
	private Drawable mNavigationIcon;

	private NavigationBarItem mPrimaryNavigationBarItem;
	private NavigationBarItem mTitleNavigationBarItem;
	private NavigationBarItem mSecondaryNavigationBarItem;

	public NavigationBarView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		prepareNavigationBarContent();

		final Theme theme = context.getTheme();
		final Resources resources = context.getResources();
		TypedValue value = new TypedValue();

		theme.resolveAttribute(R.attr.upIndicatorText, value, true);
		mUpIndicatorText = 0 < value.resourceId ? resources
				.getText(value.resourceId) : null;

		theme.resolveAttribute(R.attr.upIndicator, value, true);
		mUpIndicator = 0 < value.resourceId ? resources
				.getDrawable(value.resourceId) : null;

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.NavigationBar, defStyleAttr,
				R.style.Widget_NavigationBar);
		mTitleNavigationBarItem.view.setTextAppearance(context, a
				.getResourceId(R.styleable.NavigationBar_titleTextStyle,
						R.style.TextAppearance_NavigationBar_Title));
		int navigationTextAppearance = a.getResourceId(
				R.styleable.NavigationBar_navigationTextStyle,
				R.style.TextAppearance_NavigationBar_Navigation);
		mPrimaryNavigationBarItem.view.setTextAppearance(context,
				navigationTextAppearance);
		mSecondaryNavigationBarItem.view.setTextAppearance(context,
				navigationTextAppearance);
		mDisplayOptions = a.getInteger(
				R.styleable.NavigationBar_displayOptions,
				NavigationBar.DISPLAY_SHOW_TITLE);
		a.recycle();
	}

	public NavigationBarView(Context context, AttributeSet attrs) {
		this(context, attrs, R.attr.navigationTextStyle);
	}

	public NavigationBarView(Context context) {
		this(context, null);
	}

	private void prepareNavigationBarContent() {
		final Context context = getContext();
		LayoutInflater.from(context).inflate(
				R.layout.layout_navigation_bar_content, this);
		mPrimaryNavigationBarItem = new NavigationBarItem(context,
				R.id.primaryNavigationItem,
				(TextView) findViewById(R.id.primaryNavigationItem),
				Gravity.LEFT, "left");
		mPrimaryNavigationBarItem.setCallback(this);
		mSecondaryNavigationBarItem = new NavigationBarItem(context,
				R.id.secondaryNavigationItem,
				(TextView) findViewById(R.id.secondaryNavigationItem),
				Gravity.RIGHT, "right");
		mSecondaryNavigationBarItem.setCallback(this);
		mTitleNavigationBarItem = new NavigationBarItem(context,
				R.id.navigationTitle,
				(TextView) findViewById(R.id.navigationTitle), Gravity.LEFT,
				"test");
		mTitleNavigationBarItem.setCallback(this);

		mListNavigation = (Spinner) findViewById(R.id.listNavigation);
		mNavigationCustomContainer = (ViewGroup) findViewById(R.id.navigationCustomContainer);
	}

	public void setDisplayOptions(int displayOptions) {
		mDisplayOptions = displayOptions;
		applyDisplayOptions();
	}

	public int getDisplayOptions() {
		return mDisplayOptions;
	}

	private void applyDisplayOptions() {
		final int displayOptions = mDisplayOptions;
		boolean isPrimaryNavigationAsUp = isPrimaryNavigationAsUp();
		mPrimaryNavigationBarItem
				.setIcon(isPrimaryNavigationAsUp ? mUpIndicator
						: mPrimaryNavigationIcon);
		mPrimaryNavigationBarItem
				.setTitle(isPrimaryNavigationAsUp ? mUpIndicatorText
						: mPrimaryNavigationText);
		mTitleNavigationBarItem
				.setIcon((displayOptions & NavigationBar.DISPLAY_SHOW_ICON) == NavigationBar.DISPLAY_SHOW_ICON ? mNavigationIcon
						: null);
		mTitleNavigationBarItem
				.setVisible((displayOptions & NavigationBar.DISPLAY_SHOW_TITLE) == NavigationBar.DISPLAY_SHOW_TITLE);

		mNavigationCustomContainer
				.setVisibility((displayOptions & NavigationBar.DISPLAY_SHOW_CUSTOM) == NavigationBar.DISPLAY_SHOW_CUSTOM ? View.VISIBLE
						: View.GONE);
	}

	private boolean isPrimaryNavigationAsUp() {
		return (mDisplayOptions & NavigationBar.DISPLAY_PRIMARY_NAVIGATION_AS_UP) == NavigationBar.DISPLAY_PRIMARY_NAVIGATION_AS_UP;
	}

	public NavigationBarItem getPrimaryNavigationItem() {
		return mPrimaryNavigationBarItem;
	}

	public NavigationBarItem getSecondaryNavigationItem() {
		return mSecondaryNavigationBarItem;
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

	@Override
	public void onIconChanged(NavigationBarItem item, Drawable icon) {
		if (item == mPrimaryNavigationBarItem) {
			mPrimaryNavigationIcon = mUpIndicator == icon ? mPrimaryNavigationIcon
					: icon;
		} else if (item == mTitleNavigationBarItem) {
			mNavigationIcon = icon;
		}

		applyDisplayOptions();
	}
}
