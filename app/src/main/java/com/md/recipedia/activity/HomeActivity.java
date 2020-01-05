package com.md.recipedia.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.md.recipedia.R;
import com.md.recipedia.fragment.AboutFragment;
import com.md.recipedia.fragment.CategoryFragment;
import com.md.recipedia.fragment.FavoriteFragment;
import com.md.recipedia.fragment.HomeFragment;
import com.md.recipedia.fragment.OpenSourceFragment;
import com.md.recipedia.utils.DrawerUtils;
import com.md.recipedia.utils.OtherUtils;
import com.md.recipedia.utils.ThemeUtils;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;


public class HomeActivity extends BaseActivity {
    String version;
    AccountHeader header;
    Toolbar toolbar;
    MaterialDialog changelogDialog;
    PackageInfo pInfo = null;
    View toolbarShadow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //toolbar thing
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.home);


        toolbarShadow = findViewById(R.id.toolbarShadow);
        //getting the current version of app
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        version = "v " + pInfo.versionName;
        checkAndSetFirstRun();
        OtherUtils.isVersionUpdated(this);

        //nav drawer header setup
        header = new AccountHeaderBuilder()
                .withActivity(this)
                .addProfiles(
                        new ProfileDrawerItem().withName(getResources().getString(R.string.app_name)).withEmail(version)
                )
                .withHeaderBackground(R.drawable.nav_header_banner)
                .withHeaderBackgroundScaleType(ImageView.ScaleType.CENTER_CROP)
                .withAlternativeProfileHeaderSwitching(false)
                .withSelectionListEnabledForSingleProfile(false)
                .withProfileImagesVisible(false)
                .build();

        //setup nav drawer
        if (ThemeUtils.getTheme(getApplicationContext()) <= 1) {
            DrawerUtils.generateDrawer(toolbar, R.color.nav_drawer_background, R.color.nav_drawer_primary_text, R.color.nav_drawer_secondary_text, R.color.nav_drawer_primary_icon, R.color.nav_drawer_selected_text, R.color.nav_drawer_selected_text, R.color.nav_drawer_selected, header, this, this, toolbarShadow);
        } else if (ThemeUtils.getTheme(getApplicationContext()) == 2) {
            DrawerUtils.generateDrawer(toolbar, R.color.nav_drawer_dark_background, R.color.nav_drawer_dark_primary_text, R.color.nav_drawer_dark_secondary_text, R.color.nav_drawer_dark_primary_icon, R.color.nav_drawer_dark_selected_text, R.color.nav_drawer_dark_selected_text, R.color.nav_drawer_dark_selected, header, this, this, toolbarShadow);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data.hasExtra("childIntent")) {
            HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.nav_home));
            FavoriteFragment favoriteFragment = (FavoriteFragment) getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.nav_favorite));
            CategoryFragment categoryFragment = (CategoryFragment) getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.nav_category));
            OpenSourceFragment openSourceFragment = (OpenSourceFragment) getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.nav_open_source));
            AboutFragment aboutFragment = (AboutFragment) getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.nav_about));

            if (homeFragment != null && homeFragment.isVisible())
                DrawerUtils.getDrawer().setSelection(8, false);
            if (favoriteFragment != null && favoriteFragment.isVisible())
                DrawerUtils.getDrawer().setSelection(2, false);
            if (categoryFragment != null && categoryFragment.isVisible())
                DrawerUtils.getDrawer().setSelection(1, false);
            if (openSourceFragment != null && openSourceFragment.isVisible())
                DrawerUtils.getDrawer().setSelection(5, false);
            if (aboutFragment != null && aboutFragment.isVisible())
                DrawerUtils.getDrawer().setSelection(7, false);


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_changelog, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuChangelogItem:
                OtherUtils.showChangeLogDialog(HomeActivity.this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void checkAndSetFirstRun() {
        SharedPreferences sharedPreferences = getSharedPreferences("RecipediaPreference", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("isFirstRun", true)) {
            sharedPreferences.edit().putBoolean("isFirstRun", false).apply();
            sharedPreferences.edit().putInt("appVersion", pInfo.versionCode).apply();
            OtherUtils.showChangeLogDialog(this);
        }

    }
}
