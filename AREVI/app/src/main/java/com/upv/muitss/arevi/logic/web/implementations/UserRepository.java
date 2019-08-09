package com.upv.muitss.arevi.logic.web.implementations;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.upv.muitss.arevi.entities.AccessToken;
import com.upv.muitss.arevi.entities.DataResponse;
import com.upv.muitss.arevi.entities.User;
import com.upv.muitss.arevi.entities.UserLogIn;
import com.upv.muitss.arevi.helpers.AppState;
import com.upv.muitss.arevi.helpers.Utils;
import com.upv.muitss.arevi.logic.web.interfaces.ActivityMessage;
import com.upv.muitss.arevi.logic.web.interfaces.UserApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {

    private final String TAG = this.getClass().getCanonicalName();
    private static UserRepository userRepository = null;
    private static UserApiService mUserApiService;

    public static UserRepository getInstance(){
        if (userRepository == null){
            userRepository = new UserRepository();
            mUserApiService = ApiUtils.createService(UserApiService.class);
        }
        return userRepository;
    }

    public void registerUser(User user) {
        AppState.getInstance().getUser().fetchingData = true;
        mUserApiService.postUser(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {

                if(response.isSuccessful()) {
                    User apiUser = response.body();

                    assert apiUser != null;

                    Log.i(TAG, "post submitted to API." + response.body().toString());

                    Utils.saveLogIn(user.email, user.password);
                    logIn();
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

    private void logIn(){
        logIn(null);
    }

    public void logIn(ActivityMessage caller){
        UserLogIn userLogIn = Utils.getLogIn();
        if (!TextUtils.isEmpty(userLogIn.email) && !TextUtils.isEmpty(userLogIn.password)){

            AppState.getInstance().getUser().fetchingData = true;
            mUserApiService.auth(new UserLogIn(userLogIn.email, userLogIn.password)).enqueue(new Callback<AccessToken>() {
                @Override
                public void onResponse(@NonNull Call<AccessToken> call, @NonNull Response<AccessToken> response) {

                    if(response.isSuccessful()) {
                        AccessToken token = response.body();

                        Log.i(TAG, "post submitted to API." + response.body());

                        assert token != null;

                        mUserApiService = ApiUtils.createService(UserApiService.class, token.accessToken);
                        AppState.getInstance().getUser().fetchingData = false;
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

    public void getUser(String userId) {
        AppState.getInstance().getUser().fetchingData = true;
        mUserApiService.getUser(userId).enqueue(new Callback<DataResponse<User>>() {
            @Override
            public void onResponse(@NonNull Call<DataResponse<User>> call, @NonNull Response<DataResponse<User>> response) {

                if(response.isSuccessful()) {
                    DataResponse<User> apiUser = response.body();
                    assert apiUser != null;
                    User user = apiUser.data.get(0);

                    AppState.getInstance().setUser(user);
                    Utils.popProgressDialog(null, null);
                    Log.i(TAG, "post submitted to API." + response.body().toString());
                    AppState.getInstance().getUser().fetchingData = false;
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
}
