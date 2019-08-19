package com.upv.muitss.arevi.logic.web.interceptors;

import android.text.TextUtils;

import com.upv.muitss.arevi.logic.web.implementations.RetrofitClient;
import com.upv.muitss.arevi.logic.web.interfaces.PolyApiService;

import okhttp3.OkHttpClient;

public class Instance {

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    public static <S> S createService(Class<S> serviceClass) {
        if (serviceClass == PolyApiService.class){
            return RetrofitClient.getPolyClient().create(serviceClass);
        }
        return createService(serviceClass, null);
    }

    public static <S> S createService(Class<S> serviceClass, final String authToken) {
        if (!TextUtils.isEmpty(authToken)) {
            AuthenticationInterceptor interceptor =
                    new AuthenticationInterceptor(authToken);

            if (!httpClient.interceptors().contains(interceptor)) {
                httpClient.addInterceptor(interceptor);

                return RetrofitClient.getAreviClient(httpClient.build()).create(serviceClass);
            }
        }

        return RetrofitClient.getAreviClient().create(serviceClass);
    }
}
