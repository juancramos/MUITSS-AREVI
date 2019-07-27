package com.upv.muitss.arevi.logic.webrtc.implementations;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;


class Utils {

    private static Utils instance;

    static Utils getInstance() {
        if (instance == null) {
            instance = new Utils();
        }
        return instance;
    }

    List<IceServer> getIceServerData(Context context) {
        String jsonString = getAssetsJSON(context,"ice_servers.json");
        Gson gson = new Gson();
        Type listType = new TypeToken<List<IceServer>>(){}.getType();
        return gson.fromJson(jsonString, listType);
    }

    private String getAssetsJSON(Context context, String fileName) {
        String json = null;
        try {
            InputStream inputStream = context.getAssets().open(fileName);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return json;
    }
}

