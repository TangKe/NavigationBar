package me.tangke.navigationbar;

import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.FrameLayout.LayoutParams;
import android.widget.SpinnerAdapter;

/**
 * Similar with iOS NavigationBar, with some navigate features, NavigationBar will read the some
 * attributes from current activity automatically, such as title, icon defined in AndroidManifest,
 * can styled by theme attribute, support Fragment({@link NavigationBarFragment})
 *
 * @author Tank
 */
public interface NavigationBar {
    /**
     * Normal state
     */
    int NAVIGATION_MODE_STANDARD = 0x00000000;
    /**
     * A dropdown list will replace the title
     */
    int NAVIGATION_MODE_LIST = 0x00000001;

    /**
     * Append a navigation item to as back button
     */
    int DISPLAY_PRIMARY_NAVIGATION_AS_UP = 1 << 0;
    /**
     * Use the exist space to customer view, this view show on the top of the NavigationBar content
     */
    int DISPLAY_SHOW_CUSTOM = 1 << 1;
    /**
     * Control the title's visibility
     */
    int DISPLAY_SHOW_TITLE = 1 << 2;
    /**
     * Control the icon left of title's visibility
     */
    int DISPLAY_SHOW_LOGO = 1 << 3;

    /**
     * Show the {@link NavigationBar}
     */
    void show();

    /**
     * Hide the {@link NavigationBar}, the content will auto fill the space of {@link NavigationBar}
     */
    void hide();

    /**
     * Get the reference of the primary container, in the LEFT edge of {@link NavigationBar}
     *
     * @return
     */
    NavigationBarItemGroup getPrimaryNavigationBarItemGroup();

    /**
     * Get the reference of secondary container, in the right edge of {@link NavigationBar}
     *
     * @return
     */
    NavigationBarItemGroup getSecondaryNavigationBarItemGroup();

    /**
     * Set the navigation mode
     *
     * @param mode
     * @see {@link #NAVIGATION_MODE_LIST}, {@link #NAVIGATION_MODE_STANDARD}
     */
    void setNavigationMode(int mode);

    /**
     * Set the callback of dropdown item click
     *
     * @param adapter  adapter to generate view
     * @param listener item click callback
     */
    void setListNavigationCallbacks(SpinnerAdapter adapter,
                                    OnNavigationListener listener);

    /**
     * Set the callback of dropdown item click
     *
     * @param adapter          adapter to generate view
     * @param selectedPosition default selected item index
     * @param listener         item click callback
     */
    void setListNavigationCallbacks(SpinnerAdapter adapter,
                                    int selectedPosition, OnNavigationListener listener);

    /**
     * Set the title, {@link NavigationBar} will read the activity title by default
     *
     * @param res
     */
    void setTitle(int res);

    /**
     * Set the title, {@link NavigationBar} will read the activity title by default
     *
     * @param title
     */
    void setTitle(CharSequence title);

    /**
     * Set the background of {@link NavigationBar}
     *
     * @param drawable
     */
    void setBackgroundDrawable(Drawable drawable);

    /**
     * Set a customer view
     *
     * @param res layout resource
     */
    void setCustomView(@LayoutRes int res);

    /**
     * Set a customer view
     *
     * @param view
     */
    void setCustomView(View view);

    /**
     * Set a customer view and specified the LayoutParams
     *
     * @param view
     * @param layoutParams
     */
    void setCustomView(View view, LayoutParams layoutParams);

    /**
     * Set the icon
     *
     * @param resId
     */
    void setIcon(@DrawableRes int resId);

    /**
     * Set the icon
     *
     * @param icon
     */
    void setIcon(Drawable icon);

    /**
     * Set the display options of {@link NavigationBar}
     *
     * @param displayOptions
     */
    void setDisplayOptions(int displayOptions);

    /**
     * Get current display options
     *
     * @return
     */
    int getDisplayOptions();

    /**
     * Factory method to create a {@link NavigationBarItem}
     *
     * @param id
     * @param title
     * @param icon
     * @param gravity
     * @return
     */
    NavigationBarItem newNavigationBarItem(int id, CharSequence title,
                                           int icon, int gravity);

    /**
     * Factory method to create a {@link NavigationBarItem}
     *
     * @param id
     * @param title
     * @param icon
     * @param gravity
     * @return
     */
    NavigationBarItem newNavigationBarItem(int id, int title, int icon,
                                           int gravity);

    /**
     * Set the tint color of {@link NavigationBar}
     *
     * @param color
     */
    void setNavigationBarColorPrimary(int color);

    void setNavigationBarTextColorPrimary(int color);

    void setNavigationBarColorAccent(int color);

    /**
     * Dropdown item click callback
     *
     * @author Tank
     */
    interface OnNavigationListener {
        void onNavigationItemSelected(int itemPosition, long itemId);
    }
}
