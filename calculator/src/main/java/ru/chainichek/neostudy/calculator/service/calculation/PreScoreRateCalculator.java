package ru.chainichek.neostudy.calculator.service.calculation;

import java.math.BigDecimal;

public interface PreScoreRateCalculator {
    BigDecimal calculatePreScoreRate(boolean isInsuranceEnabled, boolean isSalaryClient);
}
