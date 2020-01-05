package com.md.recipedia.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.md.recipedia.R;
import com.md.recipedia.activity.RecipeMenuActivity;
import com.md.recipedia.modal.CategoryModal;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    private Context mContext;
    private List<CategoryModal> categoryModalList;


    public CategoryAdapter(Context mContext, List<CategoryModal> categoryModalList) {
        this.mContext = mContext;
        this.categoryModalList = categoryModalList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe_category, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final CategoryModal categoryModal = categoryModalList.get(position);
        holder.tvCategoryName.setText(categoryModal.getCategory_name());

            Glide.with(mContext)
                    .load(categoryModal.getCategory_banner())
                    .placeholder(R.drawable.place_holder)
                    .into(holder.ivCategoryBanner);



        holder.cvCategory.setOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View view) {
                                                     Intent i = new Intent(mContext, RecipeMenuActivity.class);
                                                     i.putExtra("TITLE", categoryModal.getCategory_name());
                                                     i.putExtra("CATEGORY", categoryModal.getCategory_name().toLowerCase().replace(",", "").replace(" ", "_"));
                                                     mContext.startActivity(i);
                                                 }
                                             }
        );
    }

    @Override
    public int getItemCount() {
        return categoryModalList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvCategoryName)
        TextView tvCategoryName;
        @BindView(R.id.ivCategoryBanner)
        ImageView ivCategoryBanner;
        @BindView(R.id.cvCategoryCard)
        CardView cvCategory;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


        }
    }
}
