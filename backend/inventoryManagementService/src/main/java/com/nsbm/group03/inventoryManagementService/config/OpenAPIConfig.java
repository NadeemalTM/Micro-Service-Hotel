package com.nsbm.group03.inventoryManagementService.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI inventoryServiceAPI() {
        return new OpenAPI().info(new Info()
                .title("Inventory Management Service API")
                .description("API for managing hotel inventory, stock levels, and reorders")
                .version("1.0.0"));
    }
}
