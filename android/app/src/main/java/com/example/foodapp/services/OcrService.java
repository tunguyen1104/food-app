package com.example.foodapp.services;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface OcrService {
    @FormUrlEncoded
    @POST("parse/image")
    Call<ResponseBody> parseImage(
        @Field("base64Image") String base64Image,
        @Field("language") String language,
        @Field("apikey") String apikey
    );
}
