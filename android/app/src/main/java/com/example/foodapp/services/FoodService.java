package com.example.foodapp.services;

import com.example.foodapp.dto.response.ApiResponse;
import com.example.foodapp.dto.response.FoodDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface FoodService {
    @GET("/api/food")
    Call<ApiResponse<List<FoodDto>>> getFoods();

}
