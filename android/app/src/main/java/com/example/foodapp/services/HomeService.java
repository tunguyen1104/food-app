package com.example.foodapp.services;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface HomeService {
    @GET("/api/food/all")
    Call<?> getHomeData(@Header("Authorization") String authHeader);
}
