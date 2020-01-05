package com.md.recipedia.adapter;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.md.recipedia.R;
import com.md.recipedia.activity.MainRecipeActivity;
import com.md.recipedia.modal.RecipeMenuModal;

import java.util.List;

public class RecipeMenuAdapter extends RecyclerView.Adapter<RecipeMenuAdapter.MyViewHolder> {

    Context mContext;
    List<RecipeMenuModal> recipeMenuModalList;
    boolean isfav;
    SQLiteDatabase database;
    String favToastmsg;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvRecipeName;
        ImageView ivRecipeBanner, ivFavorite;
        CardView cvRecipe;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvRecipeName = (TextView) itemView.findViewById(R.id.tvRecipeName);
            ivRecipeBanner = (ImageView) itemView.findViewById(R.id.ivRecipeBanner);
            ivFavorite = (ImageView) itemView.findViewById(R.id.ivFavorite);
            cvRecipe = (CardView) itemView.findViewById(R.id.card_view_recipe_menu);
        }
    }

    public RecipeMenuAdapter(Context mContext, List<RecipeMenuModal> recipeMenuModalList) {
        this.mContext = mContext;
        this.recipeMenuModalList = recipeMenuModalList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe_menu, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final RecipeMenuModal recipeMenuModal = recipeMenuModalList.get(position);
        final String recipename = recipeMenuModal.getRecipe_name().toLowerCase().replace(",", "").replace(" ", "_");

        database = mContext.openOrCreateDatabase("recipedia.db", Context.MODE_PRIVATE, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS favList(recipe TEXT)");


        if (isFav(recipename))
            holder.ivFavorite.setImageResource(R.drawable.ic_favorite);
         else
            holder.ivFavorite.setImageResource(R.drawable.ic_not_favorite);


        holder.ivFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFav(recipename)) {
                    favToastmsg = "Addedd to favorite list";
                    Toast.makeText(mContext, favToastmsg, Toast.LENGTH_SHORT).show();
                    holder.ivFavorite.setImageResource(R.drawable.ic_favorite);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("recipe", recipename);
                    database.insert("favList", null, contentValues);
                } else if (isFav(recipename)) {
                    favToastmsg = "Removed from favorite list";
                    Toast.makeText(mContext, favToastmsg, Toast.LENGTH_SHORT).show();
                    holder.ivFavorite.setImageResource(R.drawable.ic_not_favorite);
                    database.delete("favList", "recipe=?", new String[]{recipename});
                }
            }
        });
        holder.tvRecipeName.setText(recipeMenuModal.getRecipe_name());


        Glide.with(mContext)
                .load(recipeMenuModal.getRecipe__banner())
                .placeholder(R.drawable.place_holder)
                .into(holder.ivRecipeBanner);

        holder.cvRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("LOLOLOLOLO","sfasfasg");
                Intent i = new Intent(mContext, MainRecipeActivity.class);
                i.putExtra("TITLE", recipeMenuModal.getRecipe_name());
                i.putExtra("SERVES", recipeMenuModal.getRecipe_serves());
                i.putExtra("MAKES", recipeMenuModal.getRecipe_makes());
                i.putExtra("PREPARATION", recipeMenuModal.getRecipe_preparation());
                i.putExtra("COOKING", recipeMenuModal.getRecipe_cooking());
                i.putExtra("INGREDIENTS", recipeMenuModal.getRecipe_ingredients());
                i.putExtra("METHOD", recipeMenuModal.getRecipe_method());
                i.putExtra("BANNER", recipeMenuModal.getRecipe__banner());

                mContext.startActivity(i);
            }
        });

    }

    public boolean isFav(String value) {
        Cursor cursor = database.rawQuery("select recipe from favList where recipe=?", new String[]{value});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    @Override
    public int getItemCount() {
        return recipeMenuModalList.size();
    }

}
