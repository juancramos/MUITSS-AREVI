package com.upv.muitss.arevi.logic.web.implementations;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.upv.muitss.arevi.entities.AccessToken;
import com.upv.muitss.arevi.entities.DataResponse;
import com.upv.muitss.arevi.entities.Profile;
import com.upv.muitss.arevi.entities.Round;
import com.upv.muitss.arevi.entities.Task;
import com.upv.muitss.arevi.entities.User;
import com.upv.muitss.arevi.entities.UserInfo;
import com.upv.muitss.arevi.entities.UserLogIn;
import com.upv.muitss.arevi.entities.Work;
import com.upv.muitss.arevi.helpers.AppState;
import com.upv.muitss.arevi.helpers.Utils;
import com.upv.muitss.arevi.logic.web.interceptors.Instance;
import com.upv.muitss.arevi.logic.web.interfaces.AREVIApiService;
import com.upv.muitss.arevi.logic.web.interfaces.ActivityMessage;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AREVIRepository {

    private final String TAG = this.getClass().getCanonicalName();
    private static AREVIRepository AREVIRepository = null;
    private static AREVIApiService apiService = null;

    public static AREVIRepository getInstance(){
        if (AREVIRepository == null){
            AREVIRepository = new AREVIRepository();
            apiService = Instance.createService(AREVIApiService.class);
        }
        return AREVIRepository;
    }

    public void registerUser(User user, ActivityMessage caller) {
        user.fetchingData = true;
        apiService.postApiUser(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {

                if(response.isSuccessful()) {
                    User apiUser = response.body();

                    assert apiUser != null;

                    Log.i(TAG, "post submitted to API." + apiUser.toString());

                    Utils.saveLogIn(apiUser.email, user.password);
                    Utils.saveUserId(apiUser.id);
                    logIn(caller);
                }
                else {
                    Utils.popProgressDialog(null, null);
                    Log.i(TAG, "post submitted to API." + response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Utils.popProgressDialog(null, null);
                Log.e(TAG, "Unable to submit post to API.");
            }
        });
    }

    public void updateUser(String id, User user, ActivityMessage caller) {
        user.fetchingData = true;
        apiService.patchApiUser(id, user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {

                if(response.isSuccessful()) {
                    User apiUser = response.body();

                    assert apiUser != null;

                    Log.i(TAG, "post submitted to API." + apiUser.toString());

                    Utils.saveLogIn(apiUser.email, user.password);
                    Utils.saveUserId(apiUser.id);
                    logIn(caller);
                }
                else {
                    Utils.popProgressDialog(null, null);
                    Log.i(TAG, "post submitted to API." + response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Utils.popProgressDialog(null, null);
                Log.e(TAG, "Unable to submit post to API.");
            }
        });
    }

    public void postUserInfo(UserInfo userInfo, ActivityMessage caller) {
        userInfo.userId = Utils.getUserId();
        apiService.postApiUserInfo(userInfo).enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(@NonNull Call<UserInfo> call, @NonNull Response<UserInfo> response) {

                if(response.isSuccessful()) {
                    UserInfo apiUserInfo = response.body();

                    assert apiUserInfo != null;

                    Log.i(TAG, "post submitted to API." + apiUserInfo.toString());

                    AppState.getInstance().setUserInfo(apiUserInfo);

                    Utils.popProgressDialog(null, null);
                    if (caller != null) caller.onResponse(apiUserInfo);
                }
                else {
                    Utils.popProgressDialog(null, null);
                    Log.i(TAG, "post submitted to API." + response.body());
                    if (caller != null) caller.onResponse(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserInfo> call, @NonNull Throwable t) {
                Utils.popProgressDialog(null, null);
                Log.e(TAG, "Unable to submit post to API.");
                if (caller != null) caller.onResponse(null);
            }
        });
    }

    public void updateUserInfo(String id, UserInfo userInfo, ActivityMessage caller) {
        userInfo.userId = Utils.getUserId();
        apiService.putApiUserInfo(id, userInfo).enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(@NonNull Call<UserInfo> call, @NonNull Response<UserInfo> response) {

                if(response.isSuccessful()) {
                    UserInfo apiUserInfo = response.body();

                    assert apiUserInfo != null;

                    Log.i(TAG, "post submitted to API." + apiUserInfo.toString());

                    AppState.getInstance().setUserInfo(apiUserInfo);

                    Utils.popProgressDialog(null, null);
                    if (caller != null) caller.onResponse(apiUserInfo);
                }
                else {
                    Utils.popProgressDialog(null, null);
                    Log.i(TAG, "post submitted to API." + response.body());
                    if (caller != null) caller.onResponse(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserInfo> call, @NonNull Throwable t) {
                Utils.popProgressDialog(null, null);
                Log.e(TAG, "Unable to submit post to API.");
                if (caller != null) caller.onResponse(null);
            }
        });
    }

    public void postProfile(Profile profile) {
        profile.userId = Utils.getUserId();
        apiService.postApiProfile(profile).enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(@NonNull Call<Profile> call, @NonNull Response<Profile> response) {

                if(response.isSuccessful()) {
                    Profile apiProfile = response.body();

                    assert apiProfile != null;
                    AppState.getInstance().setProfile(apiProfile);

                    Log.i(TAG, "post submitted to API." + apiProfile.toString());

                    Utils.popProgressDialog(null, null);
                }
                else {
                    Utils.popProgressDialog(null, null);
                    Log.i(TAG, "post submitted to API." + response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Profile> call, @NonNull Throwable t) {
                Utils.popProgressDialog(null, null);
                Log.e(TAG, "Unable to submit post to API.");
            }
        });
    }

    public void updateProfile(String id, Profile profile) {
        profile.userId = Utils.getUserId();
        apiService.putApiProfile(id, profile).enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(@NonNull Call<Profile> call, @NonNull Response<Profile> response) {

                if(response.isSuccessful()) {
                    Profile apiProfile = response.body();

                    assert apiProfile != null;
                    AppState.getInstance().setProfile(apiProfile);

                    Log.i(TAG, "post submitted to API." + apiProfile.toString());

                    Utils.popProgressDialog(null, null);
                }
                else {
                    Utils.popProgressDialog(null, null);
                    Log.i(TAG, "post submitted to API." + response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Profile> call, @NonNull Throwable t) {
                Utils.popProgressDialog(null, null);
                Log.e(TAG, "Unable to submit post to API.");
            }
        });
    }

    public void postRound(Round round) {
        if (TextUtils.isEmpty(Utils.getUserId()) || TextUtils.isEmpty(AppState.getInstance().getTask().id)
                || TextUtils.isEmpty(AppState.getInstance().getProfile().id)) return;

        round.userId = Utils.getUserId();
        round.taskId = AppState.getInstance().getTask().id;
        round.profileId = AppState.getInstance().getProfile().id;
        apiService.postApiRound(round).enqueue(new Callback<Round>() {
            @Override
            public void onResponse(@NonNull Call<Round> call, @NonNull Response<Round> response) {

                if(response.isSuccessful()) {
                    Round apiRound = response.body();

                    assert apiRound != null;

                    AppState.getInstance().setRound(apiRound);
                    Log.i(TAG, "post submitted to API." + apiRound.toString());

                }
                else {
                    Log.i(TAG, "post submitted to API." + response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Round> call, @NonNull Throwable t) {
                Log.e(TAG, "Unable to submit post to API.");
            }
        });
    }

    public void patchRound(String id, List<Work> score, boolean completed) {
        JsonObject json = new JsonObject();
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        json.add("score", gson.toJsonTree(score));
        json.addProperty("completed", gson.toJson(completed));
        apiService.patchApiRound(id, json).enqueue(new Callback<Round>() {
            @Override
            public void onResponse(@NonNull Call<Round> call, @NonNull Response<Round> response) {

                if(response.isSuccessful()) {
                    Round apiRound = response.body();

                    assert apiRound != null;

                    AppState.getInstance().setRound(apiRound);
                    Log.i(TAG, "post submitted to API." + apiRound.toString());
                }
                else {
                    Log.i(TAG, "post submitted to API." + response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Round> call, @NonNull Throwable t) {
                Log.e(TAG, "Unable to submit post to API.");
            }
        });
    }

    private void logIn(ActivityMessage caller){
        logIn(Utils.getLogIn(), caller);
    }

    public void logIn(UserLogIn userLogIn, ActivityMessage caller){
        if (!TextUtils.isEmpty(userLogIn.email) && !TextUtils.isEmpty(userLogIn.password)){

            AppState.getInstance().getUser().fetchingData = true;
            apiService.authApi(new UserLogIn(userLogIn.email, userLogIn.password)).enqueue(new Callback<AccessToken>() {
                @Override
                public void onResponse(@NonNull Call<AccessToken> call, @NonNull Response<AccessToken> response) {

                    AppState.getInstance().getUser().fetchingData = false;
                    if(response.isSuccessful()) {
                        AccessToken token = response.body();
                        Log.i(TAG, "post submitted to API." + response.body());

                        assert token != null;

                        Utils.saveLogIn(userLogIn.email, userLogIn.password);
                        apiService = Instance.createService(AREVIApiService.class, token.accessToken);

                        getApiUser(null);

                        Utils.popProgressDialog(null, null);
                        if (caller != null) caller.onResponse(token.accessToken);
                    }
                    else {
                        Utils.popProgressDialog(null, null);
                        Log.i(TAG, "post submitted to API." + response.body());
                        if (caller != null) caller.onResponse(null);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<AccessToken> call, @NonNull Throwable t) {
                    AppState.getInstance().getUser().fetchingData = false;
                    Utils.popProgressDialog(null, null);
                    Log.e(TAG, "Unable to submit post to API.");
                    if (caller != null) caller.onResponse(null);
                }
            });
        }
        else {
            if (caller != null) caller.onResponse(null);
        }
    }

    private void getApiUser(String userId) {
        AppState.getInstance().getUser().fetchingData = true;
        apiService.getApiUser(userId).enqueue(new Callback<DataResponse<User>>() {
            @Override
            public void onResponse(@NonNull Call<DataResponse<User>> call, @NonNull Response<DataResponse<User>> response) {

                AppState.getInstance().getUser().fetchingData = false;
                if(response.isSuccessful()) {
                    DataResponse<User> apiUser = response.body();
                    assert apiUser != null;
                    User user = apiUser.data.get(0);

                    Utils.saveUserId(user.id);
                    user.password = Utils.getLogIn().password;
                    AppState.getInstance().setUser(user);

                    Utils.popProgressDialog(null, null);
                    Log.i(TAG, "post submitted to API." + apiUser.toString());
                }
                else {
                    Utils.popProgressDialog(null, null);
                    Log.i(TAG, "post submitted to API." + response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<DataResponse<User>> call, @NonNull Throwable t) {
                AppState.getInstance().getUser().fetchingData = false;
                Utils.popProgressDialog(null, null);
                Log.e(TAG, "Unable to submit post to API.");
            }
        });
    }

    public void getApiUserInfo(String userId, ActivityMessage caller) {
        apiService.findApiUserInfo(userId).enqueue(new Callback<DataResponse<UserInfo>>() {
            @Override
            public void onResponse(@NonNull Call<DataResponse<UserInfo>> call, @NonNull Response<DataResponse<UserInfo>> response) {

                if(response.isSuccessful()) {
                    DataResponse<UserInfo> apiUserInfo = response.body();
                    assert apiUserInfo != null;
                    UserInfo userInfo = apiUserInfo.data.get(0);

                    AppState.getInstance().setUserInfo(userInfo);
                    if (caller != null) caller.onResponse(userInfo.id);

                    Utils.popProgressDialog(null, null);
                    Log.i(TAG, "post submitted to API." + userInfo.toString());
                }
                else {
                    Utils.popProgressDialog(null, null);
                    Log.i(TAG, "post submitted to API." + response.body());
                    if (caller != null) caller.onResponse(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<DataResponse<UserInfo>> call, @NonNull Throwable t) {
                Utils.popProgressDialog(null, null);
                Log.e(TAG, "Unable to submit post to API.");
                if (caller != null) caller.onResponse(null);
            }
        });
    }

    public void getApiProfile(String userId, ActivityMessage caller) {
        apiService.findApiProfile(userId, 1).enqueue(new Callback<DataResponse<Profile>>() {
            @Override
            public void onResponse(@NonNull Call<DataResponse<Profile>> call, @NonNull Response<DataResponse<Profile>> response) {

                if(response.isSuccessful()) {
                    DataResponse<Profile> apiProfile = response.body();
                    assert apiProfile != null;
                    Profile profile = apiProfile.data.get(0);

                    AppState.getInstance().setProfile(profile);
                    if (caller != null) caller.onResponse(profile);

                    Utils.popProgressDialog(null, null);
                    Log.i(TAG, "post submitted to API." + profile.toString());
                }
                else {
                    Utils.popProgressDialog(null, null);
                    Log.i(TAG, "post submitted to API." + response.body());
                    if (caller != null) caller.onResponse(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<DataResponse<Profile>> call, @NonNull Throwable t) {
                Utils.popProgressDialog(null, null);
                Log.e(TAG, "Unable to submit post to API.");
                if (caller != null) caller.onResponse(null);
            }
        });
    }

    public void getApiTask(ActivityMessage caller) {
        apiService.findApiTask(1).enqueue(new Callback<DataResponse<Task>>() {
            @Override
            public void onResponse(@NonNull Call<DataResponse<Task>> call, @NonNull Response<DataResponse<Task>> response) {

                if(response.isSuccessful()) {
                    DataResponse<Task> apiTask = response.body();
                    assert apiTask != null;
                    if(apiTask.data.isEmpty()) {
                        if (caller != null) caller.onResponse(null);
                        return;
                    }

                    Task task = apiTask.data.get(0);

                    AppState.getInstance().setTask(task);
                    if (caller != null) caller.onResponse(task);

                    Log.i(TAG, "post submitted to API." + task.toString());
                }
                else {
                    Log.i(TAG, "post submitted to API." + response.body());
                    if (caller != null) caller.onResponse(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<DataResponse<Task>> call, @NonNull Throwable t) {
                Log.e(TAG, "Unable to submit post to API.");
                if (caller != null) caller.onResponse(null);
            }
        });
    }
}
