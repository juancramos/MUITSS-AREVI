package com.upv.muitss.arevi.enums;

import java.util.Arrays;
import java.util.List;

public enum AgeType {
    A_17("17 or younger"),  A_18("18-20"), A_21("21-29"),
    A_30("30-39"), A_40("40-49"), A_50("50-59"), A_60("60 or older");

    private static final List<String> ages = Arrays.asList(A_17.getAgeType(), A_18.getAgeType(),
            A_21.getAgeType(), A_30.getAgeType(), A_40.getAgeType(), A_50.getAgeType(), A_60.getAgeType());
    private String ageType;

    AgeType(String age) {
        this.ageType = age;
    }

    public String getAgeType() {
        return ageType;
    }

    public static String getAgeType(int position){
        return ages.get(position);
    }

    public static int getAgeType(String age){
        return ages.indexOf(age);
    }
}
