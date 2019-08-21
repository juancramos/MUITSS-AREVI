package com.upv.muitss.arevi.entities;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Round {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("score")
    @Expose
    public List<Work> score = new ArrayList<>();
    @SerializedName("completed")
    @Expose
    public String completed;
    @SerializedName("userId")
    @Expose
    public String userId;
    @SerializedName("taskId")
    @Expose
    public String taskId;
    @SerializedName("profileId")
    @Expose
    public String profileId;


    @NonNull
    @Override
    public String toString() {
        return "round = {" +
                "id = '" + id + '\'' +
                ", score = '" + score.toString() + '\'' +
                ", completed = '" + completed + '\'' +
                ", userId = '" + userId + '\'' +
                ", taskId = '" + taskId + '\'' +
                ", profileId = '" + profileId + '\'' +
                '}';
    }

    public boolean isLocal(){
        return TextUtils.isEmpty(id);
    }

    public String getCompleted() {
        return completed;
    }

    public void setCompleted(String pEnabled) {
        completed = pEnabled;
    }
}
