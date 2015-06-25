package me.tangke.navigationbar;

/**
 * 
 * 
 * @author Tank
 *
 */
public interface NavigationBarItemParent {
	public void addNavigationBarItem(NavigationBarItem item);

	public void addNavigationBarItem(NavigationBarItem item, int index);

	public void removeNavigationBarItem(int index);

	public void removeNavigationBarItem(NavigationBarItem item);

	public int getNavigationBarItemCount();

}
