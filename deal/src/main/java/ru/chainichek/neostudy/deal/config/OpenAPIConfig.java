package ru.chainichek.neostudy.deal.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.util.List;

@Configuration
@OpenAPIDefinition
public class OpenAPIConfig {
    private static final String APP_VERSION = "v1.0.0";
    private static final String APP_NAME = "MS 'Deal'";
    private static final String APP_DESCRIPTION = """
            Microservice 'Deal' as part of the 'Credit Bank' application, designed for long-term storage of information about deals
            """;

    @Value("${server.port}")
    private String port;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info().title(APP_NAME)
                        .version(APP_VERSION)
                        .description("%s - %s".formatted(APP_NAME, APP_DESCRIPTION)))
                .servers(
                        List.of(new Server().url("http://localhost:%s".formatted(port))
                                .description("Local service")
                        ));
    }
}
