package ru.chainichek.neostudy.calculator.domain.service.calculation;

import java.math.BigDecimal;

public interface PskCalculator {
    BigDecimal execute(BigDecimal monthPayment, Integer term);
}
