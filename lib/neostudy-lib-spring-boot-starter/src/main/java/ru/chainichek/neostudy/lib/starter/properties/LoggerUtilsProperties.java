package ru.chainichek.neostudy.lib.starter.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("neostudy.lib.loggerutils")
public class LoggerUtilsProperties {
    private Boolean enabled;
}
