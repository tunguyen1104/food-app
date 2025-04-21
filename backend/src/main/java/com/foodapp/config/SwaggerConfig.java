package com.foodapp.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private SecurityScheme createJwtScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");
    }

    private License createLicense() {
        return new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");
    }

    private Info createApiInfo() {
        return new Info()
                .title("Food App API - Swagger Documentation")
                .version("1.0.0")
                .description("This API provides endpoints for managing the FoodApp system, including authentication, orders, messaging, and more.")
                .license(createLicense());
    }

    @Bean
    public OpenAPI foodAppOpenAPI() {
        return new OpenAPI()
                .info(createApiInfo())
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", createJwtScheme()));
    }
}
