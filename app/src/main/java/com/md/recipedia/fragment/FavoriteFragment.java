package com.md.recipedia.fragment;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.md.recipedia.R;
import com.md.recipedia.adapter.FavoriteAdapter;
import com.md.recipedia.modal.FavoriteModal;
import com.md.recipedia.utils.OtherUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SHARIQUE on 24/09/2016.
 */

public class FavoriteFragment extends Fragment {
    public RecyclerView recyclerView;
    public List<FavoriteModal> favoriteModalList;
    FavoriteAdapter recipeAdapter;
    String JSON_URL, title, msg;
    FavoriteModal r;
    String[] recipe_name, image_url;
    RequestQueue requestQueue;
    MaterialDialog noInternetDialog;
    public TextView noFav;
    SQLiteDatabase database;
    LinearLayout linearLayout;
    View view;

    public FavoriteFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_favorite, container, false);


        //url string for json file
        JSON_URL = getResources().getString(R.string.json_file_url);
        database = view.getContext().openOrCreateDatabase("recipedia.db", Context.MODE_PRIVATE, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS favList(recipe TEXT)");
        //setting up recyclerview, it's adapter, animator, and layout manager... etc...
        recyclerView = (RecyclerView) view.findViewById(R.id.fav_recyclerview);
        favoriteModalList = new ArrayList<>();
        recipeAdapter = new FavoriteAdapter(view.getContext(), favoriteModalList);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recipeAdapter);
        noFav = (TextView) view.findViewById(R.id.noFav);
        linearLayout = (LinearLayout) view.findViewById(R.id.recipeFavHolder);

        if (checkData()) {
            noFav.setText("Loading...");
        }

        //request queue of volley
        requestQueue = Volley.newRequestQueue(view.getContext());

        prepareData();
        return view;
    }

    private void prepareData() {
        try
        {
            JSONObject jsonFile= new JSONObject(OtherUtils.loadJSONFromAsset(view.getContext(),"recipes.json"));
            JSONArray recipeArray=jsonFile.getJSONArray("recipe");
            TypedArray recipe_banner = getResources().obtainTypedArray(R.array.recipe_banner);
            for (int i = 0; i < recipeArray.length(); i++) {
                JSONObject recipe = recipeArray.getJSONObject(i);
                if(OtherUtils.isFav(view.getContext(),database,recipe.getString("name").toLowerCase().replace(",", "").replace(" ", "_")))
                {
                    int recipeBanner=recipe_banner.getResourceId(i,-1);
                    r = new FavoriteModal(recipe.getString("name"), recipe.getString("serves"), recipe.getString("makes"), recipe.getString("preparation"), recipe.getString("cooking"), recipe.getString("ingredients"), recipe.getString("method"),recipeBanner);
                    favoriteModalList.add(r);
                }
            }
            recipe_banner.recycle();
            if (favoriteModalList.size() > 0) {
                recipeAdapter.notifyDataSetChanged();
                noFav.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        }
        catch (Exception e){}

    }

    /*private void prepareData() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, JSON_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("recipe");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject recipe = jsonArray.getJSONObject(i);
                                if(OtherUtils.isFav(view.getContext(),database,recipe.getString("name").toLowerCase().replace(",", "").replace(" ", "_")))
                                {
                                    r = new FavoriteModal(recipe.getString("name"), recipe.getString("banner"), recipe.getString("serves"),recipe.getString("makes"), recipe.getString("preparation"),recipe.getString("cooking"), recipe.getString("ingredients"), recipe.getString("method"));
                                    favoriteModalList.add(r);
                                }
                            }
                            if (favoriteModalList.size() > 0) {
                                recipeAdapter.notifyDataSetChanged();
                                noFav.setVisibility(View.GONE);
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
    }*/


    @Override
    public void onResume() {
        super.onResume();
        recipeAdapter.notifyDataSetChanged();
    }


    public boolean checkData() {
        Cursor cursor = database.rawQuery("select * from favList", null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }


    
}

