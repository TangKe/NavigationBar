package me.tangke.navigationbar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;

import java.lang.ref.WeakReference;

/**
 * NavigationBar item define
 *
 * @author Tank
 */
public abstract class NavigationBarItem implements OnClickListener {

    int id;
    View view;

    CharSequence title;
    Drawable icon;

    int gravity;

    boolean isTintEnable = true;
    int tintColor;
    ColorFilter tintColorFilter;

    OnNavigationItemClickListener onNavigationItemClickListener;

    private WeakReference<Context> mContext;

    public NavigationBarItem(Context context, int id, View view, int gravity) {
        this.id = id;
        this.view = view;
        this.gravity = gravity;
        view.setOnClickListener(this);
        this.mContext = new WeakReference<>(context);
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

    public void setIcon(Drawable icon) {
        if (null != icon) {
            this.icon = icon.mutate();
        } else {
            this.icon = null;
        }
        invalidate();
    }

    public CharSequence getTitle() {
        return title;
    }

    public void setTitle(int res) {
        setTitle(0 < res ? mContext.get().getText(res) : null);
    }

    public void setTitle(CharSequence title) {
        this.title = title;
        invalidate();
    }

    public void setVisible(boolean visible) {
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        invalidate();
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

    public void setTintEnable(boolean isEnable) {
        isTintEnable = isEnable;
        invalidate();
    }

    public boolean isTintEnable() {
        return isTintEnable;
    }

    public void setTintColor(int tintColor) {
        this.tintColor = tintColor;
        if (tintColor != Color.TRANSPARENT) {
            tintColorFilter = new PorterDuffColorFilter(tintColor, PorterDuff.Mode.SRC_IN);
        } else {
            tintColorFilter = null;
        }
        invalidate();
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

    final void invalidate() {
        onInvalidate();
    }

    protected void onInvalidate() {

    }
}