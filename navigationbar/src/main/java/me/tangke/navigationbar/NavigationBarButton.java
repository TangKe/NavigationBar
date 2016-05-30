package me.tangke.navigationbar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.widget.TextView;

/**
 * Clickable navigation item
 *
 * @author Tank
 */
public class NavigationBarButton extends NavigationBarItem {
    TextView text;

    NavigationBarButton(Context context, int id, TextView view, int gravity) {
        super(context, id, view, gravity);
        text = view;
    }

    @Override
    public void setIcon(Drawable icon) {
        if (this.icon == icon) {
            return;
        }

        this.icon = icon;

        if (null != icon) {
            icon.setColorFilter(isTintEnable ? NavigationBarView.sColorAccentFilter : null);
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

        text.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        text.setText(title);
    }

    @Override
    protected void onInvalidate() {
        setIcon(this.icon);
    }
}
