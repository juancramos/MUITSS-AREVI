package com.upv.muitss.arevi.logic.web.implementations;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.upv.muitss.arevi.entities.AccessToken;
import com.upv.muitss.arevi.entities.DataResponse;
import com.upv.muitss.arevi.entities.User;
import com.upv.muitss.arevi.entities.UserInfo;
import com.upv.muitss.arevi.entities.UserLogIn;
import com.upv.muitss.arevi.helpers.AppState;
import com.upv.muitss.arevi.helpers.Utils;
import com.upv.muitss.arevi.logic.web.interfaces.AREVIApiService;
import com.upv.muitss.arevi.logic.web.interfaces.ActivityMessage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AREVIRepository {

    private final String TAG = this.getClass().getCanonicalName();
    private static AREVIRepository AREVIRepository = null;
    private static AREVIApiService apiService;

    public static AREVIRepository getInstance(){
        if (AREVIRepository == null){
            AREVIRepository = new AREVIRepository();
            apiService = Utils.createService(AREVIApiService.class);
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

    public void postUserInfo(UserInfo userInfo) {
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
                }
                else {
                    Utils.popProgressDialog(null, null);
                    Log.i(TAG, "post submitted to API." + response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserInfo> call, @NonNull Throwable t) {
                Utils.popProgressDialog(null, null);
                Log.e(TAG, "Unable to submit post to API.");
            }
        });
    }

    public void logIn(ActivityMessage caller){
        UserLogIn userLogIn = Utils.getLogIn();
        if (!TextUtils.isEmpty(userLogIn.email) && !TextUtils.isEmpty(userLogIn.password)){

            AppState.getInstance().getUser().fetchingData = true;
            apiService.authApi(new UserLogIn(userLogIn.email, userLogIn.password)).enqueue(new Callback<AccessToken>() {
                @Override
                public void onResponse(@NonNull Call<AccessToken> call, @NonNull Response<AccessToken> response) {

                    if(response.isSuccessful()) {
                        AppState.getInstance().getUser().fetchingData = false;
                        AccessToken token = response.body();
                        Log.i(TAG, "post submitted to API." + response.body());

                        assert token != null;

                        apiService = Utils.createService(AREVIApiService.class, token.accessToken);

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

                if(response.isSuccessful()) {
                    AppState.getInstance().getUser().fetchingData = false;
                    DataResponse<User> apiUser = response.body();
                    assert apiUser != null;
                    User user = apiUser.data.get(0);

                    AppState.getInstance().setUser(user);
                    Utils.saveUserId(user.id);
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
}
