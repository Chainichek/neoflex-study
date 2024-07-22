package ru.chainichek.neostudy.deal.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Configuration
@OpenAPIDefinition
@SecurityScheme(
        name = "X_API_KEY",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.HEADER,
        paramName = "X-API-KEY"
)
public class OpenAPIConfig {
    private static final String APP_VERSION = "v1.2.0";
    private static final String APP_NAME = "MS 'Deal'";
    private static final String APP_DESCRIPTION = """
            Microservice 'Deal' as part of the 'Credit Bank' application, designed for long-term storage of information about deals
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
