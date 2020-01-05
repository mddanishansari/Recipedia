package com.md.recipedia.activity;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.md.recipedia.R;
import com.md.recipedia.modal.RecipeMenuModal;
import com.md.recipedia.utils.OtherUtils;
import com.mikepenz.iconics.context.IconicsContextWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Random;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RandomRecipeActivity extends BaseActivity {
    String sharingText, JSON_URL;
    RequestQueue requestQueue;
    RecipeMenuModal r;
    SQLiteDatabase database;
    int randomIndex;
    String[] recipe_name, recipe_serves, recipe_makes, recipe_preparation, recipe_cooking, recipe_ingredients, recipe_method;
    int[] recipe_banner;
    @BindString(R.string.share_recipe_footer)
    String SHARE_LINK;
    String recipe_key, favMsg;
    MenuItem itemFav, itemShare;
    MaterialDialog waitDialog;
    @BindView(R.id.toolbar_main_recipe)
    Toolbar toolbar;
    @BindView(R.id.header)
    ImageView ivHeader;

    @BindView(R.id.layoutServes)
    View servesView;
    @BindView(R.id.layoutMakes)
    View makesView;
    @BindView(R.id.layoutPreparation)
    View preparationView;
    @BindView(R.id.layoutCooking)
    View cookingView;
    @BindView(R.id.layoutIngredients)
    View ingredientsView;
    @BindView(R.id.layoutMethod)
    View methodView;
    TextView tvServesTitle, tvMakesTitle, tvPreparationTitle, tvCookingTitle, tvIngredientsTitle, tvMethodTitle;
    TextView tvServesBody, tvMakesBody, tvPreparationBody, tvCookingBody, tvIngredientsBody, tvMethodBody;

    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.nestedScrollingLinearLayout)
    LinearLayout nestedScrolling;
    FloatingActionButton fab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_random_recipe);
        ButterKnife.bind(this);
