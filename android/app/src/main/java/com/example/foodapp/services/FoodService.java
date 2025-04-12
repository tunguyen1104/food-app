package com.example.foodapp.services;

import com.example.foodapp.dto.response.ApiResponse;
import com.example.foodapp.dto.response.CategoryDto;
import com.example.foodapp.dto.response.FoodDto;
import com.example.foodapp.dto.response.IngredientDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface FoodService {
    @GET("/api/food")
    Call<ApiResponse<List<FoodDto>>> getFoods();

    @GET("/api/food/{id}")
    Call<ApiResponse<FoodDto>> getFoodDetail(@Path("id") Long id);

    @GET("/api/category")
    Call<ApiResponse<List<CategoryDto>>> getAllCategories();

    @GET("/api/ingredients")
    Call<ApiResponse<List<IngredientDto>>> getAllIngredients();

    @PUT("/api/admin/foods/{id}")
    Call<FoodDto> updateFoodItem(@Path("id") Long id, @Body FoodDto foodDto);

    @POST("/api/admin/foods")
    Call<FoodDto> addFoodItem(@Body FoodDto foodDto);

    @DELETE("/api/admin/foods/{id}")
    Call<FoodDto> deleteFoodItem(@Path("id") Long id);

}
