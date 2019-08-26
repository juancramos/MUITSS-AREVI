package com.upv.muitss.arevi.entities;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Work {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("scaleV")
    @Expose
    public float scaleV;
    @SerializedName("scaleV1")
    @Expose
    public float scaleV1;
    @SerializedName("scaleV2")
    @Expose
    public float scaleV2;
    @SerializedName("distance")
    @Expose
    public float distance;
    @SerializedName("seconds")
    @Expose
    public int seconds;
    @SerializedName("minutes")
    @Expose
    public int minutes;
    @SerializedName("milliSeconds")
    @Expose
    public int milliSeconds;

    @NonNull
    @Override
    public String toString() {
        return "{" +
                "id = '" + id + '\'' +
                ", scaleV = '" + scaleV + '\'' +
                ", scaleV1 = '" + scaleV1 + '\'' +
                ", scaleV2 = '" + scaleV2 + '\'' +
                ", distance = '" + distance + '\'' +
                '}';
    }

    public void reset() {
        id = null;
        scaleV = -1;
        scaleV1 = -1;
        scaleV2 = -1;
    }
}
