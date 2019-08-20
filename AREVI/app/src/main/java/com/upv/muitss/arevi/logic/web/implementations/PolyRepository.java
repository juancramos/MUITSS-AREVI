package com.upv.muitss.arevi.logic.web.implementations;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.upv.muitss.arevi.R;
import com.upv.muitss.arevi.entities.PolyAsset;
import com.upv.muitss.arevi.helpers.AppState;
import com.upv.muitss.arevi.helpers.Utils;
import com.upv.muitss.arevi.logic.web.interceptors.Instance;
import com.upv.muitss.arevi.logic.web.interfaces.PolyApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PolyRepository {

    private final String TAG = this.getClass().getCanonicalName();
    private static final String API_KEY = Utils.getResourceString(R.string.poly_server_api_key);
    private static PolyRepository polyRepository = null;
    private static PolyApiService apiService = null;

    public static PolyRepository getInstance(){
        if (polyRepository == null){
            polyRepository = new PolyRepository();
            apiService = Instance.createService(PolyApiService.class);
        }
        return polyRepository;
    }

    public void getApiAsset(String assetId) {
        apiService.getApiAsset(assetId, API_KEY).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {

                if(response.isSuccessful()) {
                    JsonObject obj = response.body();

                    if (obj == null) return;

                    PolyAsset item = new PolyAsset(obj.get("name").getAsString());
                    item.displayName = obj.get("displayName").getAsString();
                    item.authorName = obj.get("authorName").getAsString();
                    item.license = obj.get("license").getAsString();

                    if (obj.has("description")) {
                        item.description = obj.get("description").getAsString();
                    }

                    // Find the glTF URL.
                    JsonArray formats = obj.getAsJsonArray("formats");
                    for (int j = 0; j < formats.size(); j++) {
                        JsonObject format = formats.get(j).getAsJsonObject();
                        if (format.get("formatType").getAsString().equals("GLTF2")) {
                            item.modelUrl = format.get("root").getAsJsonObject().get("url").getAsString();
                            break;
                        }
                    }

                    AppState.getInstance().queuePolyAsset(item);
                    Log.i(TAG, "post submitted to API." + obj.toString());
                }
                else {
                    Log.i(TAG, "post submitted to API." + response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                Log.e(TAG, "Unable to submit post to API.");
            }
        });
    }
}
