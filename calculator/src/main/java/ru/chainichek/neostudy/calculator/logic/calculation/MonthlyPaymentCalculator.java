package ru.chainichek.neostudy.calculator.logic.calculation;

import java.math.BigDecimal;

public interface MonthlyPaymentCalculator {
    BigDecimal calculateMonthlyPayment(BigDecimal amount, BigDecimal rate, int term);
}
