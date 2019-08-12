package com.upv.muitss.arevi.entities;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Profile {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("profileName")
    @Expose
    public String profileName;
    @SerializedName("configuration")
    @Expose
    public Configuration configuration = new Configuration();
    @SerializedName("enabled")
    @Expose
    public String enabled;
    @SerializedName("userId")
    @Expose
    public String userId;


    private boolean enabledBoolean;


    @NonNull
    @Override
    public String toString() {
        return "profile = {" +
                "id = '" + id + '\'' +
                ", profileName = '" + profileName + '\'' +
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
        enabledBoolean = Boolean.valueOf(pEnabled);
    }

    public Configuration getConfiguration() {
        return configuration;    }

    public void setfiguration(Configuration configuration) {
        this.configuration = configuration;    }

}
