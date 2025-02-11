package org.example.exmdirect_new.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Разрешение публичного доступа к Swagger
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()

                        // Разрешение доступа к эндпоинтам учителя
                        .requestMatchers("/teachers").permitAll() // Список всех учителей
                        .requestMatchers("/teachers/{id}").permitAll() // Получение учителя по ID
                        .requestMatchers("/teachers/subject/**").permitAll() // Поиск по предмету
                        .requestMatchers("/teachers/upload").permitAll() // Загрузка учителей из файла
                        .requestMatchers("/teachers/update-login").permitAll() // Смена логина и пароля
                        .requestMatchers("/teachers/**").permitAll() // Любые дополнительные эндпоинты учителя

                        // Разрешение доступа к эндпоинтам вопросов
                        .requestMatchers("/questions").permitAll() // Получение всех вопросов
                        .requestMatchers("/questions/{id}").permitAll() // Получение вопроса по ID
                        .requestMatchers("/questions/group/{groupId}").permitAll() // Вопросы по группе
                        .requestMatchers("/questions/upload").permitAll() // Загрузка вопросов из файла
                        .requestMatchers("/questions/**").permitAll() // Любые дополнительные эндпоинты вопросов

                        // Разрешение публичного доступа ко всем запросам
                        .anyRequest().permitAll()
                )
                .httpBasic(Customizer.withDefaults());

        logger.debug("Фильтр безопасности успешно настроен");
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
