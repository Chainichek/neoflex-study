package ru.chainichek.neostudy.calculator.service.calculation;

import java.math.BigDecimal;

public interface MonthlyPaymentCalculator {
    BigDecimal execute(BigDecimal amount, BigDecimal rate, int term);
}
