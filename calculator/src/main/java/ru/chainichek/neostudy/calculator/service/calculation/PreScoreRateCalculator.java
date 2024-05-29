package ru.chainichek.neostudy.calculator.service.calculation;

import java.math.BigDecimal;

public interface PreScoreRateCalculator {
    BigDecimal execute(boolean isInsuranceEnabled, boolean isSalaryClient);
}
