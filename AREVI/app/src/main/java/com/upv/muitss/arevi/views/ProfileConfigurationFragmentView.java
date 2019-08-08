package com.upv.muitss.arevi.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.upv.muitss.arevi.R;
import com.upv.muitss.arevi.helpers.AppState;
import com.upv.muitss.arevi.helpers.Utils;

public class ProfileConfigurationFragmentView extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public ProfileConfigurationFragmentView() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ProfileConfigurationFragmentView newInstance(int sectionNumber) {
        ProfileConfigurationFragmentView fragment = new ProfileConfigurationFragmentView();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_pager_profile_config, container, false);
        AppState appState = AppState.getInstance();

        EditText mEmailTxt = rootView.findViewById(R.id.text_input_email);
        TextInputLayout mEmailLayout = rootView.findViewById(R.id.text_input_layout_email);
        mEmailTxt.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                appState.setprofileFormHasError(false);
                String textFromEditView = ((EditText)v).getText().toString();

                // Reset errors.
                mEmailLayout.setError(null);
                if (Utils.isNullOrEmpty(textFromEditView) || Utils.emailValidation(textFromEditView)) {
                    appState.setprofileFormHasError(true);
                    mEmailLayout.setError("Enter a valid email");
                }

            }
        });

        EditText passwordTxt = rootView.findViewById(R.id.text_input_password);
        TextInputLayout passwordLayout = rootView.findViewById(R.id.text_input_layout_password);
        passwordTxt.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                appState.setprofileFormHasError(false);
                String textFromEditView = ((EditText)v).getText().toString();
                // Reset errors.
                passwordLayout.setError(null);
                if (Utils.isNullOrEmpty(textFromEditView)) {
                    appState.setprofileFormHasError(true);
                    passwordLayout.setError("Can not be empty");
                }
            }
        });

        EditText fullNameTxt = rootView.findViewById(R.id.text_input_full_name);
        TextInputLayout fullNameLayout = rootView.findViewById(R.id.text_input_layout_full_name);
        fullNameTxt.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                appState.setprofileFormHasError(false);
                String textFromEditView = ((EditText)v).getText().toString();
                // Reset errors.
                fullNameLayout.setError(null);
                if (Utils.isNullOrEmpty(textFromEditView)) {
                    appState.setprofileFormHasError(true);
                    fullNameLayout.setError("Can not be empty");
                }
            }
        });

        EditText otherGenreNameTxt = rootView.findViewById(R.id.text_input_genre);
        TextInputLayout otherGenreLayout = rootView.findViewById(R.id.text_input_layout_genre);
        otherGenreNameTxt.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                appState.setprofileFormHasError(false);
                String textFromEditView = ((EditText)v).getText().toString();
                // Reset errors.
                otherGenreLayout.setError(null);
                if (otherGenreLayout.getVisibility() == View.VISIBLE && Utils.isNullOrEmpty(textFromEditView)) {
                    appState.setprofileFormHasError(true);
                    otherGenreLayout.setError("Can not be empty");
                }
            }
        });

        Spinner genreSpinner = rootView.findViewById(R.id.input_spinner_gender);
        genreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                otherGenreLayout.setVisibility(position == 2 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        EditText occupationTxt = rootView.findViewById(R.id.text_input_occupation);
        TextInputLayout occupationLayout = rootView.findViewById(R.id.text_input_layout_occupation);
        occupationTxt.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                appState.setprofileFormHasError(false);
                String textFromEditView = ((EditText)v).getText().toString();
                // Reset errors.
                occupationLayout.setError(null);
                if (Utils.isNullOrEmpty(textFromEditView)) {
                    appState.setprofileFormHasError(true);
                    occupationLayout.setError("Can not be empty");
                }
            }
        });

        Spinner spinner = rootView.findViewById(R.id.input_spinner_education);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(rootView.getContext(),
                R.array.input_spinner_education_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_wrap_text_adapter);
        spinner.setAdapter(adapter);

        return rootView;
    }
}