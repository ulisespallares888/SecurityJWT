package com.programandoenjava.jwt.config.swagger;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


// Esta es la ANOTACIÓN para que Swagger UI sepa que se usa JWT
@SecurityScheme(
        name = "bearerAuth", // debe coincidir con el SecurityRequirement
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                // Aplica JWT globalmente a todas las peticiones protegidas
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                // Agrega servidores
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Localhost"),
                        new Server().url("http://127.0.0.1:8080").description("Localhost (IP)"),
                        new Server().url("https://tucompra.com").description("Producción")
                ))
                // Información general de la API
                .info(new Info()
                        .title("Order Service API")
                        .description("Order Service API Description")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Codmind")
                                .url("https://codmind.com")
                                .email("apis@codmind.com")
                        )

                );
    }
}






