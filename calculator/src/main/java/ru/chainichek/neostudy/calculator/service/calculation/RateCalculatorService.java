package ru.chainichek.neostudy.calculator.service.calculation;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.chainichek.neostudy.calculator.dto.score.ScoringDataDto;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.time.Period;

@Service
public class RateCalculatorService implements PreScoreRateCalculator, ScoreRateCalculator {

    private final BigDecimal baseRate;
    private final MathContext resultMathContext;
    private final MathContext calculationMathContext;

    private final BigDecimal insuranceRate = BigDecimal.valueOf(3);
    private final BigDecimal salaryRate = BigDecimal.valueOf(1);

    public RateCalculatorService(final @Value("${app.property.base-rate}") BigDecimal baseRate,
                                 final @Qualifier("resultMathContext") MathContext resultMathContext,
                                 final @Qualifier("calculationMathContext") MathContext calculationMathContext) {
        this.baseRate = baseRate;
        this.resultMathContext = resultMathContext;
        this.calculationMathContext = calculationMathContext;
    }

    @Override
    public BigDecimal calculatePreScoreRate(final boolean isInsuranceEnabled,
                                            final boolean isSalaryClient) {
        return baseRate
                .subtract(isInsuranceEnabled ? insuranceRate : BigDecimal.ZERO, calculationMathContext)
                .subtract(isSalaryClient ? salaryRate : BigDecimal.ZERO, resultMathContext);
    }

    @Override
    public BigDecimal calculateScoreRate(final ScoringDataDto scoringData) {
        BigDecimal rate = baseRate;

        switch (scoringData.employment().employmentStatus()) {
            case SELF_EMPLOYED -> rate = rate.add(BigDecimal.valueOf(2), calculationMathContext);
            case BUSINESS_OWNER -> rate = rate.add(BigDecimal.valueOf(3), calculationMathContext);
        }

        if (scoringData.employment().position() != null) {
            switch (scoringData.employment().position()) {
                case MIDDLE_MANAGER -> rate = rate.subtract(BigDecimal.valueOf(2), calculationMathContext);
                case TOP_MANAGER -> rate = rate.subtract(BigDecimal.valueOf(3), calculationMathContext);
            }
        }

        if (scoringData.maritalStatus() != null) {
            switch (scoringData.maritalStatus()) {
                case MARRIED -> rate = rate.subtract(BigDecimal.valueOf(3), calculationMathContext);
                case DIVORCED -> rate = rate.add(BigDecimal.ONE, calculationMathContext);
            }
        }

        final int age = Period.between(scoringData.birthdate(), LocalDate.now()).getYears();
        switch (scoringData.gender()) {
            case MALE -> {
                if (age >= 30 && age < 55) {
                    rate = rate.subtract(BigDecimal.valueOf(3), calculationMathContext);
                }
            }
            case FEMALE -> {
                if (age >= 32 && age < 60) {
                    rate = rate.subtract(BigDecimal.valueOf(3), calculationMathContext);
                }
            }
        }

        return rate
                .subtract(scoringData.isInsuranceEnabled() ? insuranceRate : BigDecimal.ZERO, calculationMathContext)
                .subtract(scoringData.isSalaryClient() ? salaryRate : BigDecimal.ZERO, resultMathContext);
    }
}
