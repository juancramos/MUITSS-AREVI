package com.upv.muitss.arevi.entities;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("password")
    @Expose
    public String password;
    @SerializedName("enabled")
    @Expose
    private String enabled;
    private boolean enabledBoolean;
    public boolean fetchingData;


    @NonNull
    @Override
    public String toString() {
        return "user{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", enabled='" + enabled + '\'' +
                '}';
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String pEnabled) {
        enabled = pEnabled;
        enabledBoolean = Boolean.valueOf(pEnabled);
    }

    public boolean isValidState() {
        return email != null && !email.isEmpty() && password != null && !password.isEmpty();
    }
}


