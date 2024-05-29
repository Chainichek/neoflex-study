package ru.chainichek.neostudy.calculator.service.calculation;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;

@Service
public class CalculatePskUseCase implements PskCalculator {
    private final MathContext resultMathContext;

    public CalculatePskUseCase(final @Qualifier("resultMathContext") MathContext resultMathContext) {
        this.resultMathContext = resultMathContext;
    }

    @Override
    public BigDecimal execute(final BigDecimal monthPayment,
                              final Integer term) {
        return monthPayment.multiply(BigDecimal.valueOf(term), resultMathContext);
    }
}
