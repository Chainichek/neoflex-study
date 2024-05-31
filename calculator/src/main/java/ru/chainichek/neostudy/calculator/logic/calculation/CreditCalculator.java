package ru.chainichek.neostudy.calculator.logic.calculation;

import ru.chainichek.neostudy.calculator.dto.score.PaymentScheduleElementDto;
import ru.chainichek.neostudy.calculator.dto.score.ScoringDataDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface CreditCalculator {
    void checkScoringData(ScoringDataDto scoringData);
    BigDecimal calculateScoreRate(ScoringDataDto scoringData);
    BigDecimal calculatePsk(BigDecimal amount, BigDecimal monthPayment, Integer term);
    List<PaymentScheduleElementDto> calculatePaymentSchedule(BigDecimal amount,
                                                             BigDecimal rate,
                                                             BigDecimal monthlyPayment,
                                                             int term,
                                                             LocalDate loanStartDate);
}
