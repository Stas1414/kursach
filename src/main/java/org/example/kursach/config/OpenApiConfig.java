package org.example.kursach.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "MDM Web Platform API",
                version = "1.0.0",
                description = """
                        REST‑API курсового проекта по централизованному управлению мастер‑данными (MDM).
                        
                        Функциональность:
                        - управление доменами мастер‑данных;
                        - CRUD по мастер‑записям и их статусам (DRAFT/IN_REVIEW/APPROVED/PUBLISHED);
                        - правила и инциденты качества данных;
                        - задачи согласования (workflow) и публикация мастер‑данных во внешние системы;
                        - интеграционные каналы;
                        - аудит и дашборд;
                        - управление пользователями и ролями.
                        """,
                contact = @Contact(
                        name = "MDM System Administrator",
                        email = "admin@corp.local"
                ),
                license = @License(name = "Educational use only")
        ),
        security = @SecurityRequirement(name = "basicAuth"),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local Docker / Dev")
        }
)
@SecurityScheme(
        name = "basicAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "basic",
        description = "Basic Authentication. Используйте admin/admin123 или другого пользователя системы."
)
public class OpenApiConfig {

        @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("http://localhost:5173")); 
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}


