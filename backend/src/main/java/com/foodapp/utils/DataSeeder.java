package com.foodapp.utils;


import com.foodapp.domain.Category;
import com.foodapp.domain.Food;
import com.foodapp.domain.Ingredient;
import com.foodapp.domain.Role;
import com.foodapp.repositories.CategoryRepository;
import com.foodapp.repositories.FoodRepository;
import com.foodapp.repositories.IngredientRepository;
import com.foodapp.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final IngredientRepository ingredientRepository;
    private final FoodRepository foodRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public void run(String... args) {
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role(Role.MANAGER));
            roleRepository.save(new Role(Role.EMPLOYEE));
        }
        if (ingredientRepository.count() == 0 && foodRepository.count() == 0) {
            // Insert Food Categories
            Category banhMiCategory = new Category("Bánh Mì");
            Category drinkCategory = new Category("Đồ Uống");
            categoryRepository.saveAll(List.of(banhMiCategory, drinkCategory));

            // Insert Ingredients for Bánh Mì
            Ingredient bread = new Ingredient("Bánh Mì", null);
            Ingredient pork = new Ingredient("Thịt Heo", null);
            Ingredient cucumber = new Ingredient("Dưa Chuột", null);
            Ingredient chili = new Ingredient("Tương Ớt", null);
            Ingredient pate = new Ingredient("Pate", null);
            Ingredient butter = new Ingredient("Bơ", null);
            Ingredient ham = new Ingredient("Giăm Bông", null);
            Ingredient cabbage = new Ingredient("Cải Bắp", null);
            ingredientRepository.saveAll(List.of(bread, pork, cucumber, chili, pate, butter, ham, cabbage));

            // Insert Foods
            Food banhMiThit = new Food(
                    "Bánh Mì Thịt",
                    "Bánh mì giòn rụm kết hợp với thịt nguội, pate, bơ và salad",
                    new BigDecimal("20000"),
                    null,
                    banhMiCategory,
                    List.of(bread, ham, pate, butter, cucumber, cabbage, chili)
            );

            Food banhMiChaLua = new Food(
                    "Bánh Mì Chả Lụa",
                    "Bánh mì kẹp chả lụa thơm ngon với dưa leo",
                    new BigDecimal("18000"),
                    null,
                    banhMiCategory,
                    List.of(bread, ham, butter, cucumber, cabbage)
            );

            Food banhMiPate = new Food(
                    "Bánh Mì Pate",
                    "Bánh mì kết hợp pate béo ngậy với dưa leo và rau mùi.",
                    new BigDecimal("15000"),
                    null,
                    banhMiCategory,
                    List.of(bread, pate, butter, cucumber, cabbage, chili)
            );

            Food nuocChanh = new Food(
                    "Nước Chanh",
                    "Nước chanh đánh đá",
                    new BigDecimal("15000"),
                    null,
                    drinkCategory,
                    null
            );

            foodRepository.saveAll(List.of(banhMiThit, banhMiChaLua, banhMiPate, nuocChanh));

            System.out.println("✅ Sample Bánh Mì data inserted successfully!");
        }
    }
}
