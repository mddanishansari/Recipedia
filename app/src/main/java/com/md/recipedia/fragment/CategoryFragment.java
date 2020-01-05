package com.md.recipedia.fragment;


import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.md.recipedia.R;
import com.md.recipedia.adapter.CategoryAdapter;
import com.md.recipedia.modal.CategoryModal;

import java.util.ArrayList;
import java.util.List;


public class CategoryFragment extends Fragment {

    RecyclerView recyclerView;
    public static List<CategoryModal> categoryModalList;
    public static CategoryAdapter categoryAdapter;


    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        //initializing recyclerview, list and adapter
        recyclerView = (RecyclerView) view.findViewById(R.id.category_recyclerview);
        categoryModalList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(view.getContext(), categoryModalList);

        //setting various recycler view properties
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(categoryAdapter);
        prepareData();


        return view;
    }

    private void prepareData() {
        CategoryModal c;
        TypedArray imgs = getResources().obtainTypedArray(R.array.category_banner);

        String[] category_name = getResources().getStringArray(R.array.category_name);
        String[] category_banner = getResources().getStringArray(R.array.category_banner);
        for (int i = 0; i < category_name.length; i++) {
            c = new CategoryModal(category_name[i], imgs.getResourceId(i,-1));
            categoryModalList.add(c);
        }
        imgs.recycle();
        categoryAdapter.notifyDataSetChanged();
    }

}
