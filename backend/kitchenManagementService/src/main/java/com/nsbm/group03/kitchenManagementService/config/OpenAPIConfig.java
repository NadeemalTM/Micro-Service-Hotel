package com.nsbm.group03.kitchenManagementService.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI kitchenServiceAPI() {
        return new OpenAPI().info(new Info()
                .title("Kitchen Management Service API")
                .description("API for managing kitchen orders, preparation, and chef assignments")
                .version("1.0.0"));
    }
}
