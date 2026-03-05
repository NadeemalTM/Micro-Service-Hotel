package com.nsbm.group03.restaurantManagementService.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI restaurantServiceAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Restaurant Management Service API")
                        .description("API for managing restaurant orders, menu items, and table bookings")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Hotel Management System")
                                .email("support@hotel.com")));
    }
}
