package com.emazon.users.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.stream.Stream;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI(
            @Value("${app.description}") String description,
            @Value("${app.title}") String title,
            @Value("${app.version}") String version

            ) {
        return new OpenAPI().info(new Info().title(title).version(version).description(description));
    }

}
