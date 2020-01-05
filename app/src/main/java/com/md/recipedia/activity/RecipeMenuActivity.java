package com.md.recipedia.activity;


import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.md.recipedia.R;
import com.md.recipedia.adapter.RecipeMenuAdapter;
import com.md.recipedia.modal.RecipeMenuModal;
import com.md.recipedia.utils.OtherUtils;
import com.pnikosis.materialishprogress.ProgressWheel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeMenuActivity extends BaseActivity {

    List<RecipeMenuModal> recipeMenuModalList;
    public RecipeMenuAdapter recipeAdapter;
    String title, category;
    @BindString(R.string.json_file_url)
    String JSON_URL;
    RecipeMenuModal recipeMenuModal;
    RequestQueue requestQueue;
    MaterialDialog noInternetDialog;
    @BindView(R.id.progressBar)
    ProgressWheel loadingSpinner;
    @BindView(R.id.recipe_recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar_recipe_menu)
    Toolbar toolbar;
    View toolbarShadow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_menu);
        ButterKnife.bind(this);

        //setting up dialog for no internet connection
        noInternetDialog = new MaterialDialog.Builder(this)
                .title(R.string.no_internet_connection_dialog_title)
                .content(R.string.no_internet_connection_dialog_content)
                .positiveText("RETRY")
                .negativeText("GO BACK")
                .cancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        recreate();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        RecipeMenuActivity.super.onBackPressed();
                    }
                })
                .build();
/*
        if (!isNetworkAvailable()) {
            showDialog();

        }*/


        //setup toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(4);


        //setting up intent extras that comes from previous activityy
        Intent i = getIntent();
        title = i.getStringExtra("TITLE");
        category = i.getStringExtra("CATEGORY");

        //setting title of toolbar
        getSupportActionBar().setTitle(title);

        toolbarShadow = findViewById(R.id.toolbarShadowRecipeMenu);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(4);
            toolbarShadow.setVisibility(View.GONE);

        }
        else
        {
            toolbarShadow.setVisibility(View.VISIBLE);

        }
        //setting up recyclerview, it's adapter, animator, and layout manager... etc...
        recipeMenuModalList = new ArrayList<>();
        recipeAdapter = new RecipeMenuAdapter(this, recipeMenuModalList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recipeAdapter);


        //request queue of volley
        requestQueue = Volley.newRequestQueue(this);

        prepareData();

    }

    private void prepareData() {
        try {
            JSONObject jsonFile = new JSONObject(OtherUtils.loadJSONFromAsset(this, "recipes.json"));
            JSONArray recipeArray = jsonFile.getJSONArray("recipe");
            TypedArray recipe_banner = getResources().obtainTypedArray(R.array.recipe_banner);
            for (int i = 0; i < recipeArray.length(); i++) {
                JSONObject recipe = recipeArray.getJSONObject(i);
                if (category.equals(recipe.getString("category"))) {
                    int recipeBanner = recipe_banner.getResourceId(i, -1);
                    recipeMenuModal = new RecipeMenuModal(recipe.getString("name"), recipe.getString("serves"), recipe.getString("makes"), recipe.getString("preparation"), recipe.getString("cooking"), recipe.getString("ingredients"), recipe.getString("method"), recipeBanner);
                    recipeMenuModalList.add(recipeMenuModal);
                }
            }
            recipe_banner.recycle();
            if (recipeMenuModalList.size() > 0) {

                recipeAdapter.notifyDataSetChanged();
                loadingSpinner.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
        }

    }
/*
    private void prepareData() {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, JSON_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("recipe");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject recipe = jsonArray.getJSONObject(i);
                                if (category.equals(recipe.getString("category"))) {
                                    recipeMenuModal = new RecipeMenuModal(recipe.getString("name"), recipe.getString("banner"), recipe.getString("serves"),recipe.getString("makes"), recipe.getString("preparation"),recipe.getString("cooking"), recipe.getString("ingredients"), recipe.getString("method"));
                                    recipeMenuModalList.add(recipeMenuModal);
                                }
                            }
                            if (recipeMenuModalList.size() > 0) {

                                recipeAdapter.notifyDataSetChanged();
                                loadingSpinner.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                            }

                        } catch (Exception e) {

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }
        );
        requestQueue.add(jsonObjReq);

    }
*/

    public void showDialog() {
        noInternetDialog.show();
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_random, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuShuffleItem:
                Collections.shuffle(recipeMenuModalList);
                recipeAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        recipeAdapter.notifyDataSetChanged();
    }

    public RecipeMenuAdapter getAdapter() {
        return this.recipeAdapter;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
