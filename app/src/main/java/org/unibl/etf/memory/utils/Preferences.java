package org.unibl.etf.memory.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    private static final String PREFERENCES_FILE_NAME = "memory-preferences";
    private static final String LANG_KEY = "memory-lang";
    private static final String MUSIC_KEY = "memory-music";

    public static void changeLanguage(Context context, String newLanguage){
        SharedPreferences.Editor editor=getSharedPreferencesEditor(context);
        editor.putString(LANG_KEY, newLanguage);
        editor.apply();
    }

    public static void changeIsMusicPlaying(Context context, Boolean isPlaying){
        SharedPreferences.Editor editor=getSharedPreferencesEditor(context);
        editor.putBoolean(MUSIC_KEY, isPlaying);
        editor.apply();
    }


    public static String getLanguage(Context context){
        SharedPreferences sharedPreferences=getSharedPreferences(context);
        return sharedPreferences.getString(LANG_KEY, "en");
    }

    public static Boolean getIsMusicPlaying(Context context){
        SharedPreferences sharedPreferences=getSharedPreferences(context);
        return sharedPreferences.getBoolean(MUSIC_KEY, true);
    }

    private static SharedPreferences.Editor getSharedPreferencesEditor(Context context){
        SharedPreferences sharedPreferences=getSharedPreferences(context);
        return sharedPreferences.edit();
    }

    private static SharedPreferences getSharedPreferences(Context context){
        return context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
    }
}
