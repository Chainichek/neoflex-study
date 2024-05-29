package ru.chainichek.neostudy.calculator.service.calculation;

import java.math.BigDecimal;

public interface PskCalculator {
    BigDecimal calculatePsk(BigDecimal monthPayment, Integer term);
}
