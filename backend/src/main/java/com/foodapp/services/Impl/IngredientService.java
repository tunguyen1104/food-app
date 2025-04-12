package com.foodapp.services.Impl;

import com.foodapp.dto.food.IngredientDto;
import com.foodapp.mapper.IngredientMapper;
import com.foodapp.repositories.IngredientRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IngredientService {
    IngredientRepository ingredientRepository;
    IngredientMapper ingredientMapper;

    public List<IngredientDto> getAllFoodIngredients() {
        return ingredientRepository.findAll()
                .stream()
                .map(ingredientMapper::toIngredientDto)
                .toList();
    }
}
