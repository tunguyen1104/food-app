package com.example.foodapp.listeners;

import com.example.foodapp.dto.response.FoodDto;

public interface CreateOrderAdapterListener {
    void onItemCheckedChange(FoodDto item, boolean isChecked);

    void onQuantityChange(FoodDto item, int quantity);
}
