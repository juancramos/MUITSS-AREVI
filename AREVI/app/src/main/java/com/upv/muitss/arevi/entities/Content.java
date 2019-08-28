package com.upv.muitss.arevi.entities;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Content {
    public int id;
    @SerializedName("question")
    @Expose
    public String question;
    @SerializedName("answer")
    @Expose
    public String answer;

    @NonNull
    @Override
    public String toString() {
        return "{" +
                "question = '" + question + '\'' +
                ", answer = '" + answer + '\'' +
                '}';
    }

}
