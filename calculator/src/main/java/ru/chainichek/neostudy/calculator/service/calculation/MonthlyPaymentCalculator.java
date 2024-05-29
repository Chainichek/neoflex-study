package ru.chainichek.neostudy.calculator.service.calculation;

import java.math.BigDecimal;

public interface MonthlyPaymentCalculator {
    BigDecimal calculateMonthlyPayment(BigDecimal amount, BigDecimal rate, int term);
}
