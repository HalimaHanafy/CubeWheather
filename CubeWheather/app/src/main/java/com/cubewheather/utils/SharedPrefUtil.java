package com.cubewheather.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by halimahanafy on 25/07/16.
 */

public class SharedPrefUtil {

    private static Context context;
    private static SharedPrefUtil instance = null;
    private static String preferenceName;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;


    /**
     * Constructor prevents any other class from instantiating.
     */
    private SharedPrefUtil() {
        preferences = context.getSharedPreferences(preferenceName, context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    /**
     * * Make sure that there is only one SharedPrefUtil instance.
     *
     * @param context The android Context instance.
     * @return Returns only one instance of SharedPrefUtil.
     */
    public static SharedPrefUtil getInstance(Context context) {

        SharedPrefUtil.context = context;
        SharedPrefUtil.preferenceName = context.getPackageName();
        if (instance == null) {
            instance = new SharedPrefUtil();
        }
        return instance;
    }


    /**
     * Read boolean preferences
     *
     * @param preferenceName The unique name of preference.
     * @param defaultValue   The value if there is no saved one.
     * @return The value of saved preference.
     */
    public boolean read(String preferenceName, boolean defaultValue) {
        return preferences.getBoolean(preferenceName, defaultValue);
    }

    /**
     * Write boolean preferences.
     *
     * @param preferenceName  The unique name of preference.
     * @param preferenceValue The value to save in preference.
     */
    public void write(String preferenceName, boolean preferenceValue) {

        editor.putBoolean(preferenceName, preferenceValue);
        editor.apply();
    }

}
