package com.upv.muitss.arevi.logic.web.interfaces;

import com.upv.muitss.arevi.entities.AccessToken;
import com.upv.muitss.arevi.entities.DataResponse;
import com.upv.muitss.arevi.entities.User;
import com.upv.muitss.arevi.entities.UserInfo;
import com.upv.muitss.arevi.entities.UserLogIn;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AREVIApiService {

    String USER_API_ROUTE = "/user";
    String AUTH_API_ROUTE = "/authentication";
    String USER_INFO_API_ROUTE = "/user-info";

    @GET(USER_API_ROUTE)
    Call<DataResponse<User>> getApiUser(@Query("") String id);

    @POST(USER_API_ROUTE)
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<User> postApiUser(@Body User pUser);

    @POST(AUTH_API_ROUTE)
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<AccessToken> authApi(@Body UserLogIn logIn);

    @GET(USER_INFO_API_ROUTE)
    Call<DataResponse<UserInfo>> getApiUserInfo(@Query("") String id);

    @GET(USER_INFO_API_ROUTE)
    Call<DataResponse<UserInfo>> findApiUserInfo(@Query("userId") String userId);

    @POST(USER_INFO_API_ROUTE)
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<UserInfo> postApiUserInfo(@Body UserInfo pUserInfo);
}

