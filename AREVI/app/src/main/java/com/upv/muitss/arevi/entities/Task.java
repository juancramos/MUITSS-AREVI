package com.upv.muitss.arevi.entities;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Task {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("work")
    @Expose
    public List<Work> work = new ArrayList<>();
    @SerializedName("enabled")
    @Expose
    public String enabled;

    @NonNull
    @Override
    public String toString() {
        return "task = {" +
                "id = '" + id + '\'' +
                ", work = '" + work.toString() + '\'' +
                ", enabled = '" + enabled + '\'' +
                '}';
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String pEnabled) {
        enabled = pEnabled;
    }

}
