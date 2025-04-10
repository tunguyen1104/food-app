package com.foodapp.services.Impl;

import com.foodapp.constants.ErrorCode;
import com.foodapp.domain.Category;
import com.foodapp.domain.Food;
import com.foodapp.dto.requests.FoodRequest;
import com.foodapp.dto.response.FoodResponse;
import com.foodapp.exceptions.AppException;
import com.foodapp.mapper.FoodMapper;
import com.foodapp.repositories.FoodRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FoodService {
    FoodRepository foodRepository;
    CategoryService categoryService;
    FoodMapper foodMapper;

    @Transactional(readOnly = true)
    public List<FoodResponse> getFoods() {
        List<Food> foods = foodRepository.findAll();
        return foods.stream()
                .map(foodMapper::toFoodResponse)
                .collect(Collectors.toList());
    }

    public Food getFoodEntityById(Long id) {
        return foodRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.FOOD_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public FoodResponse getFoodById(Long id) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.FOOD_NOT_FOUND));
        return foodMapper.toFoodResponse(food);
    }

    @Transactional
    public FoodResponse createFood(FoodRequest foodRequest) {
        Category category = categoryService.getCategoryEntityById(foodRequest.getCategoryId());

        Food food = Food.builder()
                .foodName(foodRequest.getFoodName())
                .price(foodRequest.getPrice())
                .avatarUrl(foodRequest.getAvatarUrl())
                .description(foodRequest.getDescription())
                .category(category)
                .build();

        foodRepository.save(food);

        return foodMapper.toFoodResponse(food);
    }

    @Transactional
    public FoodResponse updateFood(Long id, FoodRequest foodRequest) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.FOOD_NOT_FOUND));

        Category category = categoryService.getCategoryEntityById(foodRequest.getCategoryId());

        food.setFoodName(foodRequest.getFoodName());
        food.setPrice(foodRequest.getPrice());
        food.setDescription(foodRequest.getDescription());
        food.setAvatarUrl(foodRequest.getAvatarUrl());
        food.setCategory(category);

        foodRepository.save(food);

        return foodMapper.toFoodResponse(food);
    }

    @Transactional
    public void deleteFood(Long id) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.FOOD_NOT_FOUND));
        foodRepository.delete(food);
    }
}
