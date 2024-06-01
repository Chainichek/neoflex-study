package ru.chainichek.neostudy.calculator.dto.score;

import jakarta.validation.constraints.Future;
import org.springframework.format.annotation.DateTimeFormat;
import ru.chainichek.neostudy.calculator.util.Validation;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PaymentScheduleElementDto(Integer number,
                                        @DateTimeFormat(pattern = Validation.DATE_FORMAT_PATTERN) @Future
                                        LocalDate date,
                                        BigDecimal totalPayment,
                                        BigDecimal interestPayment,
                                        BigDecimal debtPayment,
                                        BigDecimal remainingDebt) {
}
