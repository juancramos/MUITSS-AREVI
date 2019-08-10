package com.upv.muitss.arevi.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.upv.muitss.arevi.R;
import com.upv.muitss.arevi.entities.User;
import com.upv.muitss.arevi.entities.UserInfo;
import com.upv.muitss.arevi.helpers.AppState;
import com.upv.muitss.arevi.helpers.Utils;
import com.upv.muitss.arevi.logic.web.implementations.AREVIRepository;
import com.upv.muitss.arevi.logic.web.interfaces.ActivityMessage;

public class ProfileConfigurationFragmentView extends Fragment implements ActivityMessage {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private View rootView;
    private User user;
    private UserInfo userInfo;

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

        rootView =  inflater.inflate(R.layout.fragment_pager_profile_config, container, false);

        if (!AppState.getInstance().getUserInfo().isValidState()) {
            Utils.popProgressDialog(getActivity(), "Loading...");
            AREVIRepository.getInstance().getApiUserInfo(Utils.getUserId(), this);
        }
        else {
            loadData();
        }
        return rootView;
    }

    private void loadData(){
        user = AppState.getInstance().getUser() == null ? new User() : AppState.getInstance().getUser();
        userInfo = AppState.getInstance().getUserInfo() == null ? new UserInfo() : AppState.getInstance().getUserInfo();

        EditText mEmailTxt = rootView.findViewById(R.id.text_input_email);
        mEmailTxt.setText(user.email);
        mEmailTxt.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                user.email = Utils.validateInput(mEmailTxt);
            }
        });

        EditText passwordTxt = rootView.findViewById(R.id.text_input_password);
        passwordTxt.setText(user.email);
        passwordTxt.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                user.password = Utils.validateInput(passwordTxt);
            }
        });

        EditText fullNameTxt = rootView.findViewById(R.id.text_input_full_name);
        fullNameTxt.setText(userInfo.fullName);
        fullNameTxt.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                userInfo.fullName = Utils.validateInput(fullNameTxt);
            }
        });

        EditText otherGenderNameTxt = rootView.findViewById(R.id.text_input_gender);
        TextInputLayout otherGenderLayout = rootView.findViewById(R.id.text_input_layout_gender);
        if(!TextUtils.isEmpty(userInfo.genderOther)){
            otherGenderNameTxt.setText(userInfo.genderOther);
        }
        else {
            otherGenderLayout.setVisibility(View.GONE);
        }
        otherGenderNameTxt.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                userInfo.genderOther = Utils.validateInput(otherGenderNameTxt);
            }
        });

        EditText occupationTxt = rootView.findViewById(R.id.text_input_occupation);
        occupationTxt.setText(userInfo.occupation);
        occupationTxt.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                userInfo.occupation = Utils.validateInput(occupationTxt);
            }
        });

        EditText visualIllnessTxt = rootView.findViewById(R.id.text_input_visual_illness);
        visualIllnessTxt.setText(userInfo.visualIllness);
        visualIllnessTxt.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                userInfo.visualIllness =  Utils.validateInput(visualIllnessTxt);
            }
        });

        Spinner genderSpinner = rootView.findViewById(R.id.input_spinner_gender);
        genderSpinner.setSelection(getIndex(genderSpinner, userInfo.gender));
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                otherGenderLayout.setVisibility(position == 2 ? View.VISIBLE : View.GONE);
                userInfo.gender = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Spinner ageSpinner = rootView.findViewById(R.id.input_spinner_age);
        ageSpinner.setSelection(getIndex(ageSpinner, userInfo.age));
        ageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userInfo.age = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Spinner educationSpinner = rootView.findViewById(R.id.input_spinner_education);
        educationSpinner.setSelection(getIndex(educationSpinner, userInfo.education));
        educationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userInfo.education = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(rootView.getContext(),
                R.array.input_spinner_education_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_wrap_text_adapter);
        educationSpinner.setAdapter(adapter);
    }

    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }


    @Override
    public <T> void onResponse(T response) {
        if (response instanceof String && !(TextUtils.isEmpty((String) response))){
            loadData();
        }
    }
}