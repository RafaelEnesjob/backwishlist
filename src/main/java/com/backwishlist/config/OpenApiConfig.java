package com.backwishlist.config;


import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.*;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI wishlistApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Wishlist API - LuizaLabs Case")
                        .version("v1")
                        .description("API for managing customer wish lists")
                        .contact(new Contact()
                                .name("Rafael Enes")
                                .email("rafaelenes19@gmail.com")
                                .url("https://github.com/RafaelEnesjob"))
                        .license(new License()
                                .name("Licença MIT")
                                .url("https://opensource.org/licenses/MIT")))
                .addServersItem(new Server()
                        .url("http://localhost:8080")
                        .description("Local environment"));
    }
}