package com.upv.muitss.arevi.entities;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class Work {
    @SerializedName("ids")
    @Expose
    public String[] ids = new String[]{};

    @NonNull
    @Override
    public String toString() {
        return "{" +
                "ids = '" + Arrays.toString(ids) + '\'' +
                '}';
    }
}
