package com.upv.muitss.arevi.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.upv.muitss.arevi.R;
import com.upv.muitss.arevi.helpers.Utils;

public class AppConfigurationFragmentView extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public AppConfigurationFragmentView() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static AppConfigurationFragmentView newInstance(int sectionNumber) {
        AppConfigurationFragmentView fragment = new AppConfigurationFragmentView();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pager_app_config, container, false);

        boolean isDark = Utils.isDarkTheme();
        int sizeTheme = Utils.getSavedThemeStyle();

        if (isDark){
            ((RadioButton)rootView.findViewById(R.id.APP_THEME_DARK)).setChecked(true);
        }
        else {
            ((RadioButton)rootView.findViewById(R.id.APP_THEME)).setChecked(true);
        }
        switch (sizeTheme){
            case R.style.AppTheme:
            case R.style.AppThemeDark:
                ((RadioButton)rootView.findViewById(R.id.APP_THEME_NORMAL_FONT_SIZE)).setChecked(true);
                break;
            case R.style.FontSizeMedium:
            case R.style.FontSizeMediumDark:
                ((RadioButton)rootView.findViewById(R.id.APP_THEME_MEDIUM_FONT_SIZE)).setChecked(true);
                break;
            case R.style.FontSizeLarge:
            case R.style.FontSizeLargeDark:
                ((RadioButton)rootView.findViewById(R.id.APP_THEME_LARGE_FONT_SIZE)).setChecked(true);
                break;
        }
        return rootView;
    }
}