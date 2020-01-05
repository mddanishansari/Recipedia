package com.md.recipedia.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.md.recipedia.R;
import com.md.recipedia.utils.ThemeUtils;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.aboutlibraries.util.Colors;

/**
 * A simple {@link Fragment} subclass.
 */
public class OpenSourceFragment extends Fragment {
    AppCompatButton githubBtn, libraryBtn;
    View view;

    public OpenSourceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_open_source, container, false);
        libraryBtn = (AppCompatButton) view.findViewById(R.id.implementedLibraries);
        githubBtn = (AppCompatButton) view.findViewById(R.id.githubLink);

        libraryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ThemeUtils.getTheme(view.getContext()) <= 1) {
                    new LibsBuilder()
                            .withAboutIconShown(true)
                            .withAboutAppName(getResources().getString(R.string.app_name))
                            .withAboutVersionShown(true)
                            .withActivityTitle("Implemented Libraries")
                            .withActivityColor(new Colors(Color.parseColor("#F5F5F5"), Color.parseColor("#E0E0E0")))
                            .withActivityStyle(Libs.ActivityStyle.LIGHT)
                            .start(view.getContext());
                } else if (ThemeUtils.getTheme(getContext()) == 2) {
                    new LibsBuilder()
                            .withAboutIconShown(true)
                            .withAboutAppName(getResources().getString(R.string.app_name))
                            .withAboutVersionShown(true)
                            .withActivityTitle("Implemented Libraries")
                            .withActivityColor(new Colors(Color.parseColor("#212121"), Color.parseColor("#000000")))
                            .withActivityStyle(Libs.ActivityStyle.DARK)
                            .start(view.getContext());
                }
            }
        });

        githubBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(Intent.createChooser(new Intent(Intent.ACTION_VIEW)
                                .setData(Uri.parse(getResources().getString(R.string.github_link)))
                        , "Open with"));
            }
        });

        return view;
    }

}
