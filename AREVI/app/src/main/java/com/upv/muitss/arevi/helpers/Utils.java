package com.upv.muitss.arevi.helpers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import com.upv.muitss.arevi.R;

import java.lang.reflect.Field;

public class Utils {
    private static final String TAG = "Utils";



    public static void saveTheme(Activity context, String value) {
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

    /**
     * Using reflection to override default typeface
     * NOTICE: DO NOT FORGET TO SET TYPEFACE FOR APP THEME AS DEFAULT TYPEFACE WHICH WILL BE OVERRIDDEN
     * @param context to work with assets
     * @param defaultFontNameToOverride for example "monospace"
     * @param customFontFileNameInAssets file name of the font from assets
     */
    public static void overrideFont(Context context, String defaultFontNameToOverride, String customFontFileNameInAssets) {
        try {
            final Typeface customFontTypeface = Typeface.createFromAsset(context.getAssets(), customFontFileNameInAssets);

            final Field defaultFontTypefaceField = Typeface.class.getDeclaredField(defaultFontNameToOverride);
            defaultFontTypefaceField.setAccessible(true);
            defaultFontTypefaceField.set(null, customFontTypeface);
        } catch (Exception e) {
            Log.d(TAG ,"Can not set custom font " + customFontFileNameInAssets + " instead of " + defaultFontNameToOverride);
        }
    }
}
