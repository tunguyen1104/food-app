package com.example.foodapp.viewmodel.home;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodapp.dto.response.FoodDto;
import com.example.foodapp.repositories.HomeRepository;

import java.util.List;

public class HomeViewModel extends ViewModel {
    private final HomeRepository homeRepository;

    private final MutableLiveData<List<FoodDto>> foodData = new MutableLiveData<>();

    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public HomeViewModel(Context context) {
        homeRepository = new HomeRepository(context);
        fetchFoodData();
    }

    public LiveData<List<FoodDto>> getFoodData() {
        return foodData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    private void fetchFoodData() {
        homeRepository.getFoodData(new HomeRepository.HomeCallback<List<FoodDto>>() {
            @Override
            public void onSuccess(List<FoodDto> data) {
                foodData.setValue(data);
                System.out.println(data);
            }

            @Override
            public void onError(String message) {
                errorMessage.setValue(message);
                System.out.println(message);
            }
        });
    }
}
