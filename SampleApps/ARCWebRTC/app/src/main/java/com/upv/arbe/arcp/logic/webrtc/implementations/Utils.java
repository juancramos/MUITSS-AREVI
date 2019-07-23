package com.upv.arbe.arcp.logic.webrtc.implementations;

import com.upv.arbe.arcp.logic.webrtc.interfaces.TurnServer;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class Utils {

    static Utils instance;
    private static final String API_ENDPOINT = "https://global.xirsys.net";

    private Retrofit retrofitInstance;

    static Utils getInstance() {
        if (instance == null) {
            instance = new Utils();
        }
        return instance;
    }

    TurnServer getRetrofitInstance() {
        if (retrofitInstance == null) {
            retrofitInstance = new Retrofit.Builder()
                    .baseUrl(API_ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitInstance.create(TurnServer.class);
    }
}
