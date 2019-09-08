package com.upv.muitss.arevi.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.upv.muitss.arevi.R;
import com.upv.muitss.arevi.entities.Assessment;
import com.upv.muitss.arevi.entities.Content;
import com.upv.muitss.arevi.enums.AssessmentType;
import com.upv.muitss.arevi.helpers.AppState;
import com.upv.muitss.arevi.helpers.Utils;

import java.util.Locale;

public class AssessmentNASAFragmentView extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public AssessmentNASAFragmentView() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static AssessmentNASAFragmentView newInstance(int sectionNumber) {
        AssessmentNASAFragmentView fragment = new AssessmentNASAFragmentView();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        int section = getArguments() != null ? getArguments().getInt(ARG_SECTION_NUMBER) : -1;

        View rootView = null;

        if (section == 0) {
            rootView = inflater.inflate(R.layout.fragment_pager_assessment_nasa_1, container, false);

            TextView tittle = rootView.findViewById(R.id.section_label);
            tittle.setText(String.format(Locale.getDefault(), "%s %d / %d", tittle.getText(), 1, 2));

            TextView t1 = rootView.findViewById(R.id.nasa_1_text);
            RadioGroup rg1 = rootView.findViewById(R.id.nasa_1_radio_group);
            rg1.setOnCheckedChangeListener((group, checkedId) -> {
                RadioButton rb1 = rg1.findViewById(checkedId);
                Content content = AppState.getInstance().getAssessmentByType(AssessmentType.NASA).getContentById(rg1.getId());
                content.id = rg1.getId();
                content.question = t1.getText().toString();
                content.answer = rb1.getText().toString();
                AppState.getInstance().getAssessmentByType(AssessmentType.NASA).addContent(content);
            });

            TextView t2 = rootView.findViewById(R.id.nasa_2_text);
            RadioGroup rg2 = rootView.findViewById(R.id.nasa_2_radio_group);
            rg2.setOnCheckedChangeListener((group, checkedId) -> {
                RadioButton rb2 = rg2.findViewById(checkedId);
                Content content = AppState.getInstance().getAssessmentByType(AssessmentType.NASA).getContentById(rg2.getId());
                content.id = rg2.getId();
                content.question = t2.getText().toString();
                content.answer = rb2.getText().toString();
                AppState.getInstance().getAssessmentByType(AssessmentType.NASA).addContent(content);
            });

            TextView t3 = rootView.findViewById(R.id.nasa_3_text);
            RadioGroup rg3 = rootView.findViewById(R.id.nasa_3_radio_group);
            rg3.setOnCheckedChangeListener((group, checkedId) -> {
                RadioButton rb3 = rg3.findViewById(checkedId);
                Content content = AppState.getInstance().getAssessmentByType(AssessmentType.NASA).getContentById(rg3.getId());
                content.id = rg3.getId();
                content.question = t3.getText().toString();
                content.answer = rb3.getText().toString();
                AppState.getInstance().getAssessmentByType(AssessmentType.NASA).addContent(content);
            });

            TextView t4 = rootView.findViewById(R.id.nasa_4_text);
            RadioGroup rg4 = rootView.findViewById(R.id.nasa_4_radio_group);
            rg4.setOnCheckedChangeListener((group, checkedId) -> {
                RadioButton rb4 = rg4.findViewById(checkedId);
                Content content = AppState.getInstance().getAssessmentByType(AssessmentType.NASA).getContentById(rg4.getId());
                content.id = rg4.getId();
                content.question = t4.getText().toString();
                content.answer = rb4.getText().toString();
                AppState.getInstance().getAssessmentByType(AssessmentType.NASA).addContent(content);
            });

        }
        else if (section == 1) {
            rootView = inflater.inflate(R.layout.fragment_pager_assessment_nasa_2, container, false);

            TextView tittle = rootView.findViewById(R.id.section_label);
            tittle.setText(String.format(Locale.getDefault(), "%s %d / %d", tittle.getText(), 2, 2));

            TextView t6 = rootView.findViewById(R.id.nasa_6_text);
            RadioGroup rg6 = rootView.findViewById(R.id.nasa_6_radio_group);
            rg6.setOnCheckedChangeListener((group, checkedId) -> {
                RadioButton rb6 = rg6.findViewById(checkedId);
                Content content = AppState.getInstance().getAssessmentByType(AssessmentType.NASA).getContentById(rg6.getId());
                content.id = rg6.getId();
                content.question = t6.getText().toString();
                content.answer = rb6.getText().toString();
                AppState.getInstance().getAssessmentByType(AssessmentType.NASA).addContent(content);
            });

            TextView t7 = rootView.findViewById(R.id.nasa_7_text);
            RadioGroup rg7 = rootView.findViewById(R.id.nasa_7_radio_group);
            rg7.setOnCheckedChangeListener((group, checkedId) -> {
                RadioButton rb7 = rg7.findViewById(checkedId);
                Content content = AppState.getInstance().getAssessmentByType(AssessmentType.NASA).getContentById(rg7.getId());
                content.id = rg7.getId();
                content.question = t7.getText().toString();
                content.answer = rb7.getText().toString();
                AppState.getInstance().getAssessmentByType(AssessmentType.NASA).addContent(content);
            });
        }

        Assessment currentAssessment = AppState.getInstance().getAssessmentByType(AssessmentType.NASA);
        currentAssessment.name = AssessmentType.NASA.toString() + '-'
                + AppState.getInstance().getProfile().name + '-' + Utils.getLogIn().email;
        currentAssessment.type = AssessmentType.NASA;
        AppState.getInstance().addAssessment(currentAssessment);
        return rootView;
    }
}