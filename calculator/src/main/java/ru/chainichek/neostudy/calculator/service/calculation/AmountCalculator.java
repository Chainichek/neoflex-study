package ru.chainichek.neostudy.calculator.service.calculation;

import java.math.BigDecimal;

public interface AmountCalculator {
    BigDecimal calculateAmount(BigDecimal amount, boolean isInsuranceEnabled, boolean isSalaryClient);
}
