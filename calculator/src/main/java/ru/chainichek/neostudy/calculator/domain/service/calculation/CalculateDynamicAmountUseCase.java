package ru.chainichek.neostudy.calculator.domain.service.calculation;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;

@Service
public class CalculateDynamicAmountUseCase implements AmountCalculator {
    private final BigDecimal insuranceAmountRate = BigDecimal.valueOf(6);
    private final BigDecimal salaryAmountRate = BigDecimal.ZERO;
    private final MathContext resultMathContext;
    private final MathContext calculationMathContext;

    public CalculateDynamicAmountUseCase(final @Qualifier("resultMathContext") MathContext resultMathContext,
                                         final @Qualifier("calculationMathContext") MathContext calculationMathContext) {
        this.resultMathContext = resultMathContext;
        this.calculationMathContext = calculationMathContext;
    }

    @Override
    public BigDecimal execute(final BigDecimal amount,
                              final boolean isInsuranceEnabled,
                              final boolean isSalaryClient) {
        return amount
                .add(isInsuranceEnabled ? amount.multiply(insuranceAmountRate.divide(BigDecimal.valueOf(100), calculationMathContext),
                        calculationMathContext) : BigDecimal.ZERO, calculationMathContext)
                .add(isSalaryClient ? amount.multiply(salaryAmountRate.divide(BigDecimal.valueOf(100), calculationMathContext),
                        calculationMathContext) : BigDecimal.ZERO, resultMathContext);
    }
}
