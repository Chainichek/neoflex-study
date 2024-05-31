package ru.chainichek.neostudy.calculator.logic.calculation;

import java.math.BigDecimal;

public interface AmountCalculator {
    BigDecimal calculateAmount(BigDecimal amount, boolean isInsuranceEnabled, boolean isSalaryClient);
}
