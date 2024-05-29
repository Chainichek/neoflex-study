package ru.chainichek.neostudy.calculator.domain.service.calculation;

import ru.chainichek.neostudy.calculator.domain.entity.dto.score.PaymentScheduleElementDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface PaymentScheduleCalculator {
    List<PaymentScheduleElementDto> execute(BigDecimal amount,
                                            BigDecimal rate,
                                            BigDecimal monthlyPayment,
                                            int term,
                                            LocalDate loanStartDate);
}
