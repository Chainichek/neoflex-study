package ru.chainichek.neostudy.calculator.domain.service.calculation;

import java.math.BigDecimal;

public interface AmountCalculator {
    BigDecimal execute(BigDecimal amount, boolean isInsuranceEnabled);
}
