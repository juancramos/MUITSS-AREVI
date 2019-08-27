package com.upv.muitss.arevi.enums;

public enum AssessmentType {
    SUS("SUS"),  NASA("NASA");

    private String assessmentType;

    AssessmentType(String assessment) {
        this.assessmentType = assessment;
    }

    public String getAssessmentType() {
        return assessmentType;
    }
}
