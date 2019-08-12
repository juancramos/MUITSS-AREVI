package com.upv.muitss.arevi.entities;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserInfo {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("fullName")
    @Expose
    public String fullName;
    @SerializedName("gender")
    @Expose
    public String gender;
    @SerializedName("genderOther")
    @Expose
    public String genderOther;
    @SerializedName("age")
    @Expose
    public String age;
    @SerializedName("visualIllness")
    @Expose
    public String visualIllness;
    @SerializedName("occupation")
    @Expose
    public String occupation;
    @SerializedName("education")
    @Expose
    public String education;
    @SerializedName("userId")
    @Expose
    public String userId;

    @NonNull
    @Override
    public String toString() {
        return "user-info = {" +
                "id = '" + id + '\'' +
                ", fullName = '" + fullName + '\'' +
                ", gender = '" + gender + '\'' +
                ", genderOther = '" + genderOther + '\'' +
                ", age = '" + age + '\'' +
                ", visualIllness = '" + visualIllness + '\'' +
                ", occupation = '" + occupation + '\'' +
                ", education = '" + education + '\'' +
                ", userId = '" + userId + '\'' +
                '}';
    }

    public boolean isValidState() {
        return fullName != null && !fullName.isEmpty()
                && gender != null && !gender.isEmpty()
                && age != null && !age.isEmpty()
                && visualIllness != null && !visualIllness.isEmpty()
                && occupation != null && !occupation.isEmpty()
                && education != null && !education.isEmpty();
    }
}
