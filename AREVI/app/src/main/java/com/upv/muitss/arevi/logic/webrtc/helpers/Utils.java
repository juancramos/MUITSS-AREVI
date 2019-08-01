package com.upv.muitss.arevi.logic.webrtc.helpers;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.upv.muitss.arevi.R;
import com.upv.muitss.arevi.logic.webrtc.models.IceServer;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Utils {

    private static Utils instance;

    public static Utils getInstance() {
        if (instance == null) {
            instance = new Utils();
        }
        return instance;
    }

    public List<IceServer> getIceServerData(Context context) {
        String jsonString = getAssetsJSON(context);
        Gson gson = new Gson();
        Type listType = new TypeToken<List<IceServer>>(){}.getType();
        return gson.fromJson(jsonString, listType);
    }

    private String getAssetsJSON(Context context) {
        String json = null;
        try {
            InputStream inputStream = context.getAssets().open(context.getString(R.string.ice_candidates_file_name));
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            if (inputStream.read(buffer) != -1)
                json = new String(buffer, StandardCharsets.UTF_8);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return json;
    }
}

