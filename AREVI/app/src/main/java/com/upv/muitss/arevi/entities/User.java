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
    public boolean enabled;
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

    public boolean isValidState() {
        return email != null && !email.isEmpty() && password != null && !password.isEmpty();
    }
}
