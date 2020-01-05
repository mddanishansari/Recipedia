package com.md.recipedia.adapter;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.md.recipedia.R;
import com.md.recipedia.activity.MainRecipeActivity;
import com.md.recipedia.modal.FavoriteModal;
import com.md.recipedia.utils.OtherUtils;

import java.util.List;

import butterknife.ButterKnife;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.MyViewHolder> {

    Context mContext;
    List<FavoriteModal> lovedMenuModalList;
    SQLiteDatabase database;
    String favToastmsg;

    public FavoriteAdapter(Context mContext, List<FavoriteModal> lovedMenuModalList) {
        this.mContext = mContext;
        this.lovedMenuModalList = lovedMenuModalList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe_menu, parent, false);
        //PREFS_NAMES = "FavoritePrefs";
        //favPrefs = mContext.getSharedPreferences(PREFS_NAMES, 0);
        return new FavoriteAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final FavoriteModal favoriteModal = lovedMenuModalList.get(position);
        final String recipename = favoriteModal.getRecipe_name().toLowerCase().replace(",", "").replace(" ", "_");

        database = mContext.openOrCreateDatabase("recipedia.db", Context.MODE_PRIVATE, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS favList(recipe TEXT)");


        if (OtherUtils.isFav(mContext, database, recipename))
            holder.ivFavorite.setImageResource(R.drawable.ic_favorite);
        else
            holder.ivFavorite.setImageResource(R.drawable.ic_not_favorite);


        holder.ivFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!OtherUtils.isFav(mContext, database, recipename)) {
                    favToastmsg = "Addedd to favorite list";
                    Toast.makeText(mContext, favToastmsg, Toast.LENGTH_SHORT).show();

                    holder.ivFavorite.setImageResource(R.drawable.ic_favorite);

                    ContentValues contentValues = new ContentValues();
                    contentValues.put("recipe", recipename);
                    database.insert("favList", null, contentValues);
                } else if (OtherUtils.isFav(mContext, database, recipename)) {
                    favToastmsg = "Removed from favorite list";
                    Toast.makeText(mContext, favToastmsg, Toast.LENGTH_SHORT).show();
                    holder.ivFavorite.setImageResource(R.drawable.ic_not_favorite);
                    database.delete("favList", "recipe=?", new String[]{recipename});
                }

                lovedMenuModalList.remove(position);
                notifyDataSetChanged();


            }
        });
        holder.tvRecipeName.setText(favoriteModal.getRecipe_name());


            Glide.with(mContext)
                    .load(favoriteModal.getRecipe__banner())
                    .placeholder(R.drawable.place_holder)
                    .into(holder.ivRecipeBanner);

        holder.cvRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //do nothing
                Intent i = new Intent(mContext, MainRecipeActivity.class);
                i.putExtra("TITLE", favoriteModal.getRecipe_name());
                i.putExtra("SERVES", favoriteModal.getRecipe_serves());
                i.putExtra("MAKES", favoriteModal.getRecipe_makes());
                i.putExtra("PREPARATION", favoriteModal.getRecipe_preparation());
                i.putExtra("COOKING", favoriteModal.getRecipe_cooking());
                i.putExtra("INGREDIENTS", favoriteModal.getRecipe_ingredients());
                i.putExtra("METHOD", favoriteModal.getRecipe_method());
                i.putExtra("BANNER", favoriteModal.getRecipe__banner());

                mContext.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return lovedMenuModalList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvRecipeName, nofav;
        ImageView ivRecipeBanner, ivFavorite;
        CardView cvRecipe;
        LinearLayout linearLayout;


        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            tvRecipeName = (TextView) itemView.findViewById(R.id.tvRecipeName);
            ivRecipeBanner = (ImageView) itemView.findViewById(R.id.ivRecipeBanner);
            ivFavorite = (ImageView) itemView.findViewById(R.id.ivFavorite);
            cvRecipe = (CardView) itemView.findViewById(R.id.card_view_recipe_menu);
            nofav = (TextView) itemView.findViewById(R.id.noFav);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.recipeFavHolder);
        }
    }
}
