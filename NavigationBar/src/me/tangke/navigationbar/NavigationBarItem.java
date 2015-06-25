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

/**
 * 导航选项定义
 * 
 * @author Tank
 * 
 */
public abstract class NavigationBarItem implements OnClickListener {

	int id;
	View view;

	CharSequence title;
	Drawable icon;

	int gravity;

	OnNavigationItemClickListener onNavigationItemClickListener;

	private WeakReference<Context> mContext;
	private String tag;

	NavigationBarItem(Context context, int id, View view, int gravity,
			String tag) {
		this.id = id;
		this.view = view;
		this.gravity = gravity;
		this.tag = tag;
		view.setOnClickListener(this);
		this.mContext = new WeakReference<Context>(context);
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
		setIcon(0 < res ? mContext.get().getResources().getDrawable(res) : null);
	}

	public abstract void setIcon(Drawable icon);

	public CharSequence getTitle() {
		return title;
	}

	public void setTitle(int res) {
		setTitle(0 < res ? mContext.get().getText(res) : null);
	}

	public void setTitle(CharSequence title) {
		this.title = title;
	}

	public void setVisible(boolean visible) {
		view.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
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

}