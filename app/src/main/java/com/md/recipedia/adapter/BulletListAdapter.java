package com.md.recipedia.adapter;


import android.content.Context;
import android.support.annotation.ArrayRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.md.recipedia.R;

import butterknife.ButterKnife;

public class BulletListAdapter extends RecyclerView.Adapter<BulletListAdapter.ChangelogVH> {

    private final CharSequence[] mItems;

    public BulletListAdapter(@NonNull Context context, @ArrayRes int items) {
        mItems = context.getResources().getTextArray(items);
    }

    @Override
    public ChangelogVH onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bullet, parent, false);
        return new ChangelogVH(view);
    }

    @Override
    public void onBindViewHolder(ChangelogVH holder, int position) {
        holder.title.setText(Html.fromHtml(mItems[position].toString()));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mItems != null ? mItems.length : 0;
    }

    public static class ChangelogVH extends RecyclerView.ViewHolder {

        final TextView title;

        public ChangelogVH(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }
}