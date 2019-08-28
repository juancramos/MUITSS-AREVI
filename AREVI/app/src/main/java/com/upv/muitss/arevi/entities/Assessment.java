package com.upv.muitss.arevi.entities;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.upv.muitss.arevi.enums.AssessmentType;

import java.util.ArrayList;
import java.util.List;

public class Assessment {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("type")
    @Expose
    public AssessmentType type;
    @SerializedName("content")
    @Expose
    public List<Content> content  = new ArrayList<>();
    @SerializedName("completed")
    @Expose
    public String completed;
    @SerializedName("roundId")
    @Expose
    public String roundId;
    @SerializedName("taskId")
    @Expose
    public String taskId;
    @SerializedName("profileId")
    @Expose
    public String profileId;
    @SerializedName("userId")
    @Expose
    public String userId;

    @NonNull
    @Override
    public String toString() {
        return "assessment = {" +
                "id = '" + id + '\'' +
                ", name = '" + name + '\'' +
                ", type = '" + type + '\'' +
                ", content = '" + content.toString() + '\'' +
                ", completed = '" + completed + '\'' +
                ", roundId = '" + roundId + '\'' +
                ", taskId = '" + taskId + '\'' +
                ", profileId = '" + profileId + '\'' +
                ", userId = '" + userId + '\'' +
                '}';
    }

    public boolean isLocal(){
        return TextUtils.isEmpty(id);
    }

    public void addContent(Content pContent) {
        Content content = getContentById(pContent.id);
        if (content.id > 0){
            content.id = pContent.id;
            content.answer = pContent.answer;
            content.question = pContent.question;
        }
        else this.content.add(pContent);
    }

    public Content getContentById(int pId) {
        Content content = this.content.stream().filter(c -> c.id == pId).findFirst().orElse(null);
        if (content == null) content = new Content();
        return content;
    }
}
