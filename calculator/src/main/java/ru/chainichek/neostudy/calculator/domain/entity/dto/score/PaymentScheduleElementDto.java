package ru.chainichek.neostudy.calculator.domain.entity.dto.score;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PaymentScheduleElementDto(Integer number,
                                        LocalDate date,
                                        BigDecimal totalPayment,
                                        BigDecimal interestPayment,
                                        BigDecimal debtPayment,
                                        BigDecimal remainingDebt) {
}
