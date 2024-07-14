package ru.chainichek.neostudy.dossier.config;

import feign.FeignException;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.BackOff;
import org.springframework.util.backoff.FixedBackOff;
import ru.chainichek.neostudy.dossier.dto.message.EmailMessage;
import ru.chainichek.neostudy.dossier.properties.KafkaConsumerProperties;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(KafkaConsumerProperties.class)
public class KafkaConsumerConfig {
    private final KafkaConsumerProperties properties;

    @Bean
    public DefaultErrorHandler errorHandler() {
        final BackOff backOff = new FixedBackOff(properties.getBackOff().getInterval(),
                properties.getBackOff().getAttempts());
        final DefaultErrorHandler errorHandler = new DefaultErrorHandler(
                (record, ex) -> {
                    log.error("Failed to process record with key = {} and value = {}",
                            record.key(),
                            record.value());
                    log.error("Last threw exception was: %s".formatted(ex.getMessage()), ex);
                }, backOff
        );

        errorHandler.addRetryableExceptions(
                ConnectException.class,
                SocketTimeoutException.class);

        errorHandler.addNotRetryableExceptions(
                NullPointerException.class,
                IllegalArgumentException.class,
                IOException.class
        );
        errorHandler.addNotRetryableExceptions(ConstraintViolationException.class);
        errorHandler.addNotRetryableExceptions(FeignException.NotFound.class);

        return errorHandler;
    }

    @Bean
    @ConditionalOnBean(name = "errorHandler")
    public ConcurrentKafkaListenerContainerFactory<String, EmailMessage> kafkaListenerContainerFactory(
            ConsumerFactory<String, EmailMessage> consumerFactory) {
        final ConcurrentKafkaListenerContainerFactory<String, EmailMessage> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory);
        factory.setConcurrency(3);
        factory.setCommonErrorHandler(errorHandler());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.RECORD);

        return factory;
    }
}
