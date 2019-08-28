package com.upv.muitss.arevi.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.upv.muitss.arevi.R;
import com.upv.muitss.arevi.entities.Assessment;
import com.upv.muitss.arevi.entities.Content;
import com.upv.muitss.arevi.enums.AssessmentType;
import com.upv.muitss.arevi.helpers.AppState;
import com.upv.muitss.arevi.helpers.Constants;
import com.upv.muitss.arevi.helpers.Utils;

public class AssessmentSUSFragmentView extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public AssessmentSUSFragmentView() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static AssessmentSUSFragmentView newInstance(int sectionNumber) {
        AssessmentSUSFragmentView fragment = new AssessmentSUSFragmentView();
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
            rootView = inflater.inflate(R.layout.fragment_pager_assessment_sus_1, container, false);

            TextView t1 = rootView.findViewById(R.id.sus_1_text);
            RadioGroup rg1 = rootView.findViewById(R.id.sus_1_radio_group);
            rg1.setOnCheckedChangeListener((group, checkedId) -> {
                RadioButton rb1 = rg1.findViewById(checkedId);
                Content content = AppState.getInstance().getAssessmentByType(AssessmentType.SUS).getContentById(rg1.getId());
                content.id = rg1.getId();
                content.question = t1.getText().toString();
                content.answer = rb1.getText().toString();
                AppState.getInstance().getAssessmentByType(AssessmentType.SUS).addContent(content);
            });

            TextView t2 = rootView.findViewById(R.id.sus_2_text);
            RadioGroup rg2 = rootView.findViewById(R.id.sus_2_radio_group);
            rg2.setOnCheckedChangeListener((group, checkedId) -> {
                RadioButton rb2 = rg2.findViewById(checkedId);
                Content content = AppState.getInstance().getAssessmentByType(AssessmentType.SUS).getContentById(rg2.getId());
                content.id = rg2.getId();
                content.question = t2.getText().toString();
                content.answer = rb2.getText().toString();
                AppState.getInstance().getAssessmentByType(AssessmentType.SUS).addContent(content);
            });

            TextView t3 = rootView.findViewById(R.id.sus_3_text);
            RadioGroup rg3 = rootView.findViewById(R.id.sus_3_radio_group);
            rg3.setOnCheckedChangeListener((group, checkedId) -> {
                RadioButton rb3 = rg3.findViewById(checkedId);
                Content content = AppState.getInstance().getAssessmentByType(AssessmentType.SUS).getContentById(rg3.getId());
                content.id = rg3.getId();
                content.question = t3.getText().toString();
                content.answer = rb3.getText().toString();
                AppState.getInstance().getAssessmentByType(AssessmentType.SUS).addContent(content);
            });

            TextView t4 = rootView.findViewById(R.id.sus_4_text);
            RadioGroup rg4 = rootView.findViewById(R.id.sus_4_radio_group);
            rg4.setOnCheckedChangeListener((group, checkedId) -> {
                RadioButton rb4 = rg4.findViewById(checkedId);
                Content content = AppState.getInstance().getAssessmentByType(AssessmentType.SUS).getContentById(rg4.getId());
                content.id = rg4.getId();
                content.question = t4.getText().toString();
                content.answer = rb4.getText().toString();
                AppState.getInstance().getAssessmentByType(AssessmentType.SUS).addContent(content);
            });

