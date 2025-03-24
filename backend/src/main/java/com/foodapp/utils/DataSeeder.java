package com.foodapp.utils;


import com.foodapp.domain.Role;
import com.foodapp.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {
    private final RoleRepository roleRepository;

    public DataSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role(Role.MANAGER));
            roleRepository.save(new Role(Role.EMPLOYEE));
        }
    }
}
