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
    int textAppearance;

    public NavigationBarButton(Context context, int id, TextView view, int gravity, int textAppearance) {
        super(context, id, view, gravity);
        text = view;
        this.textAppearance = textAppearance;
    }

    @Override
    protected void onInvalidate() {
        //文本
        text.setText(title);
        text.setTextAppearance(text.getContext(), textAppearance);

        //图标
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
        tint(icon);
        tint(text);
    }

    protected void tint(Drawable drawable) {
        if (null != drawable) {
            drawable.setColorFilter(isTintEnable ? tintColorFilter : null);
        }
    }

    protected void tint(TextView text) {
        if (isTintEnable) {
            text.setTextColor(tintColor);
        } else {
            text.setTextAppearance(text.getContext(), textAppearance);
        }
    }

    public void setTextAppearance(int textAppearance) {
        this.textAppearance = textAppearance;
        invalidate();
    }
}
