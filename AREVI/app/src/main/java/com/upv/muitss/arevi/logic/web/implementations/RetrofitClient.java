package com.upv.muitss.arevi.logic.web.implementations;

import com.upv.muitss.arevi.R;
import com.upv.muitss.arevi.helpers.Utils;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_AREVI_URL = Utils.getResourceString(R.string.signaling_server_url);
    private static final String BASE_POLY_URL = Utils.getResourceString(R.string.poly_server_url);
    private static Retrofit AREVIRetrofit = null;
    private static Retrofit.Builder AREVIBuilder = null;
    private static Retrofit polyRetrofit = null;

    public static Retrofit getPolyClient() {
        if (polyRetrofit ==null) {
            polyRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_POLY_URL)
                    .addConverterFactory(GsonConverterFactory.create()).build();
        }
        return polyRetrofit;
    }

    public static Retrofit getAreviClient() {
        if (AREVIRetrofit ==null) {
            AREVIBuilder = new Retrofit.Builder()
                    .baseUrl(BASE_AREVI_URL)
                    .addConverterFactory(GsonConverterFactory.create());

            AREVIRetrofit = AREVIBuilder.build();
        }
        return AREVIRetrofit;
    }

    public static Retrofit getAreviClient(OkHttpClient client) {
        if (AREVIRetrofit ==null) {
            AREVIBuilder = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(BASE_AREVI_URL)
                    .addConverterFactory(GsonConverterFactory.create());

            AREVIRetrofit = AREVIBuilder.build();
        }
        else {
            AREVIRetrofit = AREVIBuilder.client(client).build();
        }
        return AREVIRetrofit;
    }
}
