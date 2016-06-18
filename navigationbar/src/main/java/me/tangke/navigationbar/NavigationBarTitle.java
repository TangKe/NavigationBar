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
public class NavigationBarTitle extends NavigationBarButton {
    private boolean mIsIconVisible;

    public NavigationBarTitle(Context context, int id, TextView view, int gravity, int textAppearance) {
        super(context, id, view, gravity, textAppearance);
    }

    public void setIconVisible(boolean visible) {
        mIsIconVisible = visible;
        invalidate();
    }

    @Override
    protected void onInvalidate() {
        super.onInvalidate();
        if (!mIsIconVisible) {
            text.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }

        //图标
        if (null != icon) {
            //应用图标不支持着色
            icon.setColorFilter(null);
        }
    }
}
