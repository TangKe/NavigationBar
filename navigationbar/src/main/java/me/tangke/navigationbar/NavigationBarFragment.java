package me.tangke.navigationbar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 *
 *
 * @author Tank
 */
public class NavigationBarFragment extends Fragment implements
        OnNavigationItemClickListener {
    private NavigationBarImpl mNavigationBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final NavigationBarImpl navigationBar = mNavigationBar = new NavigationBarFragmentImpl(
                this);
        mNavigationBar.onCreate();
        navigationBar.setOnNavigationItemClickListener(this);
    }

    @Override
    public final View onCreateView(LayoutInflater inflater,
                                   ViewGroup container, Bundle savedInstanceState) {
        View navigationBarContainerView = mNavigationBar
                .getNavigationBarContainerView();
        View root = onCreateView(inflater,
                mNavigationBar.getNavigationBarContentView(),
                mNavigationBar.getNavigationBarView(), savedInstanceState);
        mNavigationBar.setContentView(root);
        return navigationBarContainerView;
    }

    /**
     * sub class should use this method to create view of Fragment
     *
     * @param inflater
     * @param container
     * @param navigationBar
     * @param savedInstanceState
     * @return
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             ViewGroup navigationBar, Bundle savedInstanceState) {
        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ViewGroup parent = (ViewGroup) mNavigationBar
                .getNavigationBarContainerView().getParent();
        if (null != parent) {
            parent.removeView(mNavigationBar.getNavigationBarContainerView());
        }
    }

    public NavigationBar getNavigationBar() {
        return mNavigationBar.hasNavigationBar() ? mNavigationBar : null;
    }

    @Override
    public void onNavigationItemClick(NavigationBarItem item) {
    }
}
