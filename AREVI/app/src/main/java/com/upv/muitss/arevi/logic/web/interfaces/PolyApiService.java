package com.upv.muitss.arevi.logic.web.interfaces;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PolyApiService {

    String ASSETS_API_ROUTE = "/v1/assets";

    @GET(ASSETS_API_ROUTE + "/{assetId}")
    Call<JsonObject> getApiAsset(@Path("assetId") String id, @Query("key") String key);
}

