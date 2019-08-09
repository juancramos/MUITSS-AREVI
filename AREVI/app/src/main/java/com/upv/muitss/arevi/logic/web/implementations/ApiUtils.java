package com.upv.muitss.arevi.logic.web.implementations;

import android.text.TextUtils;

import com.upv.muitss.arevi.logic.web.interfaces.AuthenticationInterceptor;

import okhttp3.OkHttpClient;

class ApiUtils {

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private ApiUtils() {}

    static <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass, null);
    }

    static <S> S createService(Class<S> serviceClass, final String authToken) {
        if (!TextUtils.isEmpty(authToken)) {
            AuthenticationInterceptor interceptor =
                    new AuthenticationInterceptor(authToken);

            if (!httpClient.interceptors().contains(interceptor)) {
                httpClient.addInterceptor(interceptor);

                return RetrofitClient.getClient(httpClient.build()).create(serviceClass);
            }
        }

        return RetrofitClient.getClient().create(serviceClass);
    }

}