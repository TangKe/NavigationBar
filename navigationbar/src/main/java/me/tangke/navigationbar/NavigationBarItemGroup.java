package me.tangke.navigationbar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * NavigationBarItem container
 *
 * @author Tank
 */
public class NavigationBarItemGroup extends NavigationBarItem implements
        NavigationBarItemParent {

    private ArrayList<NavigationBarItem> mNavigationBarItems = new ArrayList<NavigationBarItem>();
    ViewGroup group;

    public NavigationBarItemGroup(Context context, int id, ViewGroup group, int gravity) {
        super(context, id, group, gravity);
        this.group = group;
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
        group.addView(item.view, index);
        item.setOnNavigationBarItemListener(onNavigationItemClickListener);
        invalidate();
    }

    @Override
    public void removeNavigationBarItem(int index) {
        if (index < 0 || getNavigationBarItemCount() <= index) {
            return;
        }
        NavigationBarItem item = mNavigationBarItems.remove(index);
        group.removeView(item.view);
        item.setOnNavigationBarItemListener(null);
        invalidate();
    }

    @Override
    public void removeNavigationBarItem(NavigationBarItem item) {
        mNavigationBarItems.remove(item);
        group.removeView(item.view);
        item.setOnNavigationBarItemListener(null);
        invalidate();
    }

    @Override
    public int getNavigationBarItemCount() {
        return mNavigationBarItems.size();
    }

    @Override
    protected void onInvalidate() {
        super.onInvalidate();
        for (NavigationBarItem item : mNavigationBarItems) {
            item.setTintColor(tintColor);
//            item.setTitle(title);
//            item.setIcon(icon);
            item.setOnNavigationBarItemListener(onNavigationItemClickListener);
        }
    }
}
