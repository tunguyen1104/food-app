package com.example.foodapp.repositories;

import android.content.Context;

import com.example.foodapp.dto.response.ApiResponse;
import com.example.foodapp.dto.response.FoodDto;
import com.example.foodapp.network.ApiClient;
import com.example.foodapp.services.FoodService;

import org.json.JSONObject;

import java.util.List;

import lombok.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeRepository {
    private final Context context;
    private final AuthRepository authRepository;
    private final FoodService foodService;

    public HomeRepository(Context context) {
        this.context = context;
        this.authRepository = new AuthRepository(context);
        this.foodService = ApiClient.getClient(context).create(FoodService.class);
    }

    public void getFoodData(final HomeCallback<List<FoodDto>> callback) {
        Call<ApiResponse<List<FoodDto>>> call = foodService.getFoods();
        call.enqueue(new Callback<ApiResponse<List<FoodDto>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<FoodDto>>> call, @NonNull Response<ApiResponse<List<FoodDto>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    System.out.println(response.body().getData());
                    callback.onSuccess(response.body().getData());
                } else {
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
                    callback.onError("Loading data failed:" + errorMessage);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<FoodDto>>> call, @NonNull Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public interface HomeCallback<T> {
        void onSuccess(T data);

        void onError(String message);
    }
}
