package com.example.foodapp.repositories;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.example.foodapp.activities.LoginActivity;
import com.example.foodapp.dto.request.LoginRequest;
import com.example.foodapp.dto.request.RefreshTokenRequest;
import com.example.foodapp.dto.response.ApiResponse;
import com.example.foodapp.dto.response.AuthResponse;
import com.example.foodapp.network.ApiClient;
import com.example.foodapp.services.AuthService;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {
    private final Context context;
    private final SharedPreferences prefs;
    private final AuthService authService;
    private String refreshToken;

    public AuthRepository(Context context) {
        this.context = context.getApplicationContext();
        this.prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE);
        authService = ApiClient.getClient(context).create(AuthService.class);
        loadTokens();
    }

    public void loginUser(String phoneNumber, String password, final LoginCallback callback) {
        LoginRequest loginRequest = new LoginRequest(phoneNumber, password);
        Call<ApiResponse<AuthResponse>> call = authService.login(loginRequest);

        call.enqueue(new Callback<ApiResponse<AuthResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<AuthResponse>> call, @NonNull Response<ApiResponse<AuthResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<AuthResponse> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        callback.onSuccess(response.body().getData());
                    }
                } else {
                    // Try to extract error message from response body
                    String errorMessage = "Login failed: " + response.code();
                    if (response.errorBody() != null) {
                        try {
                            String errorJson = response.errorBody().string();
                            // Assuming your API returns error messages in a JSON format like: {"message": "Invalid credentials"}
                            JSONObject jsonObject = new JSONObject(errorJson);
                            if (jsonObject.has("message")) {
                                errorMessage = jsonObject.getString("message");
                            }
                        } catch (Exception e) {
                            errorMessage = "Error parsing response";
                        }
                    }
                    callback.onError("Login failed:" + errorMessage);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<AuthResponse>> call, @NonNull Throwable t) {
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
        authService.refreshToken(request).enqueue(new Callback<ApiResponse<AuthResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<AuthResponse>> call, @NonNull Response<ApiResponse<AuthResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    saveTokens(response.body().getData().getAccessToken(), response.body().getData().getRefreshToken());
                    ApiClient.updateAccessToken(response.body().getData().getAccessToken());
                    callback.onSuccess(response.body().getData().getAccessToken());
                } else {
                    forceLogout();
                    callback.onFailure();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<AuthResponse>> call, @NonNull Throwable t) {
                forceLogout();
                callback.onFailure();
            }
        });
    }

    private void saveTokens(String accessToken, String refreshToken) {
        prefs.edit()
                .putString("access_token", accessToken)
                .putString("refresh_token", refreshToken)
                .apply();
        this.refreshToken = refreshToken;
    }

    public void forceLogout() {
        prefs.edit().clear().apply();

        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    private void loadTokens() {
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
