package ru.chainichek.neostudy.deal.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Logger;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.AllArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.chainichek.neostudy.deal.dto.util.ErrorMessage;
import ru.chainichek.neostudy.deal.dto.util.InternalErrorMessage;
import ru.chainichek.neostudy.deal.exception.ForbiddenException;
import ru.chainichek.neostudy.deal.exception.ValidationException;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FeignConfig {

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.BASIC;
    }

    @Bean
    public ErrorDecoder errorDecoder(ObjectMapper objectMapper) {
        return new RestMessageErrorDecoder(objectMapper);
    }

    @AllArgsConstructor
    public static class RestMessageErrorDecoder implements ErrorDecoder {
        private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(RestMessageErrorDecoder.class);
        private final ErrorDecoder errorDecoder = new Default();
        private final ObjectMapper mapper;

        @Override
        public Exception decode(String methodKey, Response response) {
            if (response.status() == 500) {
                try (InputStream bodyIs = response.body().asInputStream()) {
                    InternalErrorMessage message = mapper.readValue(bodyIs, InternalErrorMessage.class);
                    LOG.error("Resolved an unexpected error: methodKey = {}, message = {}", methodKey, message);
                    return new RuntimeException(message.detail());
                } catch (IOException e) {
                    return new RuntimeException(e);
                }
            }

            if (response.status() >= 400) {
                ErrorMessage message;
                try (InputStream bodyIs = response.body().asInputStream()) {
                    message = mapper.readValue(bodyIs, ErrorMessage.class);
                } catch (IOException e) {
                    return new RuntimeException(e);
                }

                LOG.error("Resolved an error: methodKey = {}, message = {}", methodKey, message);

                switch (response.status()) {
                    case 400 -> {
                        return new ValidationException(message.detail());
                    }
                    case 403 -> {
                        return new ForbiddenException(message.detail());
                    }
                }
            }

            return errorDecoder.decode(methodKey, response);
        }
    }
}
