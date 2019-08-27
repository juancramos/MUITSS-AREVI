package com.upv.muitss.arevi.enums;

import java.util.Arrays;
import java.util.List;

public enum GenderType {
    Male("Male"),  Female("Female"), Other("Other (specify)");

    private static final List<String> genders = Arrays.asList(Male.getGenderType(), Female.getGenderType(), Other.getGenderType());
    private String genderType;

    GenderType(String gender) {
        this.genderType = gender;
    }

    public String getGenderType() {
        return genderType;
    }

    public static String getGenderType(int position){
        return genders.get(position);
    }

    public static int getGenderType(String gender){
        return genders.indexOf(gender);
    }
}
