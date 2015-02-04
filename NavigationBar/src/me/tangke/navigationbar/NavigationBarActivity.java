package me.tangke.navigationbar;

import me.tangke.navigationbar.NavigationBar.NavigationBarItem;
import me.tangke.navigationbar.NavigationBarImpl.OnNavigationItemClickListener;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

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
		final NavigationBarImpl navigationBar = new NavigationBarImpl(this);
		navigationBar.setOnNavigationItemClickListener(this);
		mNavigationBarImpl = navigationBar;
	}

	/**
	 * 调用父类的{@link FragmentActivity#setContentView(View)}方法
	 * 
	 * @param v
	 */
	void superSetContentView(View v) {
		super.setContentView(v);
	}

	/**
	 * 调用父类的{@link FragmentActivity#setContentView(View)}方法
	 * 
	 * @param v
	 * @param lp
	 */
	void superSetContentView(View v, ViewGroup.LayoutParams lp) {
		super.setContentView(v, lp);
	}

	@Override
	public void setContentView(View view, LayoutParams params) {
		mNavigationBarImpl.setContentView(view, params);
	}

	@Override
	public void setContentView(int layoutResID) {
		mNavigationBarImpl.setContentView(layoutResID);
	}

	@Override
	public void setContentView(View view) {
		mNavigationBarImpl.setContentView(view);
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

	public void onSupportNavigateUp() {
	}
}
