package ru.chainichek.neostudy.calculator.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.util.List;

@Configuration
@OpenAPIDefinition
public class OpenAPIConfig {
    private static final String APP_VERSION = "1.0.0";
    private static final String APP_NAME = "Microservice 'Calculator'";
    private static final String APP_DESCRIPTION = """
            Микросервис 'Калькулятор' в составе приложения 'Кредитный банк', предназначенный для расчёта кредитных приложений и графика кредитования
            """;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info().title("%sv %s".formatted(APP_VERSION, APP_NAME))
                        .version(APP_VERSION)
                        .description("%s - %s".formatted(APP_NAME, APP_DESCRIPTION)))
                .servers(
                        List.of(new Server().url("http://localhost:8080")
                                .description("Local service")
                        ));
    }
}
