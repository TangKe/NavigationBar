package me.tangke.navigationbar;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.FrameLayout.LayoutParams;
import android.widget.SpinnerAdapter;

/**
 * NavigationBar 实现和iOSNavigationBar外观类似的功能
 * <ul>
 * <li>自动读取当前Activity在Mainfest中定义时指定的标题和icon</li>
 * <li>支持Overlay模式 {@link R.attr.}</li>
 * <li></li>
 * </ul>
 * 
 * @author Tank
 * 
 */
public interface NavigationBar {
	/**
	 * 标准状态
	 */
	public final static int NAVIGATION_MODE_STANDARD = 0x00000000;
	/**
	 * 中间标题替换成下拉菜单
	 */
	public final static int NAVIGATION_MODE_LIST = 0x00000001;

	public final static int DISPLAY_PRIMARY_NAVIGATION_AS_UP = 1 << 0;
	public final static int DISPLAY_SHOW_CUSTOM = 1 << 1;
	public final static int DISPLAY_SHOW_TITLE = 1 << 2;

	/**
	 * 显示{@link NavigationBar}
	 */
	public void show();

	/**
	 * 隐藏{@link NavigationBar}
	 */
	public void hide();

	/**
	 * 获取左侧导航按钮
	 * 
	 * @return
	 */
	public NavigationBarItemGroup getPrimaryNavigationBarItemGroup();

	/**
	 * 获取右侧导航按钮
	 * 
	 * @return
	 */
	public NavigationBarItemGroup getSecondaryNavigationBarItemGroup();

	/**
	 * 设置导航模式
	 * 
	 * @see {@link #NAVIGATION_MODE_LIST}, {@link #NAVIGATION_MODE_STANDARD}
	 * @param mode
	 */
	public void setNavigationMode(int mode);

	/**
	 * 设置List导航的选项和点击回调
	 * 
	 * @param adapter
	 * @param listener
	 */
	public void setListNavigationCallbacks(SpinnerAdapter adapter,
			OnNavigationListener listener);

	/**
	 * 设置List导航的选项和点击回调
	 * 
	 * @param adapter
	 * @param selectedPosition
	 * @param listener
	 */
	public void setListNavigationCallbacks(SpinnerAdapter adapter,
			int selectedPosition, OnNavigationListener listener);

	/**
	 * 设置标题, 默认会读取当前{@link Activity}的标题
	 * 
	 * @param res
	 */
	public void setTitle(int res);

	/**
	 * 设置标题, 默认会读取当前{@link Activity}的标题
	 * 
	 * @param title
	 */
	public void setTitle(CharSequence title);

	/**
	 * 设置NavigationBar背景
	 * 
	 * @param drawable
	 */
	public void setBackgroundDrawable(Drawable drawable);

	/**
	 * 设置一个自定义{@link View}
	 * 
	 * @param res
	 */
	public void setCustomView(int res);

	/**
	 * 设置一个自定义{@link View}
	 * 
	 * @param view
	 */
	public void setCustomView(View view);

	/**
	 * 设置一个自定义{@link View}
	 * 
	 * @param view
	 * @param layoutParams
	 */
	public void setCustomView(View view, LayoutParams layoutParams);

	/**
	 * 设置图标
	 * 
	 * @param resId
	 */
	public void setIcon(int resId);

	/**
	 * 设置图标
	 * 
	 * @param icon
	 */
	public void setIcon(Drawable icon);

	/**
	 * 设置显示选项
	 * 
	 * @param displayOptions
	 */
	public void setDisplayOptions(int displayOptions);

	/**
	 * 获取当前显示选项
	 * 
	 * @return
	 */
	public int getDisplayOptions();

	/**
	 * 生成新的{@link NavigationBarItem}
	 * 
	 * @param id
	 * @param title
	 * @param icon
	 * @param gravity
	 * @param tag
	 * @return
	 */
	public NavigationBarItem newNavigationBarItem(int id, CharSequence title,
			int icon, int gravity, String tag);

	/**
	 * 导航回调
	 * 
	 * @author Tank
	 * 
	 */
	public interface OnNavigationListener {
		public void onNavigationItemSelected(int itemPosition, long itemId);
	}
}
