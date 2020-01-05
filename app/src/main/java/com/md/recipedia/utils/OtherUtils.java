package com.md.recipedia.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.afollestad.materialdialogs.MaterialDialog;
import com.md.recipedia.R;
import com.md.recipedia.adapter.BulletListAdapter;

import java.io.IOException;
import java.io.InputStream;

public class OtherUtils {

    public static int getToolbarHeight(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{R.attr.actionBarSize});
        int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        return toolbarHeight;
    }
    public static String loadJSONFromAsset(Context context,String jsonFile) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(jsonFile);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }
    public static boolean isFav(Context context, SQLiteDatabase database, String value) {

        Cursor cursor = database.rawQuery("select recipe from favList where recipe=?", new String[]{value});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public static boolean isVersionUpdated(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RecipediaPreference", Context.MODE_PRIVATE);
        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        int currentVersion = pInfo.versionCode;
        int prefsVeriosn = sharedPreferences.getInt("appVersion", 0);
        if (currentVersion > prefsVeriosn) {
            OtherUtils.showChangeLogDialog(context);
            sharedPreferences.edit().putInt("appVersion", currentVersion).apply();
        }

        return true;
    }

    public static void showChangeLogDialog(Context context) {
        final BulletListAdapter adapter = new BulletListAdapter(context, R.array.changelog);
        new MaterialDialog.Builder(context)
                .title("Changelog")
                .positiveText("Okay")
                .adapter(adapter, null)
                .show();
    }
}
