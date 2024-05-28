package ru.chainichek.neostudy.calculator.domain.service.calculation;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;

@Service
public class CalculateRateUseCase implements RateCalculator {
    private final BigDecimal baseRate;
    private final BigDecimal insuranceRate;
    private final BigDecimal salaryRate;
    private final MathContext resultMathContext;
    private final MathContext calculationMathContext;

    public CalculateRateUseCase(final @Value("${app.property.base-rate}") BigDecimal baseRate,
                                final @Value("${app.property.insurance-discount}") BigDecimal insuranceRate,
                                final @Value("${app.property.salary-discount}") BigDecimal salaryRate,
                                final @Qualifier("resultMathContext") MathContext resultMathContext,
                                final @Qualifier("calculationMathContext") MathContext calculationMathContext) {
        this.baseRate = baseRate;
        this.insuranceRate = insuranceRate;
        this.salaryRate = salaryRate;
        this.resultMathContext = resultMathContext;
        this.calculationMathContext = calculationMathContext;
    }

    @Override
    public BigDecimal execute(final boolean isInsuranceEnabled,
                              final boolean isSalaryClient) {
        return baseRate
                .subtract(isInsuranceEnabled ? insuranceRate : BigDecimal.ZERO, calculationMathContext)
                .subtract(isSalaryClient ? salaryRate : BigDecimal.ZERO, resultMathContext);
    }
}
