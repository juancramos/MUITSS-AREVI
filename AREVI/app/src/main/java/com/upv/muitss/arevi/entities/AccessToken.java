package com.upv.muitss.arevi.entities;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccessToken {

    @SerializedName("accessToken")
    @Expose
    public String accessToken;

    @NonNull
    @Override
    public String toString() {
        return '{' +
                ", accessToken='" + accessToken + '\'' +
                '}';
    }
}
