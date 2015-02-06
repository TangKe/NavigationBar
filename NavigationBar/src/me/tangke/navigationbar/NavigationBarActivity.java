package me.tangke.navigationbar;

import me.tangke.navigationbar.NavigationBarImpl.OnNavigationItemClickListener;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * 包含{@link NavigationBar}的{@link Activity}, 如果{@link Activity}需要呈现一个
 * {@link NavigationBar}, 需要继承此类
 * 
 * @author Tank
 * 
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
	 * 获取{@link NavigationBar}, 如果theme中设置了无{@link NavigationBar}, 则返回null
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
}
