package com.upv.muitss.arevi.entities;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Configuration {
    @SerializedName("selectedAppTheme")
    @Expose
    private int selectedAppTheme;
    @SerializedName("useGoogleCardboard")
    @Expose
    private String useGoogleCardboard;

    @NonNull
    @Override
    public String toString() {
        return "{" +
                "selectedAppTheme = '" + selectedAppTheme + '\'' +
                ", useGoogleCardboard = '" + useGoogleCardboard + '\'' +
                '}';
    }

    public boolean getUseGoogleCardboardBoolean(){
        return Boolean.parseBoolean(getUseGoogleCardboard());
    }

    public int getSelectedAppTheme(){
        return selectedAppTheme;
    }

    public void setSelectedAppTheme(int value){
        selectedAppTheme = value;
    }

    public String getUseGoogleCardboard(){
        return useGoogleCardboard;
    }

    public void setUseGoogleCardboard(boolean value){
        useGoogleCardboard = String.valueOf(value);
    }
}
