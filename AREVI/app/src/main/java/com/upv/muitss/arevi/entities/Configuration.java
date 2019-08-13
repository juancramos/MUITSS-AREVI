package com.upv.muitss.arevi.entities;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Configuration {
    @SerializedName("selectedAppTheme")
    @Expose
    public int selectedAppTheme;
    @SerializedName("useGoogleCardboard")
    @Expose
    public String useGoogleCardboard;

    @NonNull
    @Override
    public String toString() {
        return "{" +
                "selectedAppTheme = '" + selectedAppTheme + '\'' +
                ", useGoogleCardboard = '" + useGoogleCardboard + '\'' +
                '}';
    }

    public boolean getUseGoogleCardboardBoolean(){
        return Boolean.getBoolean(useGoogleCardboard);
    }

    public String getUseGoogleCardboard(){
        return useGoogleCardboard;
    }

    public void setUseGoogleCardboard(boolean value){
        useGoogleCardboard = String.valueOf(value);
    }
}
