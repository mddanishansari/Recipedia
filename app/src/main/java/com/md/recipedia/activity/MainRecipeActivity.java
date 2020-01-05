package com.md.recipedia.activity;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
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

import com.bumptech.glide.Glide;
import com.md.recipedia.R;
import com.md.recipedia.fragment.FavoriteFragment;
import com.md.recipedia.utils.OtherUtils;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainRecipeActivity extends BaseActivity {
    String title, serves, makes, preparation, cooking, ingredients, method, sharingText, recipe_key;
    int banner;
    @BindString(R.string.share_recipe_footer)
    String SHARE_LINK;
    MenuItem favItem;
    SQLiteDatabase database;
    String favMsg;
    FavoriteFragment favoriteFragment;
    RecipeMenuActivity recipeMenuActivity;
    @BindView(R.id.header)
    ImageView ivHeader;
    //binding views
    @BindView(R.id.toolbar_main_recipe)
    Toolbar toolbar;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_recipe);
        ButterKnife.bind(this);

        //toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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


        //getting texts
        Intent recievingIntent = getIntent();
        title = recievingIntent.getStringExtra("TITLE");
        banner = recievingIntent.getIntExtra("BANNER",R.drawable.place_holder);
        serves = recievingIntent.getStringExtra("SERVES");
        makes = recievingIntent.getStringExtra("MAKES");
        preparation = recievingIntent.getStringExtra("PREPARATION");
        cooking = recievingIntent.getStringExtra("COOKING");
        ingredients = recievingIntent.getStringExtra("INGREDIENTS");
        method = recievingIntent.getStringExtra("METHOD");

        recipe_key = title.toLowerCase().replace(",", "").replace(" ", "_");

        sharingText = "Checkout the recipe for "+title+" on Recipedia\n\n"+getResources().getString(R.string.playstore_link);


        database = this.openOrCreateDatabase("recipedia.db", Context.MODE_PRIVATE, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS favList(recipe TEXT)");


        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        collapsingToolbar.setTitle(title);
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.expandedappbar);



            Glide.with(getApplicationContext())
                    .load(banner)
                    .placeholder(R.drawable.place_holder)
                    .into(ivHeader);

        populateBody(serves, servesView, tvServesBody);
        populateBody(makes, makesView, tvMakesBody);
        populateBody(preparation, preparationView, tvPreparationBody);
        populateBody(cooking, cookingView, tvCookingBody);
        populateBody(ingredients, ingredientsView, tvIngredientsBody);
        populateBody(method, methodView, tvMethodBody);


        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.nestedScrollingLinearLayout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            linearLayout.setNestedScrollingEnabled(false);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_recipe_activity, menu);

        favItem = menu.getItem(0);
        if (OtherUtils.isFav(MainRecipeActivity.this, database, recipe_key)) {
            favItem.setIcon(R.drawable.ic_menu_favorite);
        } else {
            favItem.setIcon(R.drawable.ic_menu_not_favorite);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        favoriteFragment = new FavoriteFragment();
        recipeMenuActivity = new RecipeMenuActivity();
        switch (item.getItemId()) {
            case R.id.menuShareItem:
                Intent sharingItent = new Intent(Intent.ACTION_SEND);
                sharingItent.setType("text/*");
                sharingItent.putExtra(Intent.EXTRA_TEXT, sharingText);
                startActivity(Intent.createChooser(sharingItent, "Share with"));
                return true;
            case R.id.menuFavItem:
                if (OtherUtils.isFav(MainRecipeActivity.this, database, recipe_key)) {
                    favMsg = "Removed from favorite list";
                    Toast.makeText(this, favMsg, Toast.LENGTH_SHORT).show();
                    favItem.setIcon(R.drawable.ic_menu_not_favorite);
                    database.delete("favList", "recipe=?", new String[]{recipe_key});

                } else {
                    favMsg = "Addedd to favorite list";
                    Toast.makeText(this, favMsg, Toast.LENGTH_SHORT).show();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("recipe", recipe_key);
                    database.insert("favList", null, contentValues);
                    favItem.setIcon(R.drawable.ic_menu_favorite);
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
}
