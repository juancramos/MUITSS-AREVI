package com.upv.muitss.arevi.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PolyAsset {

    @SerializedName("key")
    @Expose
    public String key;
    @SerializedName("displayName")
    @Expose
    public String displayName;
    @SerializedName("authorName")
    @Expose
    public String authorName;
    @SerializedName("license")
    @Expose
    public String license;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("modelUrl")
    @Expose
    public String modelUrl;

    public PolyAsset(String key) {
        this.key = key;
    }
}
