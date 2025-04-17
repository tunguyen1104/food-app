package com.foodapp.utils;


import com.foodapp.domain.*;
import com.foodapp.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final IngredientRepository ingredientRepository;
    private final FoodRepository foodRepository;
    private final CategoryRepository categoryRepository;
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) {
        // Clear existing data (for testing, comment out in production)
//        orderDetailRepository.deleteAll();
//        orderRepository.deleteAll();
//        userRepository.deleteAll();
//        foodRepository.deleteAll();
//        categoryRepository.deleteAll();
//        ingredientRepository.deleteAll();
//        roleRepository.deleteAll();
//        seedData();
    }
    public void seedData(){
        if (roleRepository.findByName(Role.MANAGER).isEmpty()) {
            roleRepository.saveAndFlush(new Role(Role.MANAGER));
        }
        if (roleRepository.findByName(Role.EMPLOYEE).isEmpty()) {
            roleRepository.saveAndFlush(new Role(Role.EMPLOYEE));
        }
        roleRepository.flush();
        Role managerRole = roleRepository.findByName(Role.MANAGER)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        User user = User.builder()
                .userName("admin")
                .avatarUrl(null)
                .phone("0123456789")
                .role(managerRole)
                .enabled(true)
                .fullName("Admin Account")
                .password(passwordEncoder.encode("123456789asdF@"))
                .build();
        userRepository.save(user);

        // Insert Food Categories
        Category banhMiCategory = categoryRepository.save(new Category("Bánh Mì", ""));
        Category drinkCategory = categoryRepository.save(new Category("Đồ Uống", ""));

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
                20000.0,
                null,
                banhMiCategory,
                List.of(bread, ham, pate, butter, cucumber, cabbage, chili)
        );

        Food banhMiChaLua = new Food(
                "Bánh Mì Chả Lụa",
                "Bánh mì kẹp chả lụa thơm ngon với dưa leo",
                18000.0,
                null,
                banhMiCategory,
                List.of(bread, ham, butter, cucumber, cabbage)
        );

        Food banhMiPate = new Food(
                "Bánh Mì Pate",
                "Bánh mì kết hợp pate béo ngậy với dưa leo và rau mùi.",
                15000.0,
                null,
                banhMiCategory,
                List.of(bread, pate, butter, cucumber, cabbage, chili)
        );

        Food nuocChanh = new Food(
                "Nước Chanh",
                "Nước chanh đánh đá",
                15000.0,
                null,
                drinkCategory,
                null
        );

        foodRepository.saveAll(List.of(banhMiThit, banhMiChaLua, banhMiPate, nuocChanh));
        // Seed Orders
        Order order1 = new Order(Order.Status.COMPLETED, Order.Platform.ONLINE, 40000.0, Timestamp.valueOf("2025-04-01 12:00:00"), "Takeaway",user);
        Order order2 = new Order(Order.Status.COMPLETED, Order.Platform.OFFLINE, 36000.0, Timestamp.valueOf("2025-04-01 13:00:00"), "Dine-in", user);
        Order order3 = new Order(Order.Status.COMPLETED, Order.Platform.ONLINE, 30000.0, Timestamp.valueOf("2025-04-02 14:00:00"), "Delivery", user);
        Order order4 = new Order(Order.Status.PROCESSING, Order.Platform.OFFLINE, 15000.0, Timestamp.valueOf("2025-04-02 15:00:00"), "Takeaway", user);
        Order order5 = new Order(Order.Status.COMPLETED, Order.Platform.ONLINE, 53000.0, Timestamp.valueOf("2025-04-03 16:00:00"), "Delivery", user);
        Order order6 = new Order(Order.Status.COMPLETED, Order.Platform.OFFLINE, 33000.0, Timestamp.valueOf("2025-04-05 17:00:00"), "Dine-in", user);
        Order order7 = new Order(Order.Status.COMPLETED, Order.Platform.ONLINE, 20000.0, Timestamp.valueOf("2025-04-05 18:00:00"), "Takeaway", user);
        Order order8 = new Order(Order.Status.COMPLETED, Order.Platform.OFFLINE, 15000.0, Timestamp.valueOf("2025-04-10 19:00:00"), "Delivery", user);
        Order order9 = new Order(Order.Status.COMPLETED, Order.Platform.ONLINE, 50000.0, Timestamp.valueOf("2025-04-15 20:00:00"), "Takeaway", user);
        Order order10 = new Order(Order.Status.CANCELLED, Order.Platform.OFFLINE, 18000.0, Timestamp.valueOf("2025-04-20 21:00:00"), "Dine-in", user);

        orderRepository.saveAll(Arrays.asList(order1, order2, order3, order4, order5, order6, order7, order8, order9, order10));

        // Seed Order Details
        order1.addOrderDetail(new OrderDetail(2, order1, banhMiThit)); // 2 Bánh Mì Thịt = 40000
        order2.addOrderDetail(new OrderDetail(2, order2, banhMiChaLua)); // 2 Bánh Mì Chả Lụa = 36000
        order3.addOrderDetail(new OrderDetail(2, order3, banhMiPate)); // 2 Bánh Mì Pate = 30000
        order4.addOrderDetail(new OrderDetail(1, order4, nuocChanh)); // 1 Nước Chanh = 15000
        order5.addOrderDetail(new OrderDetail(1, order5, banhMiThit)); // 1 Bánh Mì Thịt = 20000
        order5.addOrderDetail(new OrderDetail(1, order5, banhMiChaLua)); // 1 Bánh Mì Chả Lụa = 18000
        order5.addOrderDetail(new OrderDetail(1, order5, nuocChanh)); // 1 Nước Chanh = 15000
        order6.addOrderDetail(new OrderDetail(1, order6, banhMiPate)); // 1 Bánh Mì Pate = 15000
        order6.addOrderDetail(new OrderDetail(1, order6, nuocChanh)); // 1 Nước Chanh = 15000
        order7.addOrderDetail(new OrderDetail(1, order7, banhMiThit)); // 1 Bánh Mì Thịt = 20000
        order8.addOrderDetail(new OrderDetail(1, order8, banhMiPate)); // 1 Bánh Mì Pate = 15000
        order9.addOrderDetail(new OrderDetail(2, order9, banhMiThit)); // 2 Bánh Mì Thịt = 40000
        order9.addOrderDetail(new OrderDetail(1, order9, nuocChanh)); // 1 Nước Chanh = 15000
        order10.addOrderDetail(new OrderDetail(1, order10, banhMiChaLua)); // 1 Bánh Mì Chả Lụa = 18000

        orderRepository.saveAll(Arrays.asList(order1, order2, order3, order4, order5, order6, order7, order8, order9, order10));
    }
}
