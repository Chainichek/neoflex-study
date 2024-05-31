package ru.chainichek.neostudy.calculator.logic.calculation;

import java.math.BigDecimal;

public interface PskCalculator {
    BigDecimal calculatePsk(BigDecimal monthPayment, Integer term);
}
