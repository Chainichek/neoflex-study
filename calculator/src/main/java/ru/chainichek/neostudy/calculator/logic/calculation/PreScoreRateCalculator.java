package ru.chainichek.neostudy.calculator.logic.calculation;

import java.math.BigDecimal;

public interface PreScoreRateCalculator {
    BigDecimal calculatePreScoreRate(boolean isInsuranceEnabled, boolean isSalaryClient);
}
