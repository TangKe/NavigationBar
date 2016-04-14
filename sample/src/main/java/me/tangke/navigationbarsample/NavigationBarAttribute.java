package me.tangke.navigationbarsample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import me.tangke.navigationbar.NavigationBar;
import me.tangke.navigationbar.NavigationBarActivity;

public class NavigationBarAttribute extends NavigationBarActivity implements OnClickListener,
        CompoundButton.OnCheckedChangeListener {
    private CheckBox mDisplayShowPrimaryAsUp;
    private CheckBox mDisplayShowTitle;
    private CheckBox mDisplayShowLogo;
    private CheckBox mDisplayShowCustom;

    private int mDisplayOptions;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_navigation_bar_attribute);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        mDisplayShowPrimaryAsUp = (CheckBox) findViewById(R.id.displayPrimaryNavigationAsUp);
        mDisplayShowPrimaryAsUp.setOnCheckedChangeListener(this);
        mDisplayShowTitle = (CheckBox) findViewById(R.id.displayShowTitle);
        mDisplayShowTitle.setOnCheckedChangeListener(this);
        mDisplayShowLogo = (CheckBox) findViewById(R.id.displayShowLogo);
        mDisplayShowLogo.setOnCheckedChangeListener(this);
        mDisplayShowCustom = (CheckBox) findViewById(R.id.displayShowCustom);
        mDisplayShowCustom.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View arg0) {
        startActivity(new Intent(this, NavigationFeature.class));
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.displayPrimaryNavigationAsUp:
                if (isChecked) {
                    mDisplayOptions |= NavigationBar.DISPLAY_PRIMARY_NAVIGATION_AS_UP;
                } else {
                    mDisplayOptions &= ~NavigationBar.DISPLAY_PRIMARY_NAVIGATION_AS_UP;
                }
                break;
            case R.id.displayShowTitle:
                if (isChecked) {
                    mDisplayOptions |= NavigationBar.DISPLAY_SHOW_TITLE;
                } else {
                    mDisplayOptions &= ~NavigationBar.DISPLAY_SHOW_TITLE;
                }
                break;
            case R.id.displayShowLogo:
                if (isChecked) {
                    mDisplayOptions |= NavigationBar.DISPLAY_SHOW_LOGO;
                } else {
                    mDisplayOptions &= ~NavigationBar.DISPLAY_SHOW_LOGO;
                }
                break;
            case R.id.displayShowCustom:
                if (isChecked) {
                    mDisplayOptions |= NavigationBar.DISPLAY_SHOW_CUSTOM;
                } else {
                    mDisplayOptions &= ~NavigationBar.DISPLAY_SHOW_CUSTOM;
                }
                break;
        }
        getNavigationBar().setDisplayOptions(mDisplayOptions);
    }
}
