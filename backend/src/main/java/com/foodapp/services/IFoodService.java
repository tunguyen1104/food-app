package com.foodapp.services;

import com.foodapp.dto.food.CategoryDto;
import com.foodapp.dto.food.FoodDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface IFoodService {
    List<CategoryDto> getCategories();

    Map<String, List<FoodDto>> getAllFoodByCategory();
}
