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
import android.widget.EditText;

import com.upv.muitss.arevi.R;
import com.upv.muitss.arevi.helpers.Utils;

public class ProfileConfigurationFragmentView extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private TextInputLayout mEmailLayout;

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

        EditText mEmailtxt = rootView.findViewById(R.id.text_input_email);
        mEmailLayout = rootView.findViewById(R.id.text_input_layout_email);


        mEmailtxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String textFromEditView = s.toString();
                validate(textFromEditView);
            }
        });
        return rootView;
    }


    private boolean validate(String email) {

        // Reset errors.
        mEmailLayout.setError(null);
        if (!Utils.emailValidation(email)) {
            mEmailLayout.setError("Enter a valid email");
            return false;
        }

        return true;
    }
}