            TextView t5 = rootView.findViewById(R.id.sus_5_text);
            RadioGroup rg5 = rootView.findViewById(R.id.sus_5_radio_group);
            rg5.setOnCheckedChangeListener((group, checkedId) -> {
                RadioButton rb5 = rg5.findViewById(checkedId);
                Content content = AppState.getInstance().getAssessmentByType(AssessmentType.SUS).getContentById(rg5.getId());
                content.id = rg5.getId();
                content.question = t5.getText().toString();
                content.answer = rb5.getText().toString();
                AppState.getInstance().getAssessmentByType(AssessmentType.SUS).addContent(content);
            });

        }
        else if (section == 1) {
            rootView = inflater.inflate(R.layout.fragment_pager_assessment_sus_2, container, false);

            TextView t6 = rootView.findViewById(R.id.sus_6_text);
            RadioGroup rg6 = rootView.findViewById(R.id.sus_6_radio_group);
            rg6.setOnCheckedChangeListener((group, checkedId) -> {
                RadioButton rb6 = rg6.findViewById(checkedId);
                Content content = AppState.getInstance().getAssessmentByType(AssessmentType.SUS).getContentById(rg6.getId());
                content.id = rg6.getId();
                content.question = t6.getText().toString();
                content.answer = rb6.getText().toString();
                AppState.getInstance().getAssessmentByType(AssessmentType.SUS).addContent(content);
            });

            TextView t7 = rootView.findViewById(R.id.sus_7_text);
            RadioGroup rg7 = rootView.findViewById(R.id.sus_7_radio_group);
            rg7.setOnCheckedChangeListener((group, checkedId) -> {
                RadioButton rb7 = rg7.findViewById(checkedId);
                Content content = AppState.getInstance().getAssessmentByType(AssessmentType.SUS).getContentById(rg7.getId());
                content.id = rg7.getId();
                content.question = t7.getText().toString();
                content.answer = rb7.getText().toString();
                AppState.getInstance().getAssessmentByType(AssessmentType.SUS).addContent(content);
            });

            TextView t8 = rootView.findViewById(R.id.sus_8_text);
            RadioGroup rg8 = rootView.findViewById(R.id.sus_8_radio_group);
            rg8.setOnCheckedChangeListener((group, checkedId) -> {
                RadioButton rb8 = rg8.findViewById(checkedId);
                Content content = AppState.getInstance().getAssessmentByType(AssessmentType.SUS).getContentById(rg8.getId());
                content.id = rg8.getId();
                content.question = t8.getText().toString();
                content.answer = rb8.getText().toString();
                AppState.getInstance().getAssessmentByType(AssessmentType.SUS).addContent(content);
            });

            TextView t9 = rootView.findViewById(R.id.sus_9_text);
            RadioGroup rg9 = rootView.findViewById(R.id.sus_9_radio_group);
            rg9.setOnCheckedChangeListener((group, checkedId) -> {
                RadioButton rb9 = rg9.findViewById(checkedId);
                Content content = AppState.getInstance().getAssessmentByType(AssessmentType.SUS).getContentById(rg9.getId());
                content.id = rg9.getId();
                content.question = t9.getText().toString();
                content.answer = rb9.getText().toString();
                AppState.getInstance().getAssessmentByType(AssessmentType.SUS).addContent(content);
            });

            TextView t10 = rootView.findViewById(R.id.sus_10_text);
            RadioGroup rg10 = rootView.findViewById(R.id.sus_10_radio_group);
            rg10.setOnCheckedChangeListener((group, checkedId) -> {
                RadioButton rb10 = rg10.findViewById(checkedId);
                Content content = AppState.getInstance().getAssessmentByType(AssessmentType.SUS).getContentById(rg10.getId());
                content.id = rg10.getId();
                content.question = t10.getText().toString();
                content.answer = rb10.getText().toString();
                AppState.getInstance().getAssessmentByType(AssessmentType.SUS).addContent(content);
            });

            TextView t11 = rootView.findViewById(R.id.sus_11_text);
            EditText freeQuestionTxt = rootView.findViewById(R.id.system_usability_scale_free_answer_text_input);
            freeQuestionTxt.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus) {
                    Content content = AppState.getInstance().getAssessmentByType(AssessmentType.SUS).getContentById(t11.getId());
                    content.id = t11.getId();
                    content.question = t11.getText().toString();
                    content.answer = Utils.validateInput(freeQuestionTxt);
                    AppState.getInstance().getAssessmentByType(AssessmentType.SUS).addContent(content);
                }
            });
        }

        Assessment currentAssessment = AppState.getInstance().getAssessmentByType(AssessmentType.SUS);
        currentAssessment.type = AssessmentType.SUS;
        AppState.getInstance().addAssessment(currentAssessment);
        return rootView;
    }
}