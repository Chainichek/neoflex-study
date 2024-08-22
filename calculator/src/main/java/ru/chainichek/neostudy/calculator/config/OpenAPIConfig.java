package ru.chainichek.neostudy.calculator.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Configuration
@OpenAPIDefinition
public class OpenAPIConfig {
    private static final String APP_VERSION = "1.1.0";
    private static final String APP_NAME = "MS 'Calculator'";
    private static final String APP_DESCRIPTION = """
            The MS 'Calculator' within the 'Credit Bank' application is designed for calculating loan offers and repayment schedules.
            """;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info().title(APP_NAME)
                        .version(APP_VERSION)
                        .description("%s - %s".formatted(APP_NAME, APP_DESCRIPTION)));
    }
}
