package com.upv.muitss.arevi.logic.web.interfaces;

import com.google.gson.JsonObject;
import com.upv.muitss.arevi.entities.Assessment;
import com.upv.muitss.arevi.models.AccessToken;
import com.upv.muitss.arevi.models.DataResponse;
import com.upv.muitss.arevi.entities.Profile;
import com.upv.muitss.arevi.entities.Round;
import com.upv.muitss.arevi.entities.Task;
import com.upv.muitss.arevi.entities.User;
import com.upv.muitss.arevi.entities.UserInfo;
import com.upv.muitss.arevi.entities.UserLogIn;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AREVIApiService {

    String USER_API_ROUTE = "/user";
    String AUTH_API_ROUTE = "/authentication";
    String USER_INFO_API_ROUTE = "/user-info";
    String PROFILE_API_ROUTE = "/profile";
    String TASK_API_ROUTE = "/task";
    String ROUND_API_ROUTE = "/round";
    String ASSESSMENT_API_ROUTE = "/assessment";
    String BUILD_VERSION_API_ROUTE = "/build-version";

    @GET(USER_API_ROUTE)
    Call<DataResponse<User>> getApiUser(@Query("") String id);

    @POST(USER_API_ROUTE)
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<User> postApiUser(@Body User pUser);

    @PATCH(USER_API_ROUTE + "/{id}")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<User> patchApiUser(@Path("id") String id, @Body User pUser);


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

    @PUT(USER_INFO_API_ROUTE + "/{id}")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<UserInfo> putApiUserInfo(@Path("id") String id, @Body UserInfo pUserInfo);


    @GET(PROFILE_API_ROUTE)
    Call<DataResponse<Profile>> findApiProfile(@Query("userId") String userId, @Query("enabled") int enabled, @Query("$limit") int limit
            , @Query("$sort[createdAt]") int order);

    @POST(PROFILE_API_ROUTE)
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<Profile> postApiProfile(@Body Profile pUserInfo);

    @PUT(PROFILE_API_ROUTE + "/{id}")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<Profile> putApiProfile(@Path("id") String id, @Body Profile pUserInfo);


    @GET(TASK_API_ROUTE)
    Call<DataResponse<Task>> findApiTask(@Query("enabled") int enabled);


    @GET(ROUND_API_ROUTE)
    Call<DataResponse<Round>> findApiRound(@Query("completed") int completed, @Query("$limit") int limit
            , @Query("$sort[createdAt]") int order);

    @POST(ROUND_API_ROUTE)
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<Round> postApiRound(@Body Round pRound);

    @PATCH(ROUND_API_ROUTE + "/{id}")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<Round> patchApiRound(@Path("id") String id, @Body JsonObject object);


    @POST(ASSESSMENT_API_ROUTE)
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<Assessment> postApiAssessment(@Body Assessment pRound);

    @PATCH(ASSESSMENT_API_ROUTE + "/{id}")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<Assessment> patchApiAssessment(@Path("id") String id, @Body JsonObject object);


    @GET(BUILD_VERSION_API_ROUTE)
    Call<DataResponse<JsonObject>> findApiBuildVersion(@Query("enabled") int enabled);
}

