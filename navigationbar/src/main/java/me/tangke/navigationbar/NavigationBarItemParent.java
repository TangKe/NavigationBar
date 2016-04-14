package me.tangke.navigationbar;

/**
 * @author Tank
 */
interface NavigationBarItemParent {
    void addNavigationBarItem(NavigationBarItem item);

    void addNavigationBarItem(NavigationBarItem item, int index);

    void removeNavigationBarItem(int index);

    void removeNavigationBarItem(NavigationBarItem item);

    int getNavigationBarItemCount();
}
