package me.tangke.navigationbarsample;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

import me.tangke.navigationbar.NavigationBar;
import me.tangke.navigationbar.NavigationBarActivity;

public class NavigationBarAttribute extends NavigationBarActivity implements OnClickListener,
        CompoundButton.OnCheckedChangeListener, RadioGroup.OnCheckedChangeListener {
    private CheckBox mDisplayShowPrimaryAsUp;
    private CheckBox mDisplayShowTitle;
    private CheckBox mDisplayShowLogo;
    private CheckBox mDisplayShowCustom;

    private ToggleButton mToggle;

    private RadioGroup mNavigationMode;

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
        mToggle = (ToggleButton) findViewById(R.id.toggle);
        mToggle.setOnClickListener(this);

        mNavigationMode = (RadioGroup) findViewById(R.id.navigationMode);
        mNavigationMode.setOnCheckedChangeListener(this);

        getNavigationBar().setCustomView(R.layout.layout_custom_view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toggle:
                if (mToggle.isChecked()) {
                    getNavigationBar().hide();
                } else {
                    getNavigationBar().show();
                }
                break;
        }
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

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        final NavigationBar navigationBar = getNavigationBar();
        switch (checkedId) {
            case R.id.standard:
                navigationBar.setNavigationMode(NavigationBar.NAVIGATION_MODE_STANDARD);
                break;
            case R.id.list:
                navigationBar.setNavigationMode(NavigationBar.NAVIGATION_MODE_LIST);
                navigationBar.setListNavigationCallbacks(ArrayAdapter.createFromResource(this, R
                        .array.list_navigation, android.R.layout.simple_dropdown_item_1line), null);
                break;
        }
    }
}
