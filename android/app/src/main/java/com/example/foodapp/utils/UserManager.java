package com.example.foodapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.foodapp.dto.response.UserResponse;
import com.google.gson.Gson;

public class UserManager {
    private static final String PREF_NAME = "user_pref";
    private static final String KEY_USER_JSON = "user_json";

    public static void saveUser(Context context, UserResponse user) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_USER_JSON, new Gson().toJson(user)).apply();
    }

    public static UserResponse getUser(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_USER_JSON, null);
        if (json == null) return null;
        return new Gson().fromJson(json, UserResponse.class);
    }

    public static void clearUser(Context context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit().clear().apply();
    }
}