/*
        if (!isNetworkAvailable())
        {
            RandomRecipeActivity.super.onBackPressed();
            Toast.makeText(RandomRecipeActivity.this,getResources().getString(R.string.no_internet_connection),Toast.LENGTH_SHORT).show();
        }
        */
        //toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RandomRecipeActivity.class);
                startActivityForResult(intent, 1);
                finish();

            }
        });
        database = this.openOrCreateDatabase("recipedia.db", Context.MODE_PRIVATE, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS favList(recipe TEXT)");

        //url string for json file
        JSON_URL = getResources().getString(R.string.json_file_url);

        collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        //collapsingToolbar.setTitle(title);
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.expandedappbar);

        //request queue of volley
        requestQueue = Volley.newRequestQueue(this);

        //binding title text views
        tvServesTitle = (TextView) servesView.findViewById(R.id.recipeTtitle);
        tvMakesTitle = (TextView) makesView.findViewById(R.id.recipeTtitle);
        tvPreparationTitle = (TextView) preparationView.findViewById(R.id.recipeTtitle);
        tvCookingTitle = (TextView) cookingView.findViewById(R.id.recipeTtitle);
        tvIngredientsTitle = (TextView) ingredientsView.findViewById(R.id.recipeTtitle);
        tvMethodTitle = (TextView) methodView.findViewById(R.id.recipeTtitle);

        //populating title text views
        tvServesTitle.setText(getResources().getString(R.string.serves));
        tvMakesTitle.setText(getResources().getString(R.string.makes));
        tvPreparationTitle.setText(getResources().getString(R.string.preparation));
        tvCookingTitle.setText(getResources().getString(R.string.cooking));
        tvIngredientsTitle.setText(getResources().getString(R.string.ingredients));
        tvMethodTitle.setText(getResources().getString(R.string.method));

        //binding body text views
        tvServesBody = (TextView) servesView.findViewById(R.id.recipeBody);
        tvMakesBody = (TextView) makesView.findViewById(R.id.recipeBody);
        tvPreparationBody = (TextView) preparationView.findViewById(R.id.recipeBody);
        tvCookingBody = (TextView) cookingView.findViewById(R.id.recipeBody);
        tvIngredientsBody = (TextView) ingredientsView.findViewById(R.id.recipeBody);
        tvMethodBody = (TextView) methodView.findViewById(R.id.recipeBody);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            nestedScrolling.setNestedScrollingEnabled(false);
        }
        prepareData();
        /*
        waitDialog = new MaterialDialog.Builder(this)
                .content(R.string.just_a_sec)
                .progress(true, 0)
                .cancelable(false)
                .show();
*/

        Intent data = getIntent();
        data.putExtra("childIntent", "");
        setResult(RESULT_OK, data);


    }

    private void prepareData() {
        try {
            JSONObject jsonFile = new JSONObject(OtherUtils.loadJSONFromAsset(this, "recipes.json"));
            JSONArray recipeArray = jsonFile.getJSONArray("recipe");
            recipe_name = new String[recipeArray.length()];
            recipe_banner = new int[recipeArray.length()];
            recipe_serves = new String[recipeArray.length()];
            recipe_makes = new String[recipeArray.length()];
            recipe_preparation = new String[recipeArray.length()];
            recipe_cooking = new String[recipeArray.length()];
            recipe_ingredients = new String[recipeArray.length()];
            recipe_method = new String[recipeArray.length()];
            randomIndex = new Random().nextInt(recipeArray.length());
            TypedArray recipeBanner = getResources().obtainTypedArray(R.array.recipe_banner);


            for (int i = 0; i < recipeArray.length(); i++) {
                JSONObject recipe = recipeArray.getJSONObject(i);
                recipe_name[i] = recipe.getString("name");
                recipe_banner[i] = recipeBanner.getResourceId(i, -1);
                recipe_serves[i] = recipe.getString("serves");
                recipe_makes[i] = recipe.getString("makes");
                recipe_preparation[i] = recipe.getString("preparation");
                recipe_cooking[i] = recipe.getString("cooking");
                recipe_ingredients[i] = recipe.getString("ingredients");
                recipe_method[i] = recipe.getString("method");
            }
            recipeBanner.recycle();

        } catch (Exception e) {
        } finally {
            Glide.with(getApplicationContext())
                    .load(recipe_banner[randomIndex])
                    .placeholder(R.drawable.place_holder)
                    .into(ivHeader);
            collapsingToolbar.setTitle(recipe_name[randomIndex]);
            populateBody(recipe_serves[randomIndex], servesView, tvServesBody);
            populateBody(recipe_makes[randomIndex], makesView, tvMakesBody);
            populateBody(recipe_preparation[randomIndex], preparationView, tvPreparationBody);
            populateBody(recipe_cooking[randomIndex], cookingView, tvCookingBody);
            populateBody(recipe_ingredients[randomIndex], ingredientsView, tvIngredientsBody);
            populateBody(recipe_method[randomIndex], methodView, tvMethodBody);

            recipe_key = recipe_name[randomIndex].toLowerCase().replace(",", "").replace(" ", "_");
            sharingText = "Checkout the recipe for " + recipe_name[randomIndex] + " on Recipedia\n\n" + getResources().getString(R.string.playstore_link);
            //          waitDialog.dismiss();
        }
    }

    /*
    private void prepareData() {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, JSON_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("JSON_RESPONSE", response.toString());
                        try {
                            JSONArray jsonArray = response.getJSONArray("recipe");
                            Log.d("JSON_ARRAY_LENGTH", String.valueOf(jsonArray.length()));
                            recipe_name = new String[jsonArray.length()];
                            recipe_banner = new String[jsonArray.length()];
                            recipe_serves = new String[jsonArray.length()];
                            recipe_makes = new String[jsonArray.length()];
                            recipe_preparation = new String[jsonArray.length()];
                            recipe_cooking = new String[jsonArray.length()];
                            recipe_ingredients = new String[jsonArray.length()];
                            recipe_method = new String[jsonArray.length()];
                            randomIndex = new Random().nextInt(jsonArray.length());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject recipe = jsonArray.getJSONObject(i);
                                recipe_name[i] = recipe.getString("name");
                                recipe_banner[i] = recipe.getString("banner");
                                recipe_serves[i] = recipe.getString("serves");
                                recipe_makes[i] = recipe.getString("makes");
                                recipe_preparation[i] = recipe.getString("preparation");
                                recipe_cooking[i] = recipe.getString("cooking");
                                recipe_ingredients[i] = recipe.getString("ingredients");
                                recipe_method[i] = recipe.getString("method");
                            }

                        } catch (Exception e) {
                            System.out.print(e);
                        } finally {
                            Glide.with(getApplicationContext())
                                    .load(recipe_banner[randomIndex])
                                    .placeholder(R.drawable.place_holder)
                                    .into(ivHeader);
                            collapsingToolbar.setTitle(recipe_name[randomIndex]);
                            populateBody(recipe_serves[randomIndex], servesView, tvServesBody);
                            populateBody(recipe_makes[randomIndex], makesView, tvMakesBody);
                            populateBody(recipe_preparation[randomIndex], preparationView, tvPreparationBody);
                            populateBody(recipe_cooking[randomIndex], cookingView, tvCookingBody);
                            populateBody(recipe_ingredients[randomIndex], ingredientsView, tvIngredientsBody);
                            populateBody(recipe_method[randomIndex], methodView, tvMethodBody);



                            recipe_key = recipe_name[randomIndex].toLowerCase().replace(",", "").replace(" ", "_");
                            sharingText = "Checkout the recipe for "+recipe_name[randomIndex]+" on Recipedia\n\n"+getResources().getString(R.string.playstore_link);

                            waitDialog.dismiss();
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
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_recipe_activity, menu);

        itemFav = menu.getItem(0);
        itemShare = menu.getItem(1);
        if (recipe_key != null) {
            if (OtherUtils.isFav(RandomRecipeActivity.this, database, recipe_key)) {
                itemFav.setIcon(R.drawable.ic_menu_favorite);
            } else {
                itemFav.setIcon(R.drawable.ic_menu_not_favorite);
            }
        }


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuShareItem:
                Intent sharingItent = new Intent(Intent.ACTION_SEND);
                sharingItent.setType("text/*");
                sharingItent.putExtra(Intent.EXTRA_TEXT, sharingText);
                startActivity(Intent.createChooser(sharingItent, "Share recipe via"));
                return true;
            case R.id.menuFavItem:
                if (OtherUtils.isFav(RandomRecipeActivity.this, database, recipe_key)) {
                    favMsg = "Removed from favorite list";
                    Toast.makeText(this, favMsg, Toast.LENGTH_SHORT).show();
                    itemFav.setIcon(R.drawable.ic_menu_not_favorite);
                    database.delete("favList", "recipe=?", new String[]{recipe_key});

                } else {
                    favMsg = "Addedd to favorite list";
                    Toast.makeText(this, favMsg, Toast.LENGTH_SHORT).show();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("recipe", recipe_key);
                    database.insert("favList", null, contentValues);
                    itemFav.setIcon(R.drawable.ic_menu_favorite);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public static void populateBody(String value, View view, TextView textView) {
        if (value.equals(""))
            view.setVisibility(View.GONE);
        else
            textView.setText(Html.fromHtml(value));

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase));
    }


}
