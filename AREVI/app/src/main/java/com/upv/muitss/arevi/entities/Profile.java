package com.upv.muitss.arevi.entities;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Profile {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("enabled")
    @Expose
    public String enabled;
    @SerializedName("userId")
    @Expose
    public String userId;
    @SerializedName("configuration")
    @Expose
    private Configuration configuration = new Configuration();

    @NonNull
    @Override
    public String toString() {
        return "profile = {" +
                "id = '" + id + '\'' +
                ", name = '" + name + '\'' +
                ", configuration = '" + configuration.toString() + '\'' +
                ", enabled = '" + enabled + '\'' +
                ", userId = '" + userId + '\'' +
                '}';
    }

    public boolean isLocal(){
        return TextUtils.isEmpty(id);
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String pEnabled) {
        enabled = pEnabled;
    }

    public Configuration getConfiguration() {
        return configuration;    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;    }

}
