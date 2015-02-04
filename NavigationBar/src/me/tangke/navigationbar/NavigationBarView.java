package me.tangke.navigationbar;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class NavigationBarView extends FrameLayout {
	private ViewGroup mNavigationCustomContainer;
	private Button mPrimaryNavigationButton;
	private Button mSecondaryNavigationButton;

	private TextView mNavigationTitle;
	private Spinner mListNavigation;

	private int mDisplayOptions;
	private Drawable mNavigationIcon;
	private Drawable mUpIndicator;
	private CharSequence mUpIndicatorText;
	private CharSequence mPrimaryNavigationText;
	private Drawable mPrimaryNavigationIcon;
	private Drawable mSecondaryNavigationIcon;

	private ColorFilter mColorPrimaryFilter;

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

		theme.resolveAttribute(R.attr.colorPrimary, value, true);
		if (TypedValue.TYPE_REFERENCE == value.type) {
			mColorPrimaryFilter = new PorterDuffColorFilter(
					resources.getColor(value.resourceId), Mode.SRC_IN);
		} else {
			mColorPrimaryFilter = new PorterDuffColorFilter(value.data,
					Mode.SRC_IN);
		}

		theme.resolveAttribute(R.attr.upIndicator, value, true);
		mUpIndicator = 0 < value.resourceId ? resources
				.getDrawable(value.resourceId) : null;
		mUpIndicator.setColorFilter(mColorPrimaryFilter);

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.NavigationBar, defStyleAttr,
				R.style.Widget_NavigationBar);
		mNavigationTitle.setTextAppearance(context, a.getResourceId(
				R.styleable.NavigationBar_titleTextStyle,
				R.style.TextAppearance_NavigationBar_Title));
		int navigationTextAppearance = a.getResourceId(
				R.styleable.NavigationBar_navigationTextStyle,
				R.style.TextAppearance_NavigationBar_Navigation);
		mPrimaryNavigationButton.setTextAppearance(context,
				navigationTextAppearance);
		mSecondaryNavigationButton.setTextAppearance(context,
				navigationTextAppearance);
		mDisplayOptions = a.getInteger(
				R.styleable.NavigationBar_displayOptions,
				NavigationBar.DISPLAY_SHOW_TITLE);
		applyDisplayOptions();
		a.recycle();
	}

	public NavigationBarView(Context context, AttributeSet attrs) {
		this(context, attrs, R.attr.navigationTextStyle);
	}

	public NavigationBarView(Context context) {
		this(context, null);
	}

	private void prepareNavigationBarContent() {
		LayoutInflater.from(getContext()).inflate(
				R.layout.layout_navigation_bar_content, this);
		mPrimaryNavigationButton = (Button) findViewById(R.id.primaryNavigationItem);
		mSecondaryNavigationButton = (Button) findViewById(R.id.secondaryNavigationItem);
		mNavigationTitle = (TextView) findViewById(R.id.navigationTitle);

		mListNavigation = (Spinner) findViewById(R.id.listNavigation);
		mNavigationCustomContainer = (ViewGroup) findViewById(R.id.navigationCustomContainer);
	}

	private void applyDisplayOptions() {
		final int displayOptions = mDisplayOptions;
		boolean isPrimaryNavigationAsUp = (displayOptions & NavigationBar.DISPLAY_PRIMARY_NAVIGATION_AS_UP) == NavigationBar.DISPLAY_PRIMARY_NAVIGATION_AS_UP;
		mPrimaryNavigationButton
				.setCompoundDrawablesWithIntrinsicBounds(
						isPrimaryNavigationAsUp ? mUpIndicator
								: mPrimaryNavigationIcon, null, null, null);
		mPrimaryNavigationButton
				.setText(isPrimaryNavigationAsUp ? mUpIndicatorText
						: mPrimaryNavigationText);
		mNavigationTitle
				.setCompoundDrawablesWithIntrinsicBounds(
						(displayOptions & NavigationBar.DISPLAY_SHOW_ICON) == NavigationBar.DISPLAY_SHOW_ICON ? mNavigationIcon
								: null, null, null, null);
		mNavigationTitle
				.setVisibility((displayOptions & NavigationBar.DISPLAY_SHOW_TITLE) == NavigationBar.DISPLAY_SHOW_TITLE ? View.VISIBLE
						: View.INVISIBLE);

		mNavigationCustomContainer
				.setVisibility((displayOptions & NavigationBar.DISPLAY_SHOW_CUSTOM) == NavigationBar.DISPLAY_SHOW_CUSTOM ? View.VISIBLE
						: View.INVISIBLE);
	}

	public void setDisplayOptions(int displayOptions) {
		mDisplayOptions = displayOptions;
		applyDisplayOptions();
	}

	public int getDisplayOptions() {
		return mDisplayOptions;
	}

	public void setPrimaryNavigationText(CharSequence text) {
		mPrimaryNavigationText = text;
		applyDisplayOptions();
	}

	public void setPrimaryNavigationIcon(Drawable icon) {
		if (null != mPrimaryNavigationIcon) {
			mPrimaryNavigationIcon.setColorFilter(null);
		}
		mPrimaryNavigationIcon = icon;
		if (null != icon) {
			icon.setColorFilter(mColorPrimaryFilter);
		}
		applyDisplayOptions();
	}

	public void setSecondaryNavigationText(CharSequence text) {
		mSecondaryNavigationButton.setText(text);
	}

	public void setSecondaryNavigationIcon(Drawable icon) {
		if (null != mSecondaryNavigationIcon) {
			mSecondaryNavigationIcon.setColorFilter(null);
		}
		mSecondaryNavigationIcon = icon;
		if (null != icon) {
			icon.setColorFilter(mColorPrimaryFilter);
		}
		mSecondaryNavigationButton.setCompoundDrawablesWithIntrinsicBounds(
				null, null, icon, null);
	}

	public Button getPrimaryNavigation() {
		return mPrimaryNavigationButton;
	}

	public Button getSecondaryNavigation() {
		return mSecondaryNavigationButton;
	}

	public Spinner getListNavigation() {
		return mListNavigation;
	}

	public void setNavigationTitle(CharSequence title) {
		mNavigationTitle.setText(title);
	}

	public TextView getNavigationTitleView() {
		return mNavigationTitle;
	}

	public void setNavigationIcon(Drawable icon) {
		mNavigationIcon = icon;
		applyDisplayOptions();
	}

	public void setCustomView(View view, LayoutParams layoutParams) {
		mNavigationCustomContainer.removeAllViews();
		mNavigationCustomContainer.addView(view, layoutParams);
	}
}
