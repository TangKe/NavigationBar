package me.tangke.navigationbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.lang.ref.WeakReference;

/**
 * {@link NavigationBar} implements for activity
 *
 * @author Tank
 */
class NavigationBarActivityImpl extends NavigationBarImpl {
    private WeakReference<Activity> mActivity;

    public NavigationBarActivityImpl(Activity activity) {
        super(activity);
        mActivity = new WeakReference<Activity>(activity);
    }

    @Override
    public CharSequence getDefaultTitle() {
        final Activity activity = mActivity.get();
        try {
            final PackageManager packageManager = activity.getPackageManager();
            ActivityInfo activityInfo = packageManager.getActivityInfo(
                    activity.getComponentName(), 0);
            if (activityInfo.labelRes > 0) {
                return activity.getResources().getString(activityInfo.labelRes);
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return activity.getTitle();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        final Activity activity = mActivity.get();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && (activity.getWindow().getAttributes().flags & WindowManager.LayoutParams
                .FLAG_TRANSLUCENT_STATUS) == WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS) {
            final Resources resources = activity.getResources();
            int identifier = resources.getIdentifier("status_bar_height",
                    "dimen", "android");
            if (0 < identifier) {
                final View navigationBarView = getNavigationBarView();
                final int paddingToApply = resources
                        .getDimensionPixelSize(identifier);
                navigationBarView.setMinimumHeight(navigationBarView
                        .getMinimumHeight() + paddingToApply);
                navigationBarView.setPadding(0, paddingToApply, 0, 0);
            }
        }
    }

    @Override
    public Drawable getDefaultIcon() {
        final Activity activity = mActivity.get();
        try {
            final PackageManager packageManager = activity.getPackageManager();
            ActivityInfo activityInfo = packageManager.getActivityInfo(
                    activity.getComponentName(), 0);
            if (activityInfo.icon > 0) {
                return activity.getResources().getDrawable(activityInfo.icon);
            }
            return activityInfo.loadIcon(packageManager);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onReplaceContentView(ViewGroup navigationBarContainer,
                                        ViewGroup navigationBarContentContainer) {
        if (!hasNavigationBar()) {
            return;
        }
        Activity activity = mActivity.get();
        View content = activity.findViewById(android.R.id.content);
        ViewGroup contentParent = (ViewGroup) content.getParent();
        contentParent.removeView(content);
        navigationBarContentContainer.addView(content,
                new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT));
        contentParent
                .addView(navigationBarContainer, content.getLayoutParams());
    }

    @SuppressLint("NewApi")
    @Override
    protected boolean onNavigateUp() {
        final Activity activity = mActivity.get();
        if (Build.VERSION_CODES.JELLY_BEAN <= Build.VERSION.SDK_INT) {
            return activity.onNavigateUp();
        } else {
            activity.finish();
            return true;
        }
    }

    @Override
    void setContentView(View view, LayoutParams params) {
        if (null == view) {
            return;
        }
        params = null == params ? new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT) : params;
        mActivity.get().setContentView(view, params);
    }
}
