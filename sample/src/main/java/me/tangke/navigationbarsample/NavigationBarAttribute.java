package me.tangke.navigationbarsample;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

import com.larswerkman.holocolorpicker.ColorPicker;

import me.tangke.navigationbar.NavigationBar;
import me.tangke.navigationbar.NavigationBarActivity;
import me.tangke.navigationbar.NavigationBarItem;
import me.tangke.navigationbar.NavigationBarItemGroup;

public class NavigationBarAttribute extends NavigationBarActivity implements OnClickListener,
        CompoundButton.OnCheckedChangeListener, RadioGroup.OnCheckedChangeListener, ColorPicker
                .OnColorSelectedListener {
    private CheckBox mDisplayShowPrimaryAsUp;
    private CheckBox mDisplayShowTitle;
    private CheckBox mDisplayShowLogo;
    private CheckBox mDisplayShowCustom;

    private ToggleButton mToggle;
    private ToggleButton mTint;

    private RadioGroup mNavigationMode;
    private RadioGroup mColors;

    private int mDisplayOptions;

    private ColorPicker mColorPrimaryPicker;
    private ColorPicker mColorAccentPicker;
    private ColorPicker mTextColorPrimaryPicker;

    private NavigationBarItem mLike;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_navigation_bar_attribute);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        final NavigationBar navigationBar = getNavigationBar();
        mLike = navigationBar.newNavigationBarItem(R.id.like, null, R.drawable.ic_like, Gravity
                .RIGHT);
        navigationBar.getSecondaryNavigationBarItemGroup().addNavigationBarItem(mLike);
        mDisplayOptions = navigationBar.getDisplayOptions();
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

        mTint = (ToggleButton) findViewById(R.id.tint);
        mTint.setOnClickListener(this);

        mNavigationMode = (RadioGroup) findViewById(R.id.navigationMode);
        mNavigationMode.setOnCheckedChangeListener(this);

        navigationBar.setCustomView(R.layout.layout_custom_view);

        mColorPrimaryPicker = (ColorPicker) findViewById(R.id.color);
        mColorPrimaryPicker.setOnColorSelectedListener(this);
        mColors = (RadioGroup) findViewById(R.id.colors);
        mColors.setOnCheckedChangeListener(this);
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
            case R.id.tint:
                if (mTint.isChecked()) {
                    mLike.setTintEnable(true);
                } else {
                    mLike.setTintEnable(false);
                }
                break;
            case R.id.addSecondaryItem:
                NavigationBarItem like = getNavigationBar().newNavigationBarItem(R.id.like, null,
                        R.drawable.ic_like, Gravity.RIGHT);
                getNavigationBar().getSecondaryNavigationBarItemGroup().addNavigationBarItem(like);
                break;
            case R.id.removeSecondaryItem:
                NavigationBarItemGroup group = getNavigationBar().getSecondaryNavigationBarItemGroup();
                group.removeNavigationBarItem(0);
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
        if (group == mNavigationMode) {
            switch (checkedId) {
                case R.id.standard:
                    navigationBar.setNavigationMode(NavigationBar.NAVIGATION_MODE_STANDARD);
                    break;
                case R.id.list:
                    navigationBar.setNavigationMode(NavigationBar.NAVIGATION_MODE_LIST);
                    navigationBar.setListNavigationCallbacks(ArrayAdapter.createFromResource(this, R
                                    .array.list_navigation, android.R.layout
                                    .simple_dropdown_item_1line),
                            null);
                    break;
            }
        }
    }

    @Override
    public void onColorSelected(int color) {
        switch (mColors.getCheckedRadioButtonId()) {
            case R.id.colorPrimary:
                getNavigationBar().setNavigationBarColorPrimary(color);
                break;
            case R.id.colorAccent:
                getNavigationBar().setNavigationBarColorAccent(color);
                break;
            case R.id.textColorPrimary:
                getNavigationBar().setNavigationBarTextColorPrimary(color);
                break;
        }
    }
}
