package ru.chainichek.neostudy.calculator.domain.service.calculation;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;

@Service
public class CalculateAmountUseCase implements AmountCalculator {
    private final BigDecimal insuranceAmountRate;
    private final MathContext resultMathContext;
    private final MathContext calculationMathContext;

    public CalculateAmountUseCase(final @Value("${app.property.insurance-cost-rate}") BigDecimal insuranceAmountRate,
                                  final @Qualifier("resultMathContext") MathContext resultMathContext,
                                  final @Qualifier("calculationMathContext") MathContext calculationMathContext) {
        this.insuranceAmountRate = insuranceAmountRate;
        this.resultMathContext = resultMathContext;
        this.calculationMathContext = calculationMathContext;
    }

    @Override
    public BigDecimal execute(final BigDecimal amount,
                              final boolean isInsuranceEnabled) {
        return amount
                .add(isInsuranceEnabled ? amount.multiply(insuranceAmountRate.divide(BigDecimal.valueOf(100), calculationMathContext),
                        calculationMathContext) : BigDecimal.ZERO, resultMathContext);
    }
}
