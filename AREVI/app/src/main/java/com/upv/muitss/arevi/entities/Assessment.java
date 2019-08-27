package com.upv.muitss.arevi.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.upv.muitss.arevi.enums.AssessmentType;

import java.util.List;

public class Assessment {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("type")
    @Expose
    private AssessmentType type;
    @SerializedName("content")
    @Expose
    private List<Content> content;
    @SerializedName("completed")
    @Expose
    private String completed;
}
