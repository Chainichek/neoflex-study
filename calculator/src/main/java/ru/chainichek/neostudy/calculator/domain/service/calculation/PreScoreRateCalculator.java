package ru.chainichek.neostudy.calculator.domain.service.calculation;

import java.math.BigDecimal;

public interface PreScoreRateCalculator {
    BigDecimal execute(boolean isInsuranceEnabled, boolean isSalaryClient);
}
