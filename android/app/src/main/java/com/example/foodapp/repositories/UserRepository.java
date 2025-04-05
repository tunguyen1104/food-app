package com.example.foodapp.repositories;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.foodapp.dto.request.CreateUserRequest;
import com.example.foodapp.dto.response.ApiResponse;
import com.example.foodapp.dto.response.UserResponse;
import com.example.foodapp.network.ApiClient;
import com.example.foodapp.services.UserService;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {

    private final Context context;
    private final UserService userService;

    public UserRepository(Context context) {
        this.context = context;
        this.userService = ApiClient.getClient(context).create(UserService.class);
    }

    public void getUserProfile(final UserProfileCallback callback) {
        Call<ApiResponse<UserResponse>> call = userService.getUserProfile();

        call.enqueue(new Callback<ApiResponse<UserResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<UserResponse>> call, @NonNull Response<ApiResponse<UserResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onSuccess(response.body().getData());
                } else {
                    callback.onError(parseError(response));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<UserResponse>> call, @NonNull Throwable t) {
                callback.onError("Connect error: " + t.getMessage());
            }
        });
    }

    public void getAllUsers(final UserListCallback callback) {
        Call<ApiResponse<List<UserResponse>>> call = userService.getAllUsers();

        call.enqueue(new Callback<ApiResponse<List<UserResponse>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<UserResponse>>> call, @NonNull Response<ApiResponse<List<UserResponse>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onSuccess(response.body().getData());
                } else {
                    callback.onError(parseError(response));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<UserResponse>>> call, @NonNull Throwable t) {
                callback.onError("Connect error: " + t.getMessage());
            }
        });
    }

    public void createUser(CreateUserRequest request, final CreateUserCallback callback) {
        Call<ApiResponse<UserResponse>> call = userService.createUser(request);
        call.enqueue(new Callback<ApiResponse<UserResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<UserResponse>> call, @NonNull Response<ApiResponse<UserResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onSuccess(response.body().getData());
                } else {
                    callback.onError(parseError(response));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<UserResponse>> call, @NonNull Throwable t) {
                callback.onError("Connect error: " + t.getMessage());
            }
        });
    }

    private String parseError(Response<?> response) {
        String errorMessage = "Failed";
        if (response.errorBody() != null) {
            try {
                String errorJson = response.errorBody().string();
                JSONObject jsonObject = new JSONObject(errorJson);
                if (jsonObject.has("message")) {
                    errorMessage = jsonObject.getString("message");
                }
            } catch (Exception e) {
                errorMessage = "Error reading error response from server";
            }
        }
        return errorMessage;
    }

    // Callbacks

    public interface UserProfileCallback {
        void onSuccess(UserResponse user);
        void onError(String message);
    }

    public interface UserListCallback {
        void onSuccess(List<UserResponse> users);
        void onError(String message);
    }

    public interface CreateUserCallback {
        void onSuccess(UserResponse newUser);
        void onError(String errorMessage);
    }
}
