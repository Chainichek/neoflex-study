package ru.chainichek.neostudy.calculator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.MathContext;
import java.math.RoundingMode;

@Configuration
public class MathConfig {
    @Value("${app.property.result-precision}")
    private int resultPrecision;
    @Bean
    public MathContext resultMathContext() {
        return new MathContext(resultPrecision, RoundingMode.HALF_DOWN);
    }
    @Bean
    public MathContext calculationMathContext() {
        return new MathContext(10, RoundingMode.HALF_DOWN);
    }
}
