package org.example.exmdirect_new.config;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.parameters.Parameter;

import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.headers.Header;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("EXMDirect API").version("1.0"))
                .addServersItem(new Server().url("https://ee13-112-72-13-26.ngrok-free.app"));
    }

    @Bean
    public OpenApiCustomizer addNgrokHeaderCustomizer() {
        return openApi -> {
            Parameter ngrokHeader = new Parameter()
                    .in(ParameterIn.HEADER.toString())
                    .name("ngrok-skip-browser-warning")
                    .required(false)
                    .example("true")
                    .description("Bypass ngrok browser warning")
                    .schema(new io.swagger.v3.oas.models.media.StringSchema());

            openApi.getPaths().values().forEach(pathItem -> {
                pathItem.readOperations().forEach(operation ->
                        operation.addParametersItem(ngrokHeader));
            });
        };
    }
}
