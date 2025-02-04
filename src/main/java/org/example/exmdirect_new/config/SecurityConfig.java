package org.example.exmdirect_new.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Отключение CSRF для Swagger
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/swagger-ui/**",     // Swagger UI
                                "/v3/api-docs/**",    // OpenAPI JSON
                                "/swagger-ui.html"    // Дополнительный путь
                        ).permitAll()
                        .anyRequest().authenticated() // Остальные запросы требуют аутентификации
                );
        return http.build();
    }
}