package com.upv.muitss.arevi.logic.web.implementations;

import com.upv.muitss.arevi.logic.web.interfaces.APIService;

public class ApiUtils {

    private ApiUtils() {}

    private static final String BASE_URL = "http://192.168.0.105:3030/";

    public static APIService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
}