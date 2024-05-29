package ru.chainichek.neostudy.calculator.service.calculation;

import ru.chainichek.neostudy.calculator.dto.score.PaymentScheduleElementDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface PaymentScheduleCalculator {
    List<PaymentScheduleElementDto> calculatePaymentSchedule(BigDecimal amount,
                                                             BigDecimal rate,
                                                             BigDecimal monthlyPayment,
                                                             int term,
                                                             LocalDate loanStartDate);
}
