package com.upv.arbe.arcp.logic.webrtc.implementations;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TurnServerPojo {

    @SerializedName("s")
    @Expose
    public Integer s;
    @SerializedName("p")
    @Expose
    public String p;
    @SerializedName("e")
    @Expose
    public Object e;
    @SerializedName("v")
    @Expose
    IceServerList iceServerList;

    class IceServerList {

        @SerializedName("iceServers")
        @Expose
        List<IceServer> iceServers = null;

    }
}
