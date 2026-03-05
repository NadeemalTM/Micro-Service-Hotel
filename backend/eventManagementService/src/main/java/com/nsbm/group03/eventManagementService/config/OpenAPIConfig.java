package com.nsbm.group03.eventManagementService.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI eventServiceAPI() {
        return new OpenAPI().info(new Info()
                .title("Event Management Service API")
                .description("API for managing hotel events, bookings, and venue coordination")
                .version("1.0.0"));
    }
}
