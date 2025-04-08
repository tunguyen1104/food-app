package com.foodapp.services.Impl;

import com.foodapp.domain.Category;
import com.foodapp.domain.Food;
import com.foodapp.dto.food.CategoryDto;
import com.foodapp.dto.food.FoodDto;
import com.foodapp.repositories.CategoryRepository;
import com.foodapp.repositories.FoodRepository;
import com.foodapp.services.IFoodService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class FoodService implements IFoodService {
    CategoryRepository categoryRepository;
    FoodRepository foodRepository;

    @Override
    public List<CategoryDto> getCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(this::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, List<FoodDto>> getAllFoodByCategory() {
        Map<String, List<FoodDto>> allFoodByCategory = new HashMap<>();
        foodRepository.findAll()
                .forEach(food -> {
                    String categoryName = food.getCategory().getName();
                    allFoodByCategory
                            .computeIfAbsent(categoryName, k -> new ArrayList<>())
                            .add(toFoodDto(food));
                });
        return allFoodByCategory;
    }

    private CategoryDto toCategoryDto(Category category) {
        return CategoryDto.builder()
                .name(category.getName())
                .build();
    }

    private FoodDto toFoodDto(Food food) {
        return FoodDto
                .builder()
                .id(food.getId())
                .foodName(food.getFoodName())
                .price(food.getPrice())
                .description(food.getDescription())
                .imageUrl(food.getImageUrl())
                .build();
    }
}
