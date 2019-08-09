package com.upv.muitss.arevi.logic.web.interfaces;

import com.upv.muitss.arevi.entities.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    String USER_API_ROUTE = "/user";

    @GET(USER_API_ROUTE)
    Call<User> getUser();

    @POST(USER_API_ROUTE)
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<User> postUser(@Body User pUser);
}

