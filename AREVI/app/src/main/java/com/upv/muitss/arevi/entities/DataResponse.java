package com.upv.muitss.arevi.entities;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataResponse<T> {

    @SerializedName("data")
    @Expose
    public List<T> data;

    @NonNull
    @Override
    public String toString() {
        return '{' +
                "data = '" + data.toString() + '\'' +
                '}';
    }
}
