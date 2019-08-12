package com.upv.muitss.arevi.entities;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Configuration {
    @SerializedName("selectedAppTheme")
    @Expose
    public int selectedAppTheme;

    @NonNull
    @Override
    public String toString() {
        return "{" +
                "selectedAppTheme = '" + selectedAppTheme + '\'' +
                '}';
    }
}
