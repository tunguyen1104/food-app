package com.example.foodapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private final Context context;

    public AuthInterceptor(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request.Builder requestBuilder = originalRequest.newBuilder();
        String accessToken = getAccessToken();
        if (accessToken != null && !accessToken.isEmpty()) {
            requestBuilder.header("Authorization", "Bearer " + accessToken);
        }
        Response response = chain.proceed(requestBuilder.build());
        if (response.code() == 401) {
            // Attempt to refresh token
            String newAccessToken = refreshToken();
            if (newAccessToken != null) {
                // Retry request with new token
                requestBuilder.header("Authorization", "Bearer " + newAccessToken);
                return chain.proceed(requestBuilder.build());
            }
        }
        return response;
    }

    private String getAccessToken() {
        try {
            SharedPreferences prefs = EncryptedSharedPreferences.create(
                    "auth",
                    MasterKey.DEFAULT_MASTER_KEY_ALIAS,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
            return prefs.getString("access_token", null);
        } catch (Exception e) {
            e.printStackTrace();
            // Fallback to regular SharedPreferences
            return context.getSharedPreferences("auth", Context.MODE_PRIVATE)
                    .getString("access_token", null);
        }
    }

    private String refreshToken() {
        // Implement refresh token logic using AuthRepository
        // Return new access token or null if refresh fails
        return null;
    }
}
