package me.tangke.navigationbar;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

import java.lang.ref.WeakReference;

/**
 * {@link NavigationBar} implements for Fragment
 */
class NavigationBarFragmentImpl extends NavigationBarActivityImpl {
    private WeakReference<Fragment> mFragment;

    public NavigationBarFragmentImpl(Fragment fragment) {
        super(fragment.getActivity());
        mFragment = new WeakReference<Fragment>(fragment);
    }

    @Override
    protected void onReplaceContentView(ViewGroup navigationBarContainer,
                                        ViewGroup navigationBarContentContainer) {
    }

    @Override
    void setContentView(View view, LayoutParams params) {
        if (null == view) {
            return;
        }
        getNavigationBarContentView().removeAllViews();
        params = null == params ? new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT) : params;
        getNavigationBarContentView().addView(view, params);
    }
}
