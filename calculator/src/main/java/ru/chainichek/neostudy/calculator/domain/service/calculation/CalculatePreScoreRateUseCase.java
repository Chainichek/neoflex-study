package ru.chainichek.neostudy.calculator.domain.service.calculation;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;

@Service
public class CalculatePreScoreRateUseCase implements PreScoreRateCalculator {
    private final BigDecimal baseRate;
    private final MathContext resultMathContext;
    private final MathContext calculationMathContext;

    private final BigDecimal insuranceRate = BigDecimal.valueOf(3);
    private final BigDecimal salaryRate = BigDecimal.valueOf(1);


    public CalculatePreScoreRateUseCase(final @Value("${app.property.base-rate}") BigDecimal baseRate,
                                        final @Qualifier("resultMathContext") MathContext resultMathContext,
                                        final @Qualifier("calculationMathContext") MathContext calculationMathContext) {
        this.baseRate = baseRate;
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
