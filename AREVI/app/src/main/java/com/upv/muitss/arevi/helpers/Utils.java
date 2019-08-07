package com.upv.muitss.arevi.helpers;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Patterns;

import com.upv.muitss.arevi.R;

import java.util.regex.Pattern;

public class Utils {
    private static final String TAG = "Utils";


    public static void saveTheme(@NonNull Activity context, String value) {
        UserPreferences userPreferences = UserPreferences.getInstance();
        userPreferences.saveUserPreferenceString(Constants.USER_SELECTED_THEME, value);
        context.recreate();
    }

    public static boolean isDarkTheme(){
        String selectedTheme = getSavedTheme();
        switch (selectedTheme){
            case Constants.APP_DARK_THEME_DEFAULT_FONT_SIZE:
            case Constants.APP_DARK_THEME_MEDIUM_FONT_SIZE:
            case Constants.APP_DARK_THEME_LARGE_FONT_SIZE:
                return true;
            default:
                return false;
        }
    }

    public static String getSavedTheme(){
        UserPreferences userPreferences = UserPreferences.getInstance();
        return userPreferences.getUserPreferenceString(Constants.USER_SELECTED_THEME);
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

    public static boolean emailValidation(String email){
        if(email.isEmpty()) {
            return false;
        }else {
            Pattern pattern = Patterns.EMAIL_ADDRESS;
            return pattern.matcher(email.trim()).matches();
        }
    }

}
