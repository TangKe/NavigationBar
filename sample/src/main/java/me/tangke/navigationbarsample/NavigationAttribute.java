package me.tangke.navigationbarsample;

import me.tangke.navigationbar.NavigationBar;
import me.tangke.navigationbar.NavigationBarActivity;
import me.tangke.navigationbarsample.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class NavigationAttribute extends NavigationBarActivity implements OnClickListener {
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_navigation_bar_attribute);
	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		NavigationBar navigationBar = getNavigationBar();
		navigationBar.setDisplayOptions(navigationBar.getDisplayOptions()
				| NavigationBar.DISPLAY_PRIMARY_NAVIGATION_AS_UP);
	}

	@Override
	public void onClick(View arg0) {
		startActivity(new Intent(this, NavigationFeature.class));
	}
}
