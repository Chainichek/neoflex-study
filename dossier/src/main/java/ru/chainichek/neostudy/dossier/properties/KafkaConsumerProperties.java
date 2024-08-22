package ru.chainichek.neostudy.dossier.properties;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.kafka.consumer")
public class KafkaConsumerProperties {
    @Getter
    @Setter
    public static final class BackOff {
        @Min(1) @Max(60000)
        private long interval = 1000L;
        @Min(1) @Max(10)
        private long attempts = 5;
    }
    @NotNull
    private BackOff backOff = new BackOff();
}
