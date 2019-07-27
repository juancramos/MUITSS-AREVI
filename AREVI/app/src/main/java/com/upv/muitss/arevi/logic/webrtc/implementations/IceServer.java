package com.upv.muitss.arevi.logic.webrtc.implementations;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IceServer {

    @SerializedName("url")
    @Expose
    String url;
    @SerializedName("username")
    @Expose
    String username;
    @SerializedName("credential")
    @Expose
    String credential;

    @Override
    public String toString() {
        return "IceServer{" +
                "url='" + url + '\'' +
                ", username='" + username + '\'' +
                ", credential='" + credential + '\'' +
                '}';
    }
}

