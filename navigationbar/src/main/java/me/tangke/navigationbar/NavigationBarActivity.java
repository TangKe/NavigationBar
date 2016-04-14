package me.tangke.navigationbar;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * A Activity include a {@link NavigationBar}, if you want to use {@link NavigationBar}, please
 * inherit from this class
 *
 * @author Tank
 */
public abstract class NavigationBarActivity extends FragmentActivity implements
        OnNavigationItemClickListener {
    private NavigationBarImpl mNavigationBarImpl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final NavigationBarImpl navigationBar = new NavigationBarActivityImpl(
                this);
        navigationBar.onCreate();
        navigationBar.setOnNavigationItemClickListener(this);
        mNavigationBarImpl = navigationBar;
    }

    /**
     * Get the reference of {@link NavigationBar}, if the attribute navigationBar in theme is
     * false, return null
     *
     * @return
     */
    public NavigationBar getNavigationBar() {
        return mNavigationBarImpl.hasNavigationBar() ? mNavigationBarImpl
                : null;
    }

    @Override
    public void onNavigationItemClick(NavigationBarItem item) {
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        mNavigationBarImpl.setTitle(title);
    }

    @Override
    public void setTitle(int titleId) {
        super.setTitle(titleId);
        mNavigationBarImpl.setTitle(titleId);
    }
}
