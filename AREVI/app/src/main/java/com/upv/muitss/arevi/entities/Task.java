package com.upv.muitss.arevi.entities;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Task {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("work")
    @Expose
    public Work work;
    @SerializedName("enabled")
    @Expose
    public String enabled;

    @NonNull
    @Override
    public String toString() {
        return "profile = {" +
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

    public Work getWork() {
        return work;
    }

    public void setWork(Work work) {
        this.work = work;
    }

}
