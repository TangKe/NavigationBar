package me.tangke.navigationbar;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * 导航选项定义
 * 
 * @author Tank
 * 
 */
public class NavigationBarItem implements OnClickListener {
	private static ColorFilter sColorFilter;

	int id;
	TextView view;

	CharSequence title;
	Drawable icon;

	int gravity;

	OnNavigationItemClickListener onNavigationItemClickListener;
	WeakReference<Callback> callback;

	private WeakReference<Context> mContext;

	NavigationBarItem(Context context, int id, TextView view, int gravity) {
		this.id = id;
		this.view = view;
		this.gravity = gravity;
		view.setOnClickListener(this);
		this.mContext = new WeakReference<Context>(context);

		if (null == sColorFilter) {
			final Theme theme = context.getTheme();
			final Resources resources = context.getResources();
			TypedValue value = new TypedValue();

			theme.resolveAttribute(R.attr.colorPrimary, value, true);
			if (TypedValue.TYPE_REFERENCE == value.type) {
				sColorFilter = new PorterDuffColorFilter(
						resources.getColor(value.resourceId), Mode.SRC_IN);
			} else {
				sColorFilter = new PorterDuffColorFilter(value.data,
						Mode.SRC_IN);
			}
		}
	}

	public int getId() {
		return this.id;
	}

	public View getView() {
		return view;
	}

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(int res) {
		mContext.get().getResources().getDrawable(res);
	}

	public void setIcon(Drawable icon) {
		if (null != this.icon) {
			this.icon.setColorFilter(null);
		}
		this.icon = icon;

		if (null != icon) {
			icon.setColorFilter(sColorFilter);
		}

		Drawable left = null, right = null, top = null, bottom = null;
		switch (gravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
		case Gravity.LEFT:
			left = icon;
			break;
		case Gravity.RIGHT:
			right = icon;
			break;
		}

		switch (gravity & Gravity.VERTICAL_GRAVITY_MASK) {
		case Gravity.TOP:
			top = icon;
			break;
		case Gravity.BOTTOM:
			bottom = icon;
			break;
		}

		view.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
		if (null != callback && null != callback.get()) {
			callback.get().onIconChanged(this, icon);
		}
	}

	public CharSequence getTitle() {
		return title;
	}

	public void setTitle(int res) {
		setTitle(mContext.get().getText(res));
	}

	public void setTitle(CharSequence title) {
		this.title = title;
		view.setText(title);
	}

	public void setVisible(boolean visible) {
		view.setVisibility(visible ? View.VISIBLE : View.GONE);
	}

	public boolean isVisible() {
		return view.getVisibility() == View.VISIBLE;
	}

	public void setEnabled(boolean isEnabled) {
		view.setEnabled(isEnabled);
	}

	public boolean isEnabled() {
		return view.isEnabled();
	}

	@Override
	public void onClick(View v) {
		if (null != onNavigationItemClickListener) {
			onNavigationItemClickListener.onNavigationItemClick(this);
		}
	}

	void setOnNavigationBarItemListener(OnNavigationItemClickListener listener) {
		this.onNavigationItemClickListener = listener;
	}

	void setCallback(Callback callback) {
		this.callback = new WeakReference<NavigationBarItem.Callback>(callback);
	}

	interface Callback {
		public void onIconChanged(NavigationBarItem item, Drawable icon);
	}
}