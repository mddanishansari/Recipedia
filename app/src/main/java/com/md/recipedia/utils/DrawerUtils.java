package com.md.recipedia.utils;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.md.phlex.Phlex;
import com.md.recipedia.R;
import com.md.recipedia.activity.RandomRecipeActivity;
import com.md.recipedia.fragment.AboutFragment;
import com.md.recipedia.fragment.CategoryFragment;
import com.md.recipedia.fragment.FavoriteFragment;
import com.md.recipedia.fragment.HomeFragment;
import com.md.recipedia.fragment.OpenSourceFragment;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class DrawerUtils {

    public static Drawer drawer;
    public static PrimaryDrawerItem item;
    public static int identifier;
    public static MaterialDialog RateShareDialog;


    public static Drawer getDrawer() {
        return drawer;
    }

    public static void generateDrawer(final Toolbar toolbar, int backgroundColor, int textColor, int secondaryTextColor, int iconColor, int selectedTextColor, int selectedIconColor, int selectedColor, AccountHeader header, final Context context, final AppCompatActivity appCompatActivity, final View toolbarShadow) {
        PrimaryDrawerItem itemHome = new PrimaryDrawerItem().withIdentifier(8).withName(context.getResources().getString(R.string.nav_home)).withIcon(GoogleMaterial.Icon.gmd_home).withTextColorRes(textColor).withIconColorRes(iconColor).withSelectedColorRes(selectedColor).withSelectedTextColorRes(selectedTextColor).withSelectedIconColorRes(selectedIconColor);
        PrimaryDrawerItem itemCategory = new PrimaryDrawerItem().withIdentifier(1).withName(context.getResources().getString(R.string.nav_category)).withIcon(GoogleMaterial.Icon.gmd_bubble_chart).withTextColorRes(textColor).withIconColorRes(iconColor).withSelectedColorRes(selectedColor).withSelectedTextColorRes(selectedTextColor).withSelectedIconColorRes(selectedIconColor);
        PrimaryDrawerItem itemFav = new PrimaryDrawerItem().withIdentifier(2).withName(context.getResources().getString(R.string.nav_favorite)).withIcon(GoogleMaterial.Icon.gmd_favorite_border).withTextColorRes(textColor).withIconColorRes(iconColor).withSelectedColorRes(selectedColor).withSelectedTextColorRes(selectedTextColor).withSelectedIconColorRes(selectedIconColor);
        PrimaryDrawerItem itemRandom = new PrimaryDrawerItem().withIdentifier(3).withName(context.getResources().getString(R.string.nav_random)).withIcon(GoogleMaterial.Icon.gmd_shuffle).withTextColorRes(textColor).withIconColorRes(iconColor).withSelectedColorRes(selectedColor).withSelectedTextColorRes(selectedTextColor).withSelectedIconColorRes(selectedIconColor);
        PrimaryDrawerItem itemChangeTheme = new PrimaryDrawerItem().withIdentifier(4).withName(context.getResources().getString(R.string.nav_change_theme)).withIcon(GoogleMaterial.Icon.gmd_color_lens).withTextColorRes(textColor).withIconColorRes(iconColor).withSelectedColorRes(selectedColor).withSelectedTextColorRes(selectedTextColor).withSelectedIconColorRes(selectedIconColor);
        PrimaryDrawerItem itemOpenSource = new PrimaryDrawerItem().withIdentifier(5).withName(context.getResources().getString(R.string.nav_open_source)).withIcon(FontAwesome.Icon.faw_github).withTextColorRes(textColor).withIconColorRes(iconColor).withSelectedColorRes(selectedColor).withSelectedTextColorRes(selectedTextColor).withSelectedIconColorRes(selectedIconColor);
        PrimaryDrawerItem itemRateAndShare = new PrimaryDrawerItem().withIdentifier(6).withName(context.getResources().getString(R.string.nav_rate_and_share)).withIcon(GoogleMaterial.Icon.gmd_local_play).withTextColorRes(textColor).withIconColorRes(iconColor).withSelectedColorRes(selectedColor).withSelectedTextColorRes(selectedTextColor).withSelectedIconColorRes(selectedIconColor);
        PrimaryDrawerItem itemAbout = new PrimaryDrawerItem().withIdentifier(7).withName(context.getResources().getString(R.string.nav_about)).withIcon(GoogleMaterial.Icon.gmd_info).withTextColorRes(textColor).withIconColorRes(iconColor).withSelectedColorRes(selectedColor).withSelectedTextColorRes(selectedTextColor).withSelectedIconColorRes(selectedIconColor);
        drawer = new DrawerBuilder()
                .addDrawerItems(
                        //nav drawer items setup
                        itemHome, itemCategory, itemFav, itemRandom, itemChangeTheme, itemRateAndShare, itemAbout
                        /*
                        new SectionDrawerItem().withName("Recipe").withTextColorRes(secondaryTextColor).withDivider(false).withSubItems(itemCategory, itemFav, itemRandom).withEnabled(true).withIsExpanded(true),
                        new SectionDrawerItem().withName("UI").withTextColorRes(secondaryTextColor).withDivider(false).withSubItems(itemChangeTheme).withEnabled(true).withIsExpanded(true),
                        new SectionDrawerItem().withName("Misc").withTextColorRes(secondaryTextColor).withDivider(false).withSubItems(itemOpenSource, itemRateAndShare, itemAbout).withEnabled(true).withIsExpanded(true)
*/
                )
                .addStickyDrawerItems()
                .withToolbar(toolbar)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        int identifier = (int) drawerItem.getIdentifier();
                        if (drawerItem != null) {
                            Fragment f;
                            switch (identifier) {
                                case 1:
                                    Phlex.setToolbarElevation(toolbar, toolbarShadow, 4);
                                    f = new CategoryFragment();
                                    switchFragment(1, context.getResources().getString(R.string.nav_category), f, (AppCompatActivity) appCompatActivity);
                                    break;
                                case 2:
                                    Phlex.setToolbarElevation(toolbar, toolbarShadow, 4);
                                    f = new FavoriteFragment();
                                    switchFragment(2, context.getResources().getString(R.string.nav_favorite), f, (AppCompatActivity) appCompatActivity);
                                    break;
                                case 3:
                                    Phlex.setToolbarElevation(toolbar, toolbarShadow, 4);
                                    Intent intent = new Intent(context.getApplicationContext(), RandomRecipeActivity.class);
                                    appCompatActivity.startActivityForResult(intent, 1);

                                    break;
                                case 4:
                                    Phlex.setToolbarElevation(toolbar, toolbarShadow, 4);
                                    Toast.makeText(context.getApplicationContext(), "Theme Changed", Toast.LENGTH_SHORT).show();
                                    int currentTheme = ThemeUtils.getTheme(context.getApplicationContext());
                                    if (currentTheme <= 1) {
                                        ThemeUtils.setTheme(context.getApplicationContext(), 2);
                                    } else if (currentTheme == 2) {
                                        ThemeUtils.setTheme(context.getApplicationContext(), 1);
                                    }
                                    recreateActivity(appCompatActivity);
                                    break;
                                case 5:
                                    Phlex.setToolbarElevation(toolbar, toolbarShadow, 4);
                                    f = new OpenSourceFragment();
                                    switchFragment(5, context.getResources().getString(R.string.nav_open_source), f, appCompatActivity);
                                    break;
                                case 6:
                                    Phlex.setToolbarElevation(toolbar, toolbarShadow, 4);
                                    RateShareDialog = new MaterialDialog.Builder(appCompatActivity)
                                            .items(R.array.rate_and_share)
                                            .itemsCallback(new MaterialDialog.ListCallback() {
                                                @Override
                                                public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {

                                                    switch (position) {
                                                        case 0:
                                                            appCompatActivity.startActivity(Intent.createChooser(new Intent(Intent.ACTION_VIEW)
                                                                            .setData(Uri.parse(context.getResources().getString(R.string.playstore_link)))
                                                                    , "Open with"));
                                                            break;
                                                        case 1:
                                                            appCompatActivity.startActivity(Intent.createChooser(new Intent(Intent.ACTION_SEND)
                                                                    .setType("text/*")
                                                                    .putExtra(Intent.EXTRA_TEXT, context.getResources().getString(R.string.share_texts)), "Share with"));
                                                            break;
                                                    }
                                                    RateShareDialog.dismiss();
                                                }
                                            })
                                            .dismissListener(new DialogInterface.OnDismissListener() {
                                                @Override
                                                public void onDismiss(DialogInterface dialog) {
                                                    DrawerUtils.getDrawer().deselect();

                                                    HomeFragment homeFragment = (HomeFragment) appCompatActivity.getSupportFragmentManager().findFragmentByTag(context.getResources().getString(R.string.nav_home));
                                                    FavoriteFragment favoriteFragment = (FavoriteFragment) appCompatActivity.getSupportFragmentManager().findFragmentByTag(context.getResources().getString(R.string.nav_favorite));
                                                    CategoryFragment categoryFragment = (CategoryFragment) appCompatActivity.getSupportFragmentManager().findFragmentByTag(context.getResources().getString(R.string.nav_category));
                                                    OpenSourceFragment openSourceFragment = (OpenSourceFragment) appCompatActivity.getSupportFragmentManager().findFragmentByTag(context.getResources().getString(R.string.nav_open_source));
                                                    AboutFragment aboutFragment = (AboutFragment) appCompatActivity.getSupportFragmentManager().findFragmentByTag(context.getResources().getString(R.string.nav_about));

                                                    if (favoriteFragment != null && favoriteFragment.isVisible())
                                                        DrawerUtils.getDrawer().setSelection(2, false);
                                                    if (homeFragment != null && homeFragment.isVisible())
                                                        DrawerUtils.getDrawer().setSelection(8, false);
                                                    if (categoryFragment != null && categoryFragment.isVisible())
                                                        DrawerUtils.getDrawer().setSelection(1, false);
                                                    if (openSourceFragment != null && openSourceFragment.isVisible())
                                                        DrawerUtils.getDrawer().setSelection(5, false);
                                                    if (aboutFragment != null && aboutFragment.isVisible())
                                                        DrawerUtils.getDrawer().setSelection(7, false);


                                                }
                                            })
                                            .show();
                                    break;
                                case 7:
                                    Phlex.setToolbarElevation(toolbar, toolbarShadow, 4);
                                    f = new AboutFragment();
                                    switchFragment(7, context.getResources().getString(R.string.nav_about), f, appCompatActivity);
                                    break;
                                case 8:
                                    Phlex.setToolbarElevation(toolbar, toolbarShadow, 4);
                                    f = new HomeFragment();
                                    switchFragment(8, context.getResources().getString(R.string.nav_home), f, appCompatActivity);
                                    break;
                            }
                        }


                        return false;
                    }
                })
                .withSliderBackgroundColorRes(backgroundColor)
                .withActionBarDrawerToggle(true)
                .withActivity(appCompatActivity)
                .withAccountHeader(header)
                .build();

        drawer.setSelection(itemHome);


    }

    public static void switchFragment(int position, String title, Fragment fragment, AppCompatActivity activity1) {
        activity1.getSupportActionBar().setTitle(title);
        FragmentTransaction transaction = activity1.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment, title);
        transaction.commit();

    }


    public static void recreateActivity(Activity activity1) {
        Intent intent = activity1.getIntent();

        activity1.finish();
        activity1.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        activity1.startActivity(intent);
        activity1.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
