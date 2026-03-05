package com.nsbm.group03.roomManagementService.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    
    @Bean
    public OpenAPI roomManagementOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Room Management Service API")
                        .description("REST API for Hotel Room Management System")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("NSBM Group 03")
                                .email("hotel@nsbm.lk")));
    }
}
