package ru.chainichek.neostudy.calculator.service.calculation;

import java.math.BigDecimal;

public interface PskCalculator {
    BigDecimal execute(BigDecimal monthPayment, Integer term);
}
