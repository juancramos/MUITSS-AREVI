package com.upv.muitss.arevi.logic.web.implementations;

import android.support.annotation.NonNull;
import android.util.Log;

import com.upv.muitss.arevi.entities.User;
import com.upv.muitss.arevi.helpers.AppState;
import com.upv.muitss.arevi.helpers.Utils;
import com.upv.muitss.arevi.logic.web.interfaces.APIService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {

    private final String TAG = this.getClass().getCanonicalName();
    private static UserRepository userRepository = null;
    private static APIService mAPIService;

    public static UserRepository getInstance(){
        if (userRepository == null){
            userRepository = new UserRepository();
            mAPIService = ApiUtils.getAPIService();
        }
        return userRepository;
    }

    public void authenticateUser(User user) {
        AppState.getInstance().getUser().fetchingData = true;
        mAPIService.postUser(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {

                if(response.isSuccessful()) {
                    AppState.getInstance().setUser(response.body());
                    Utils.popProgressDialog(null, null);
                    Log.i(TAG, "post submitted to API." + response.body().toString());
                    AppState.getInstance().getUser().fetchingData = false;
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, Throwable t) {
                Utils.popProgressDialog(null, null);
                Log.e(TAG, "Unable to submit post to API.");
            }
        });
    }
}
