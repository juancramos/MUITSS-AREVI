package com.upv.muitss.arevi.entities;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Round {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("score")
    @Expose
    public Work score;
    @SerializedName("completed")
    @Expose
    public String completed;


    @NonNull
    @Override
    public String toString() {
        return "profile = {" +
                "id = '" + id + '\'' +
                ", score = '" + score.toString() + '\'' +
                ", completed = '" + completed + '\'' +
                '}';
    }

    public String getCompleted() {
        return completed;
    }

    public void setCompleted(String pEnabled) {
        completed = pEnabled;
    }

    public Work getScore() {
        return score;
    }

    public void setScore(Work score) {
        this.score = score;
    }
}
