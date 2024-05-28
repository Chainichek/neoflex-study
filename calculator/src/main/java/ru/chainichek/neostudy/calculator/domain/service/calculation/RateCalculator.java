package ru.chainichek.neostudy.calculator.domain.service.calculation;

import java.math.BigDecimal;

public interface RateCalculator {
    BigDecimal execute(boolean isInsuranceEnabled, boolean isSalaryClient);
}
