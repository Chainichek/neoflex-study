package ru.chainichek.neostudy.calculator.service.calculation;

import java.math.BigDecimal;

public interface AmountCalculator {
    BigDecimal execute(BigDecimal amount, boolean isInsuranceEnabled, boolean isSalaryClient);
}
