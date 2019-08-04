package com.upv.muitss.arevi.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.upv.muitss.arevi.R;

public class UserPreferences {
    private final String TAG = this.getClass().getCanonicalName();
    private String PREFERENCE_FILE_KEY;

    private static UserPreferences instance;
    private SharedPreferences sharedPref;

    public static UserPreferences getInstance() {
        if (instance == null) {
            instance = new UserPreferences();
        }
        return instance;
    }

    public void init(Context context){
        sharedPref = context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        PREFERENCE_FILE_KEY = context.getString(R.string.app_preferences_name);
    }

    public void saveUserPreferenceString(String key, String val){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, val);
        editor.apply();
    }

    public String getUserPreferenceString(String key) {
        return sharedPref.getString(key, null);
    }

    public void saveUserPreferenceFloat(String key, Float val){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putFloat(key, val);
        editor.apply();
    }

    public float getUserPreferenceFloat(String key) {
        return sharedPref.getFloat(key, -1);
    }

    public void saveUserPreferenceInt(String key, int val){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, val);
        editor.apply();
    }

    public int getUserPreferenceInt(String key) {
        return sharedPref.getInt(key, -1);
    }

    public void saveUserPreferenceBool(String key, boolean val){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, val);
        editor.apply();
    }

    public boolean getUserPreferenceBool(String key) {
        return sharedPref.getBoolean(key, false);
    }

    public void saveUserPreferenceLong(String key, long val){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(key, val);
        editor.apply();
    }

    public long getUserPreferenceLongl(String key) {
        return sharedPref.getLong(key, -1);
    }
}
