package me.tangke.navigationbar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

/**
 * NavigationBar title item
 *
 * @author Tank
 */
public class NavigationBarTitle extends NavigationBarItem {

    TextView text;
    private int mIconSize;

    private boolean mIsIconVisible;

    NavigationBarTitle(Context context, int id, TextView view, int gravity) {
        super(context, id, view, gravity);
        text = view;
        mIconSize = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 40, context.getResources()
                        .getDisplayMetrics());
    }

    @Override
    public void setIcon(Drawable icon) {
        if (this.icon == icon) {
            return;
        }

        this.icon = icon;
        if (null != icon) {
            icon.setBounds(0, 0, mIconSize, mIconSize);
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

        if (mIsIconVisible) {
            text.setCompoundDrawables(left, top, right, bottom);
        } else {
            text.setCompoundDrawables(null, null, null, null);
        }
    }

    public void setIconVisible(boolean visible) {
        mIsIconVisible = visible;
        setIcon(icon);
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        text.setText(title);
    }
}
