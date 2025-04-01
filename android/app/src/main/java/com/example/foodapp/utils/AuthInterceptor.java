package com.example.foodapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private static String accessToken;
    private final Context context;

    public AuthInterceptor(Context context) {
        this.context = context;
        loadTokens();
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request originalRequest = chain.request();

        if (accessToken == null) {
            return chain.proceed(originalRequest);
        }

        Request authenticatedRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .build();

        return chain.proceed(authenticatedRequest);
    }

    private void loadTokens() {
        SharedPreferences prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE);
        accessToken = prefs.getString("access_token", null);
    }

    public void updateAccessToken(String newAccessToken) {
        accessToken = newAccessToken;
        SharedPreferences prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE);
        prefs.edit().putString("access_token", newAccessToken).apply();
    }
}
