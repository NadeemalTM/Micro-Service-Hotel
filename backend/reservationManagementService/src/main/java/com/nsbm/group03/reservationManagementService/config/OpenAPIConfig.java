package com.nsbm.group03.reservationManagementService.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI reservationServiceAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Reservation Management Service API")
                        .description("API for managing hotel reservations, check-ins, check-outs, and guest bookings")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Hotel Management System")
                                .email("support@hotel.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}
