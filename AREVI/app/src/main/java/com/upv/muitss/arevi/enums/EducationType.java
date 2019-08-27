package com.upv.muitss.arevi.enums;

import java.util.Arrays;
import java.util.List;

public enum EducationType {
    LTHSD("Less than high school degree"),  HSDE("High school degree or equivalent"), SCD("Some college but no degree"),
    AD("Associate degree"), BD("Bachelor degree"), GD("Graduate degree");

    private static final List<String> educations = Arrays.asList(LTHSD.getEducationType(), HSDE.getEducationType(),
            SCD.getEducationType(), AD.getEducationType(), BD.getEducationType(), GD.getEducationType());
    private String educationType;

    EducationType(String education) {
        this.educationType = education;
    }

    public String getEducationType() {
        return educationType;
    }

    public static String getEducationType(int position){
        return educations.get(position);
    }

    public static int getEducationType(String education){
        return educations.indexOf(education);
    }
}
