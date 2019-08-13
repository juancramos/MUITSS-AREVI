package com.upv.muitss.arevi.logic.web.implementations;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "http://192.168.0.105:3030/";
    private static Retrofit retrofit = null;
    private static Retrofit.Builder builder = null;

    public static Retrofit getClient() {
        if (retrofit==null) {
            builder = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

            retrofit = builder.build();
        }
        return retrofit;
    }

    public static Retrofit getClient(OkHttpClient client) {
        if (retrofit==null) {
            builder = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

            retrofit = builder.build();
        }
        else {
            retrofit = builder.client(client).build();
        }
        return retrofit;
    }
}
