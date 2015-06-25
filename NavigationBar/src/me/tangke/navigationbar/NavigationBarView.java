package me.tangke.navigationbar;

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

class NavigationBarView extends FrameLayout {
	private ViewGroup mNavigationCustomContainer;

	private Spinner mListNavigation;

	private int mDisplayOptions;
	private Drawable mUpIndicator;
	private CharSequence mUpIndicatorText;
	private CharSequence mPrimaryNavigationText;

	private NavigationBarItemGroup mPrimaryNavigationBarItem;
	private NavigationBarTitle mTitleNavigationBarItem;
	private NavigationBarItemGroup mSecondaryNavigationBarItem;
	private NavigationBarButton mUpNavigationBarItem;

	private int mNavigationTextAppearance;

	public NavigationBarView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);

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

		mNavigationTextAppearance = a.getResourceId(
				R.styleable.NavigationBar_navigationTextStyle,
				R.style.TextAppearance_NavigationBar_Navigation);

		prepareNavigationBarContent();

		mTitleNavigationBarItem.text.setTextAppearance(context, a
				.getResourceId(R.styleable.NavigationBar_titleTextStyle,
						R.style.TextAppearance_NavigationBar_Title));
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
		final LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.layout_navigation_bar_content, this);
		ViewGroup primaryNavigationItemContainer = (ViewGroup) findViewById(R.id.primaryNavigationItemContainer);
		mPrimaryNavigationBarItem = new NavigationBarItemGroup(context,
				R.id.primaryNavigationItemContainer,
				primaryNavigationItemContainer, Gravity.LEFT, "left");
		mSecondaryNavigationBarItem = new NavigationBarItemGroup(
				context,
				R.id.secondaryNavigationItemContainer,
				(ViewGroup) findViewById(R.id.secondaryNavigationItemContainer),
				Gravity.END, "right");
		mTitleNavigationBarItem = new NavigationBarTitle(context,
				R.id.navigationTitle,
				(TextView) findViewById(R.id.navigationTitle), Gravity.LEFT,
				"title");

		mListNavigation = (Spinner) findViewById(R.id.listNavigation);
		mNavigationCustomContainer = (ViewGroup) findViewById(R.id.navigationCustomContainer);

		mUpNavigationBarItem = new NavigationBarButton(context,
				R.id.upNavigationItem, (TextView) inflater.inflate(
						R.layout.layout_navigation_bar_item,
						primaryNavigationItemContainer, false), Gravity.LEFT,
				"up");
		mUpNavigationBarItem.setIcon(mUpIndicator);
		mUpNavigationBarItem.text.setTextAppearance(context,
				mNavigationTextAppearance);
		mUpNavigationBarItem.setTitle(mPrimaryNavigationText);
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
		if (isPrimaryNavigationAsUp) {
			mPrimaryNavigationBarItem.addNavigationBarItem(
					mUpNavigationBarItem, 0);
		} else {
			mPrimaryNavigationBarItem
					.removeNavigationBarItem(mUpNavigationBarItem);
		}
		mTitleNavigationBarItem
				.setVisible((displayOptions & NavigationBar.DISPLAY_SHOW_TITLE) == NavigationBar.DISPLAY_SHOW_TITLE);

		mNavigationCustomContainer
				.setVisibility((displayOptions & NavigationBar.DISPLAY_SHOW_CUSTOM) == NavigationBar.DISPLAY_SHOW_CUSTOM ? View.VISIBLE
						: View.GONE);
	}

	private boolean isPrimaryNavigationAsUp() {
		return (mDisplayOptions & NavigationBar.DISPLAY_PRIMARY_NAVIGATION_AS_UP) == NavigationBar.DISPLAY_PRIMARY_NAVIGATION_AS_UP;
	}

	public NavigationBarItemGroup getPrimaryNavigationItemGroup() {
		return mPrimaryNavigationBarItem;
	}

	public NavigationBarItemGroup getSecondaryNavigationItemGroup() {
		return mSecondaryNavigationBarItem;
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

	public NavigationBarItem newNavigationBarItem(int id, CharSequence title,
			int icon, int gravity, String tag) {
		final Context context = getContext();
		NavigationBarButton item = new NavigationBarButton(context, id,
				(TextView) LayoutInflater.from(context).inflate(
						R.layout.layout_navigation_bar_item, null), gravity,
				tag);
		item.text.setTextAppearance(context, mNavigationTextAppearance);
		item.setIcon(icon);
		item.setTitle(title);
		return item;
	}
}
