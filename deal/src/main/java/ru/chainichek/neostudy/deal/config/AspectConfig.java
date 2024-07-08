package ru.chainichek.neostudy.deal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import ru.chainichek.neostudy.lib.loggerutils.aspect.LoggableAspect;

@Configuration
@EnableAspectJAutoProxy
public class AspectConfig {
    @Bean
    public LoggableAspect loggableAspect() {
        return new LoggableAspect();
    }
}
