package com.example.foodapp.repositories;

import android.content.Context;

import com.example.foodapp.dto.response.ApiResponse;
import com.example.foodapp.network.ApiClient;
import com.example.foodapp.services.UploadService;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadRepository {
    private final UploadService uploadService;

    public UploadRepository(Context context) {
        this.uploadService = ApiClient.getClient(context).create(UploadService.class);
    }

    public void uploadImage(File file, UploadCallback callback) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        uploadService.uploadImage(body).enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onSuccess(response.body().getData());
                } else {
                    callback.onError("Upload failed");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                callback.onError("Upload error: " + t.getMessage());
            }
        });
    }

    public interface UploadCallback {
        void onSuccess(String imageUrl);
        void onError(String errorMessage);
    }
}
