package me.tangke.navigationbar;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;

/**
 * 导航按钮容器
 * 
 * @author Tank
 *
 */
public class NavigationBarItemGroup extends NavigationBarItem implements
		NavigationBarItemParent {

	private ArrayList<NavigationBarItem> mNavigationBarItems = new ArrayList<NavigationBarItem>();
	private ViewGroup mGroup;

	NavigationBarItemGroup(Context context, int id, ViewGroup group,
			int gravity, String tag) {
		super(context, id, group, gravity, tag);
		mGroup = group;
	}

	@Override
	public void addNavigationBarItem(NavigationBarItem item) {
		addNavigationBarItem(item, getNavigationBarItemCount());
	}

	@Override
	public void addNavigationBarItem(NavigationBarItem item, int index) {
		if (mNavigationBarItems.contains(item)) {
			return;
		}
		index = Math.max(0, Math.min(index, getNavigationBarItemCount()));
		mNavigationBarItems.add(item);
		mGroup.addView(item.view, index);
	}

	@Override
	public void removeNavigationBarItem(int index) {
		if (index < 0 || getNavigationBarItemCount() >= index) {
			return;
		}
		NavigationBarItem item = mNavigationBarItems.remove(index);
		mGroup.removeView(item.view);
	}

	@Override
	public void removeNavigationBarItem(NavigationBarItem item) {
		mNavigationBarItems.remove(item);
		mGroup.removeView(item.view);
	}

	@Override
	public int getNavigationBarItemCount() {
		return mNavigationBarItems.size();
	}

	@Override
	void setOnNavigationBarItemListener(OnNavigationItemClickListener listener) {
		super.setOnNavigationBarItemListener(listener);
		for (NavigationBarItem item : mNavigationBarItems) {
			item.setOnNavigationBarItemListener(listener);
		}
	}

	@Override
	public void setIcon(Drawable icon) {
		setIcon(icon, 0);
	}

	public void setIcon(Drawable icon, int index) {
		if (index < 0 || getNavigationBarItemCount() >= index) {
			return;
		}
		NavigationBarItem item = mNavigationBarItems.get(index);
		item.setIcon(icon);
	}

	@Override
	public void setTitle(CharSequence title) {
		super.setTitle(title);
		setTitle(title, 0);
	}

	@Override
	public void setTitle(int res) {
		super.setTitle(res);
		setTitle(title, 0);
	}

	public void setTitle(CharSequence title, int index) {
		if (index < 0 || getNavigationBarItemCount() >= index) {
			return;
		}
		NavigationBarItem item = mNavigationBarItems.get(index);
		item.setTitle(title);
	}
}
