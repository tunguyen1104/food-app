package com.example.foodapp.viewmodel.home;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodapp.dto.response.ApiResponse;
import com.example.foodapp.dto.response.CategoryDto;
import com.example.foodapp.dto.response.FoodDto;
import com.example.foodapp.dto.response.IngredientDto;
import com.example.foodapp.repositories.FoodRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodViewModel extends ViewModel {
    private final FoodRepository foodRepository;

    private final MutableLiveData<List<FoodDto>> foodData = new MutableLiveData<>();
    private final MutableLiveData<List<CategoryDto>> foodCategories = new MutableLiveData<>();
    private final MutableLiveData<List<IngredientDto>> foodIngredients = new MutableLiveData<>();
    private final MutableLiveData<FoodDto> foodDetail = new MutableLiveData<>();

    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> updateSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> updateError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> deleteSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> deleteError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> addSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> addError = new MutableLiveData<>();

    public FoodViewModel(Context context) {
        foodRepository = new FoodRepository(context);
        fetchFoodData();
    }

    public LiveData<Boolean> getUpdateSuccess() {
        return updateSuccess;
    }

    public LiveData<String> getUpdateError() {
        return updateError;
    }

    public LiveData<Boolean> getAddSuccess() {
        return addSuccess;
    }

    public LiveData<String> getAddError() {
        return addError;
    }

    public LiveData<Boolean> getDeleteSuccess() {
        return deleteSuccess;
    }

    public LiveData<String> getDeleteError() {
        return deleteError;
    }

    public LiveData<List<FoodDto>> getFoodData() {
        fetchFoodData();
        return foodData;
    }

    public LiveData<FoodDto> getFoodDetail() {
        return foodDetail;
    }

    public void getFoodDetailById(Long foodId) {
        if (foodId == null) {
            errorMessage.setValue("Food ID cannot be null");
            return;
        }
        fetchFoodDetail(foodId);
    }

    public LiveData<List<CategoryDto>> getFoodCategories() {
        return foodCategories;
    }

    public LiveData<List<IngredientDto>> getFoodIngredients() {
        return foodIngredients;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    private void fetchFoodData() {
        foodRepository.getFoodData(new FoodRepository.HomeCallback<List<FoodDto>>() {
            @Override
            public void onSuccess(List<FoodDto> data) {
                foodData.setValue(data);
            }

            @Override
            public void onError(String message) {
                errorMessage.setValue(message);
            }
        });
    }

    private void fetchFoodDetail(Long foodId) {
        foodRepository.getFoodDetail(foodId).enqueue(new Callback<ApiResponse<FoodDto>>() {
            @Override
            public void onResponse(Call<ApiResponse<FoodDto>> call, Response<ApiResponse<FoodDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    foodDetail.setValue(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<FoodDto>> call, Throwable t) {
                errorMessage.setValue("Fail to get food detail for id: " + foodId);
            }
        });
    }

    public void updateFoodItem(FoodDto foodDto) {
        foodRepository.updateFoodItem(foodDto.getId(), foodDto).enqueue(new Callback<FoodDto>() {
            @Override
            public void onResponse(Call<FoodDto> call, Response<FoodDto> response) {
                if (response.isSuccessful()) {
                    updateSuccess.setValue(true);
                } else {
                    updateError.setValue("Failed to update food item");
                }
            }

            @Override
            public void onFailure(Call<FoodDto> call, Throwable t) {
                updateError.setValue("Error: " + t.getMessage());
            }
        });
    }

    public void addFoodItem(FoodDto foodDto) {
        foodRepository.addFoodItem(foodDto).enqueue(new Callback<FoodDto>() {
            @Override
            public void onResponse(Call<FoodDto> call, Response<FoodDto> response) {
                if (response.isSuccessful()) {
                    addSuccess.setValue(true);
                } else {
                    addError.setValue("Failed to add food item");
                }
            }

            @Override
            public void onFailure(Call<FoodDto> call, Throwable t) {
                addError.setValue("Error: " + t.getMessage());
            }
        });
    }

    public void deleteFoodItem(Long id) {
        foodRepository.deleteFoodItem(id).enqueue(new Callback<FoodDto>() {
            @Override
            public void onResponse(Call<FoodDto> call, Response<FoodDto> response) {
                if (response.isSuccessful()) {
                    deleteSuccess.setValue(true);
                } else {
                    deleteError.setValue("Failed to delete food item");
                }
            }

            @Override
            public void onFailure(Call<FoodDto> call, Throwable t) {
                deleteError.setValue("Error: " + t.getMessage());
            }
        });
    }

    public void getAllFoodCategories() {
        foodRepository.getCategories().enqueue(new Callback<ApiResponse<List<CategoryDto>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<CategoryDto>>> call, Response<ApiResponse<List<CategoryDto>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("FoodViewModel", "Categories: " + response.body().getData());
                    foodCategories.setValue(response.body().getData());
                } else {
                    Log.w("FoodViewModel", "Failed to fetch categories: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<CategoryDto>>> call, Throwable t) {
                errorMessage.setValue("Failed to fetch categories: " + t.getMessage());
            }
        });
    }

    public void getAllFoodIngredients() {
        foodRepository.getAllIngredients().enqueue(new Callback<ApiResponse<List<IngredientDto>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<IngredientDto>>> call, Response<ApiResponse<List<IngredientDto>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    foodIngredients.setValue(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<IngredientDto>>> call, Throwable t) {

            }
        });
    }
}
