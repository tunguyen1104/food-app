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

    public void updateUser(Long id, CreateUserRequest request, final UpdateUserCallback callback) {
        Call<ApiResponse<UserResponse>> call = userService.updateUser(id, request);
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

    public void deleteUser(Long id, final DeleteUserCallback callback) {
        Call<ApiResponse<Void>> call = userService.deleteUser(id);
        call.enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<Void>> call, @NonNull Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onSuccess("Account deleted successfully!");
                } else {
                    callback.onError(parseError(response));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<Void>> call, @NonNull Throwable t) {
                callback.onError("Connect error: " + t.getMessage());
            }
        });
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

    public interface UpdateUserCallback {
        void onSuccess(UserResponse updatedUser);

        void onError(String errorMessage);
    }

    public interface DeleteUserCallback {
        void onSuccess(String message);

        void onError(String errorMessage);
    }
}
