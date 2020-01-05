package com.md.recipedia.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.md.recipedia.R;
import com.mikepenz.iconics.view.IconicsButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {
    IconicsButton fbBtn, gpBtn, ghBtn, emailBtn, psBtn;
    View view;

    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_about, container, false);

        return view;
    }

}
