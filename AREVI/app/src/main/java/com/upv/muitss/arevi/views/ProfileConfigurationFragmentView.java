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
import android.widget.TextView;

import com.upv.muitss.arevi.R;
import com.upv.muitss.arevi.helpers.AppState;
import com.upv.muitss.arevi.helpers.Utils;
import com.upv.muitss.arevi.logic.web.implementations.AREVIRepository;
import com.upv.muitss.arevi.logic.web.interfaces.ActivityMessage;

import java.util.Arrays;
import java.util.List;

public class ProfileConfigurationFragmentView extends Fragment implements ActivityMessage {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final List<String> genders = Arrays.asList("Male", "Female","Other (specify)");
    private static final List<String> ages = Arrays.asList("17 or younger", "18-20", "21-29", "30-39", "40-49", "50-59", "60 or older");
    private static final List<String> educations = Arrays.asList("Less than high school degree", "High school degree or equivalent", "Some college but no degree", "Associate degree", "Bachelor degree", "Graduate degree");

    private View rootView;

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

        boolean userInfo = AppState.getInstance().getUserInfo().isValidState();
        boolean userLogin = Utils.getLogIn().isValidState();

        if (userLogin) {
            View loginView = rootView.findViewById(R.id.fragment_pager_profile_log_in);
            loginView.setVisibility(View.GONE);
        }

        if (userLogin && !userInfo) {
            Utils.popProgressDialog(getActivity(), Utils.getResourceString(R.string.dialog_loading_text));
            AREVIRepository.getInstance().getApiUserInfo(Utils.getUserId(), this);
        }
        else {
            loadData();
        }
        return rootView;
    }

    private void loadData(){
        EditText mEmailTxt = rootView.findViewById(R.id.text_input_email);
        mEmailTxt.setText(Utils.getLogIn().email);
        mEmailTxt.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                AppState.getInstance().getUser().email = Utils.validateInput(mEmailTxt);
            }
        });

        EditText passwordTxt = rootView.findViewById(R.id.text_input_password);
        passwordTxt.setText(Utils.getLogIn().password);
        passwordTxt.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                AppState.getInstance().getUser().password = Utils.validateInput(passwordTxt);
            }
        });

        EditText fullNameTxt = rootView.findViewById(R.id.text_input_full_name);
        fullNameTxt.setText(AppState.getInstance().getUserInfo().fullName);
        fullNameTxt.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                AppState.getInstance().getUserInfo().fullName = Utils.validateInput(fullNameTxt);
            }
        });

        EditText occupationTxt = rootView.findViewById(R.id.text_input_occupation);
        occupationTxt.setText(AppState.getInstance().getUserInfo().occupation);
        occupationTxt.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                AppState.getInstance().getUserInfo().occupation = Utils.validateInput(occupationTxt);
            }
        });

        EditText visualIllnessTxt = rootView.findViewById(R.id.text_input_visual_illness);
        visualIllnessTxt.setText(AppState.getInstance().getUserInfo().visualIllness);
        visualIllnessTxt.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                AppState.getInstance().getUserInfo().visualIllness =  Utils.validateInput(visualIllnessTxt);
            }
        });

        EditText otherGenderNameTxt = rootView.findViewById(R.id.text_input_gender);
        TextInputLayout otherGenderLayout = rootView.findViewById(R.id.text_input_layout_gender);
        TextView otherGenderText = rootView.findViewById(R.id.text_view_layout_gender);

        Spinner genderSpinner = rootView.findViewById(R.id.input_spinner_gender);
        genderSpinner.setSelection(getIndex(genderSpinner, AppState.getInstance().getUserInfo().gender));
        AppState.getInstance().getUserInfo().gender = genders.get(genderSpinner.getFirstVisiblePosition());
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                otherGenderLayout.setVisibility(position == 2 ? View.VISIBLE : View.GONE);
                otherGenderText.setVisibility(position == 2 ? View.VISIBLE : View.GONE);
                AppState.getInstance().getUserInfo().gender = genders.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        if(!TextUtils.isEmpty(AppState.getInstance().getUserInfo().genderOther)){
            otherGenderNameTxt.setText(AppState.getInstance().getUserInfo().genderOther);
        }
        else {
            otherGenderLayout.setVisibility(View.GONE);
            otherGenderText.setVisibility(View.GONE);
        }
        otherGenderNameTxt.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                AppState.getInstance().getUserInfo().genderOther = Utils.validateInput(otherGenderNameTxt);
            }
        });

        Spinner ageSpinner = rootView.findViewById(R.id.input_spinner_age);
        ageSpinner.setSelection(getIndex(ageSpinner, AppState.getInstance().getUserInfo().age));
        AppState.getInstance().getUserInfo().age = ages.get(ageSpinner.getFirstVisiblePosition());
        ageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AppState.getInstance().getUserInfo().age = ages.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Spinner educationSpinner = rootView.findViewById(R.id.input_spinner_education);
        educationSpinner.setSelection(getIndex(educationSpinner, AppState.getInstance().getUserInfo().education));
        AppState.getInstance().getUserInfo().education = educations.get(educationSpinner.getFirstVisiblePosition());
        educationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AppState.getInstance().getUserInfo().education = educations.get(position);
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
        else{
            loadData();
        }
    }
}