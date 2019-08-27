package com.upv.muitss.arevi.helpers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.upv.muitss.arevi.MainActivity;
import com.upv.muitss.arevi.R;
import com.upv.muitss.arevi.entities.UserLogIn;
import com.upv.muitss.arevi.models.IceServer;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;

public class Utils {
    private static ProgressDialog progressDialog = null;

    public static String getResourceString(int id){
        return MainActivity.appContext.getString(id);
    }

    public static void saveTheme(@NonNull Activity context, String value) {
        UserPreferences userPreferences = UserPreferences.getInstance();
        userPreferences.saveUserPreferenceString(Constants.USER_SELECTED_THEME, value);
        context.recreate();
    }

    public static boolean isDarkTheme(){
        String selectedTheme = getSavedTheme();

        if(selectedTheme == null) return false;

        switch (selectedTheme){
            case Constants.APP_DARK_THEME_DEFAULT_FONT_SIZE:
            case Constants.APP_DARK_THEME_MEDIUM_FONT_SIZE:
            case Constants.APP_DARK_THEME_LARGE_FONT_SIZE:
                return true;
            default:
                return false;
        }
    }

    public static int getSavedThemeStyle() {
        int theme = -1;
        String selectedTheme = getSavedTheme();
        if (selectedTheme == null) return R.style.AppTheme;

        switch (selectedTheme) {
            case Constants.APP_THEME_DEFAULT_FONT_SIZE:
                theme = R.style.AppTheme;
                break;
            case Constants.APP_THEME_MEDIUM_FONT_SIZE:
                theme = R.style.FontSizeMedium;
                break;
            case Constants.APP_THEME_LARGE_FONT_SIZE:
                theme = R.style.FontSizeLarge;
                break;
            case Constants.APP_DARK_THEME_DEFAULT_FONT_SIZE:
                theme = R.style.AppThemeDark;
                break;
            case Constants.APP_DARK_THEME_MEDIUM_FONT_SIZE:
                theme = R.style.FontSizeMediumDark;
                break;
            case Constants.APP_DARK_THEME_LARGE_FONT_SIZE:
                theme = R.style.FontSizeLargeDark;
                break;
        }
        return theme;
    }

    public static void validateForm(View view) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup)view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View v = viewGroup.getChildAt(i);
                if (v instanceof EditText) {
                    validateInput(v);
                } else{
                    validateForm(v);
                }
            }
        }
    }

    public static String validateInput(View view) {
        if (view instanceof EditText){
            EditText editText = (EditText)view;
            String textFromEditView = editText.getText().toString();
            // Reset errors.
            TextInputLayout parent = (TextInputLayout) view.getParent().getParent();
            parent.setError(null);
            if (Utils.isNullOrEmpty(textFromEditView)) {
                parent.setError(getResourceString(R.string.form_validation_error_empty));
            }
            else if (editText.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS) && Utils.emailValidation(textFromEditView)){
                parent.setError(getResourceString(R.string.form_validation_error_email));
            }
            return textFromEditView;
        }
        return null;
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void popProgressDialog(Context context, String message){
        if (progressDialog == null && context != null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(message);
            progressDialog.show();
        }
        else if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public static void saveLogIn(String email, String password) {
        UserPreferences userPreferences = UserPreferences.getInstance();
        userPreferences.saveUserPreferenceString(Constants.USER_EMAIL, email);
        userPreferences.saveUserPreferenceString(Constants.USER_PASSWORD, password);
    }

    public static UserLogIn getLogIn(){
        UserPreferences userPreferences = UserPreferences.getInstance();
        return new UserLogIn(userPreferences.getUserPreferenceString(Constants.USER_EMAIL),
                userPreferences.getUserPreferenceString(Constants.USER_PASSWORD));
    }

    public static void saveUserId(String userId) {
        UserPreferences userPreferences = UserPreferences.getInstance();
        userPreferences.saveUserPreferenceString(Constants.USER_ID, userId);
    }

    public static void saveMode(boolean mode) {
        UserPreferences userPreferences = UserPreferences.getInstance();
        userPreferences.saveUserPreferenceBool(Constants.USER_SELECTED_MODE, mode);
    }

    public static void saveCurrentBuild(String buildVersion) {
        UserPreferences userPreferences = UserPreferences.getInstance();
        userPreferences.saveUserPreferenceString(Constants.CURRENT_BUILD, buildVersion);
    }

    public static String getUserId(){
        UserPreferences userPreferences = UserPreferences.getInstance();
        return userPreferences.getUserPreferenceString(Constants.USER_ID);
    }

    public static String getSavedTheme(){
        UserPreferences userPreferences = UserPreferences.getInstance();
        return userPreferences.getUserPreferenceString(Constants.USER_SELECTED_THEME);
    }

    public static String getCurrentBuild(){
        UserPreferences userPreferences = UserPreferences.getInstance();
        return userPreferences.getUserPreferenceString(Constants.CURRENT_BUILD);
    }

    public static boolean getSavedMode(){
        UserPreferences userPreferences = UserPreferences.getInstance();
        return userPreferences.getUserPreferenceBool(Constants.USER_SELECTED_MODE);
    }

    public static List<IceServer> getIceServerData(Context context) {
        String jsonString = getAssetsJSON(context);
        Gson gson = new Gson();
        Type listType = new TypeToken<List<IceServer>>(){}.getType();
        return gson.fromJson(jsonString, listType);
    }

    /**
     * Util Methods
     */
    public static void showToast(Activity ac, final String msg) {
        ac.runOnUiThread(() -> Toast.makeText(ac, msg, Toast.LENGTH_SHORT).show());
    }

    private static String getAssetsJSON(Context context) {
        String json = null;
        try {
            InputStream inputStream = context.getAssets().open(context.getString(R.string.ice_candidates_file_name));
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            if (inputStream.read(buffer) != -1)
                json = new String(buffer, StandardCharsets.UTF_8);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return json;
    }

    private static boolean isNullOrEmpty(String text) {
        return text == null || text.isEmpty();
    }

    private static boolean emailValidation(@NonNull String email){
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return !pattern.matcher(email.trim()).matches();
    }
}
