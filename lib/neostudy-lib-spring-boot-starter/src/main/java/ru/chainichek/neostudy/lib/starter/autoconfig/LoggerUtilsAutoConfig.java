package ru.chainichek.neostudy.lib.starter.autoconfig;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.chainichek.neostudy.lib.loggerutils.aspect.LoggableAspect;
import ru.chainichek.neostudy.lib.starter.properties.LoggerUtilsProperties;

@Slf4j
@Configuration
@ConditionalOnClass(LoggableAspect.class)
@EnableConfigurationProperties(LoggerUtilsProperties.class)
public class LoggerUtilsAutoConfig {
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "neostudy.lib.loggerutils", value = "enabled", havingValue = "true")
    public LoggableAspect loggableAspect() {
        final LoggableAspect loggableAspect = new LoggableAspect();
        log.info("NeostudyLibStarter initialized logger-utils");
        return loggableAspect;
    }
}
