package ru.chainichek.neostudy.calculator.domain.service.calculation;

import java.math.BigDecimal;

public interface MonthlyPaymentCalculator {
    BigDecimal execute(BigDecimal amount, BigDecimal rate, int term);
}
