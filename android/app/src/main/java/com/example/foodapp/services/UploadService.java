package com.example.foodapp.services;

import com.example.foodapp.dto.response.ApiResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadService {
    @Multipart
    @POST("/api/upload")
    Call<ApiResponse<String>> uploadImage(@Part MultipartBody.Part file);
}
