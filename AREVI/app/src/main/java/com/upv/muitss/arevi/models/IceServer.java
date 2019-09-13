package com.upv.muitss.arevi.models;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IceServer {

    @SerializedName("url")
    @Expose
    public String url;
    @SerializedName("username")
    @Expose
    public String username;
    @SerializedName("credential")
    @Expose
    public String credential;

    @NonNull
    @Override
    public String toString() {
        return "IceServer{" +
                "url='" + url + '\'' +
                ", username='" + username + '\'' +
                ", credential='" + credential + '\'' +
                '}';
    }
}