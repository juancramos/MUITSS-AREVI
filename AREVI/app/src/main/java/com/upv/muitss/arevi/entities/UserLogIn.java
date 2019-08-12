package com.upv.muitss.arevi.entities;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserLogIn {

    @SerializedName("strategy")
    @Expose
    public String strategy = "local";
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("password")
    @Expose
    public String password;


    public UserLogIn(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @NonNull
    @Override
    public String toString() {
        return '{' +
                ", strategy='" + strategy + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public boolean isValidState() {
        return email != null && !email.isEmpty() && password != null && !password.isEmpty();
    }
}
