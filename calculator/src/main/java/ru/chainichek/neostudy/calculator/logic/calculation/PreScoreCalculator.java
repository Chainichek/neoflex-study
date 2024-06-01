package ru.chainichek.neostudy.calculator.logic.calculation;

import java.math.BigDecimal;

public interface PreScoreCalculator {
    BigDecimal calculatePreScoreRate(boolean isInsuranceEnabled, boolean isSalaryClient);
    BigDecimal calculateAmount(BigDecimal amount, boolean isInsuranceEnabled, boolean isSalaryClient);
}
