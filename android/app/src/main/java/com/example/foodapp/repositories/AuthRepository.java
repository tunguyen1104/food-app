package com.example.foodapp.repositories;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.example.foodapp.activities.LoginActivity;
import com.example.foodapp.dto.request.LoginRequest;
import com.example.foodapp.dto.request.RefreshTokenRequest;
import com.example.foodapp.dto.response.AuthResponse;
import com.example.foodapp.network.ApiClient;
import com.example.foodapp.services.AuthService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {
    private final Context context;
    private final AuthService authService;
    private String refreshToken;

    public AuthRepository(Context context) {
        this.context = context;
        authService = ApiClient.getClient(context).create(AuthService.class);
        loadTokens();
    }

    public void loginUser(String phoneNumber, String password, final LoginCallback callback) {
        LoginRequest loginRequest = new LoginRequest(phoneNumber, password);
        Call<AuthResponse> call = authService.login(loginRequest);

        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(@NonNull Call<AuthResponse> call, @NonNull Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Login failed: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<AuthResponse> call, @NonNull Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void refreshToken(final RefreshCallback callback) {
        if (refreshToken == null) {
            forceLogout();
            callback.onFailure();
            return;
        }

        RefreshTokenRequest request = new RefreshTokenRequest(refreshToken);
        authService.refreshToken(request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(@NonNull Call<AuthResponse> call, @NonNull Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    saveTokens(response.body().getAccessToken(), response.body().getRefreshToken());
                    ApiClient.updateAccessToken(response.body().getAccessToken());
                    callback.onSuccess(response.body().getAccessToken());
                } else {
                    forceLogout();
                    callback.onFailure();
                }
            }

            @Override
            public void onFailure(@NonNull Call<AuthResponse> call, @NonNull Throwable t) {
                forceLogout();
                callback.onFailure();
            }
        });
    }

    private void saveTokens(String accessToken, String refreshToken) {
        SharedPreferences prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE);
        prefs.edit()
                .putString("access_token", accessToken)
                .putString("refresh_token", refreshToken)
                .apply();
        this.refreshToken = refreshToken;
    }

    public void forceLogout() {
        SharedPreferences prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE);
        prefs.edit().clear().apply();

        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    private void loadTokens() {
        SharedPreferences prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE);
        refreshToken = prefs.getString("refresh_token", null);
    }

    public interface RefreshCallback {
        void onSuccess(String newAccessToken);

        void onFailure();
    }

    public interface LoginCallback {
        void onSuccess(AuthResponse loginResponse);

        void onError(String errorMessage);
    }
}
