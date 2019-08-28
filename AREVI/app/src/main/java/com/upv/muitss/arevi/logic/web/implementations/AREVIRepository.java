package com.upv.muitss.arevi.logic.web.implementations;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.upv.muitss.arevi.entities.Assessment;
import com.upv.muitss.arevi.entities.Content;
import com.upv.muitss.arevi.models.AccessToken;
import com.upv.muitss.arevi.models.DataResponse;
import com.upv.muitss.arevi.entities.Profile;
import com.upv.muitss.arevi.entities.Round;
import com.upv.muitss.arevi.entities.Task;
import com.upv.muitss.arevi.entities.User;
import com.upv.muitss.arevi.entities.UserInfo;
import com.upv.muitss.arevi.entities.UserLogIn;
import com.upv.muitss.arevi.entities.Work;
import com.upv.muitss.arevi.helpers.AppState;
import com.upv.muitss.arevi.helpers.Constants;
import com.upv.muitss.arevi.helpers.Utils;
import com.upv.muitss.arevi.logic.web.interceptors.Instance;
import com.upv.muitss.arevi.logic.web.interfaces.AREVIApiService;
import com.upv.muitss.arevi.logic.web.interfaces.ActivityMessage;

import java.util.Arrays;
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
        AppState.getInstance().setFetchingData(true);
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
        AppState.getInstance().setFetchingData(true);
        apiService.patchApiUser(id, user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {

                Utils.popProgressDialog(null, null);
                if(response.isSuccessful()) {
                    User apiUser = response.body();

                    assert apiUser != null;

                    Log.i(TAG, "post submitted to API." + apiUser.toString());

                    Utils.saveLogIn(apiUser.email, user.password);
                    Utils.saveUserId(apiUser.id);
                    logIn(caller);
                }
                else {
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

                Utils.popProgressDialog(null, null);
                if(response.isSuccessful()) {
                    UserInfo apiUserInfo = response.body();

                    assert apiUserInfo != null;

                    Log.i(TAG, "post submitted to API." + apiUserInfo.toString());

                    AppState.getInstance().setUserInfo(apiUserInfo);

                    if (caller != null) caller.onResponse(apiUserInfo);
                }
                else {
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

                Utils.popProgressDialog(null, null);
                if(response.isSuccessful()) {
                    UserInfo apiUserInfo = response.body();

                    assert apiUserInfo != null;

                    Log.i(TAG, "post submitted to API." + apiUserInfo.toString());

                    AppState.getInstance().setUserInfo(apiUserInfo);

                    if (caller != null) caller.onResponse(apiUserInfo);
                }
                else {
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

                Utils.popProgressDialog(null, null);
                if(response.isSuccessful()) {
                    Profile apiProfile = response.body();

                    assert apiProfile != null;
                    AppState.getInstance().setProfile(apiProfile);

                    Log.i(TAG, "post submitted to API." + apiProfile.toString());
                }
                else {
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

                Utils.popProgressDialog(null, null);
                if(response.isSuccessful()) {
                    Profile apiProfile = response.body();

                    assert apiProfile != null;
                    AppState.getInstance().setProfile(apiProfile);

                    Log.i(TAG, "post submitted to API." + apiProfile.toString());
                }
                else {
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

    public void postAssessment(Assessment assessment) {
        AppState.getInstance().setFetchingData(true);
        if (TextUtils.isEmpty(Utils.getUserId()) || TextUtils.isEmpty(AppState.getInstance().getTask().id)
                || TextUtils.isEmpty(AppState.getInstance().getProfile().id)
                || TextUtils.isEmpty(AppState.getInstance().getRound().id)) return;

        assessment.userId = Utils.getUserId();
        assessment.taskId = AppState.getInstance().getTask().id;
        assessment.profileId = AppState.getInstance().getProfile().id;
        assessment.roundId = AppState.getInstance().getRound().id;
        apiService.postApiAssessment(assessment).enqueue(new Callback<Assessment>() {
            @Override
            public void onResponse(@NonNull Call<Assessment> call, @NonNull Response<Assessment> response) {

                if(response.isSuccessful()) {
                    Assessment apiAssessment = response.body();

                    assert apiAssessment != null;

                    AppState.getInstance().addAssessment(apiAssessment);
                    AppState.getInstance().setFetchingData(false);
                    Log.i(TAG, "post submitted to API." + apiAssessment.toString());

                }
                else {
                    Log.i(TAG, "post submitted to API." + response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Assessment> call, @NonNull Throwable t) {
                AppState.getInstance().setFetchingData(false);
                Log.e(TAG, "Unable to submit post to API.");
            }
        });
    }

    public void patchAssessment(String id, List<Content> content, boolean completed) {
        JsonObject json = new JsonObject();
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        json.add("content", gson.toJsonTree(content));
        json.addProperty("completed", gson.toJson(completed));
        apiService.patchApiAssessment(id, json).enqueue(new Callback<Assessment>() {
            @Override
            public void onResponse(@NonNull Call<Assessment> call, @NonNull Response<Assessment> response) {

                if(response.isSuccessful()) {
                    Assessment apiAssessment = response.body();

                    assert apiAssessment != null;

                    AppState.getInstance().addAssessment(apiAssessment);
                    Log.i(TAG, "post submitted to API." + apiAssessment.toString());
                }
                else {
                    Log.i(TAG, "post submitted to API." + response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Assessment> call, @NonNull Throwable t) {
                Log.e(TAG, "Unable to submit post to API.");
            }
        });
    }

    private void logIn(ActivityMessage caller){
        logIn(Utils.getLogIn(), caller);
    }

    public void logIn(UserLogIn userLogIn, ActivityMessage caller){
        if (!TextUtils.isEmpty(userLogIn.email) && !TextUtils.isEmpty(userLogIn.password)){

            AppState.getInstance().setFetchingData(true);
            apiService.authApi(new UserLogIn(userLogIn.email, userLogIn.password)).enqueue(new Callback<AccessToken>() {
                @Override
                public void onResponse(@NonNull Call<AccessToken> call, @NonNull Response<AccessToken> response) {

                    AppState.getInstance().setFetchingData(false);
                    Utils.popProgressDialog(null, null);
                    if(response.isSuccessful()) {
                        AccessToken token = response.body();
                        Log.i(TAG, "post submitted to API." + response.body());

                        assert token != null;

                        Utils.saveLogIn(userLogIn.email, userLogIn.password);
                        apiService = Instance.createService(AREVIApiService.class, token.accessToken);

                        getApiUser(null);

                        if (caller != null) caller.onResponse(token.accessToken);
                    }
                    else {
                        Log.i(TAG, "post submitted to API." + response.body());
                        if (caller != null) caller.onResponse(null);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<AccessToken> call, @NonNull Throwable t) {
                    AppState.getInstance().setFetchingData(false);
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
        AppState.getInstance().setFetchingData(true);
        apiService.getApiUser(userId).enqueue(new Callback<DataResponse<User>>() {
            @Override
            public void onResponse(@NonNull Call<DataResponse<User>> call, @NonNull Response<DataResponse<User>> response) {

                AppState.getInstance().setFetchingData(false);
                Utils.popProgressDialog(null, null);
                if(response.isSuccessful()) {
                    DataResponse<User> apiUser = response.body();
                    assert apiUser != null;
                    if(apiUser.data.isEmpty()) {
                        return;
                    }
                    User user = apiUser.data.get(0);

                    Utils.saveUserId(user.id);
                    user.password = Utils.getLogIn().password;
                    AppState.getInstance().setUser(user);

                    Log.i(TAG, "post submitted to API." + apiUser.toString());
                }
                else {
                    Log.i(TAG, "post submitted to API." + response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<DataResponse<User>> call, @NonNull Throwable t) {
                AppState.getInstance().setFetchingData(false);
                Utils.popProgressDialog(null, null);
                Log.e(TAG, "Unable to submit post to API.");
            }
        });
    }

    public void getApiUserInfo(String userId, ActivityMessage caller) {
        apiService.findApiUserInfo(userId).enqueue(new Callback<DataResponse<UserInfo>>() {
            @Override
            public void onResponse(@NonNull Call<DataResponse<UserInfo>> call, @NonNull Response<DataResponse<UserInfo>> response) {

                Utils.popProgressDialog(null, null);
                if(response.isSuccessful()) {
                    DataResponse<UserInfo> apiUserInfo = response.body();
                    assert apiUserInfo != null;
                    if(apiUserInfo.data.isEmpty()) {
                        if (caller != null) caller.onResponse(null);
                        return;
                    }
                    UserInfo userInfo = apiUserInfo.data.get(0);

                    AppState.getInstance().setUserInfo(userInfo);
                    if (caller != null) caller.onResponse(userInfo.id);

                    Log.i(TAG, "post submitted to API." + userInfo.toString());
                }
                else {
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
        Profile p = AppState.getInstance().getProfile();
        if (p != null && !TextUtils.isEmpty(p.id)  && p.getConfiguration() != null && !TextUtils.isEmpty(p.getConfiguration().getUseGoogleCardboard())) {
            if (caller != null) caller.onResponse(p);
            Utils.popProgressDialog(null, null);
            return;
        }
        apiService.findApiProfile(userId, 1).enqueue(new Callback<DataResponse<Profile>>() {
            @Override
            public void onResponse(@NonNull Call<DataResponse<Profile>> call, @NonNull Response<DataResponse<Profile>> response) {

                Utils.popProgressDialog(null, null);
                if(response.isSuccessful()) {
                    DataResponse<Profile> apiProfile = response.body();
                    assert apiProfile != null;
                    if(apiProfile.data.isEmpty()) {
                        if (caller != null) caller.onResponse(null);
                        return;
                    }
                    Profile profile = apiProfile.data.get(0);

                    AppState.getInstance().setProfile(profile);
                    if (caller != null) caller.onResponse(profile);

                    Log.i(TAG, "post submitted to API." + profile.toString());
                }
                else {
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
        Task t = AppState.getInstance().getTask();
        if (t != null && !TextUtils.isEmpty(t.id) && !TextUtils.isEmpty(t.getEnabled())) {
            if (caller != null) caller.onResponse(t);
            Utils.popProgressDialog(null, null);
            return;
        }
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

    public void getApiRound() {
        Round r = AppState.getInstance().getRound();
        if (r != null && !TextUtils.isEmpty(r.id) && !TextUtils.isEmpty(r.getCompleted())) {
            Utils.popProgressDialog(null, null);
            return;
        }
        apiService.findApiRound(1, 1, -1).enqueue(new Callback<DataResponse<Round>>() {
            @Override
            public void onResponse(@NonNull Call<DataResponse<Round>> call, @NonNull Response<DataResponse<Round>> response) {

                if(response.isSuccessful()) {
                    DataResponse<Round> apiRound = response.body();
                    assert apiRound != null;
                    if(apiRound.data.isEmpty()) {
                        return;
                    }

                    Round round = apiRound.data.get(0);

                    AppState.getInstance().setRound(round);

                    Log.i(TAG, "post submitted to API." + round.toString());
                }
                else {
                    Log.i(TAG, "post submitted to API." + response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<DataResponse<Round>> call, @NonNull Throwable t) {
                Log.e(TAG, "Unable to submit post to API.");
            }
        });
    }

    public void getApiBuildVersion(ActivityMessage caller) {
        apiService.findApiBuildVersion(1).enqueue(new Callback<DataResponse<JsonObject>>() {
            @Override
            public void onResponse(@NonNull Call<DataResponse<JsonObject>> call, @NonNull Response<DataResponse<JsonObject>> response) {

                if(response.isSuccessful()) {
                    DataResponse<JsonObject> apiBuildVersion = response.body();
                    assert apiBuildVersion != null;
                    if(apiBuildVersion.data.isEmpty()) {
                        if (caller != null) caller.onResponse(Arrays.asList(Constants.CURRENT_BUILD, null));
                        return;
                    }

                    JsonObject buildVersion = apiBuildVersion.data.get(0);

                    if (caller != null) caller.onResponse(Arrays.asList(Constants.CURRENT_BUILD, buildVersion.get("version").getAsString()));

                    Log.i(TAG, "post submitted to API." + buildVersion.get("version").getAsString());
                }
                else {
                    Log.i(TAG, "post submitted to API." + response.body());
                    if (caller != null) caller.onResponse(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<DataResponse<JsonObject>> call, @NonNull Throwable t) {
                Log.e(TAG, "Unable to submit post to API.");
                if (caller != null) caller.onResponse(null);
            }
        });
    }
}
