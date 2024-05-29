package ru.chainichek.neostudy.calculator.domain.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.chainichek.neostudy.calculator.domain.entity.dto.score.CreditDto;
import ru.chainichek.neostudy.calculator.domain.entity.dto.score.ScoringDataDto;
import ru.chainichek.neostudy.calculator.domain.service.calculation.AmountCalculator;
import ru.chainichek.neostudy.calculator.domain.service.calculation.CheckCalculator;
import ru.chainichek.neostudy.calculator.domain.service.calculation.MonthlyPaymentCalculator;
import ru.chainichek.neostudy.calculator.domain.service.calculation.PaymentScheduleCalculator;
import ru.chainichek.neostudy.calculator.domain.service.calculation.PskCalculator;
import ru.chainichek.neostudy.calculator.domain.service.calculation.ScoreRateCalculator;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@AllArgsConstructor
public class ScoreUseCase {
    private final ValidateScoringDataUseCase scoringDataValidator;

    private final CheckCalculator checkCalculator;
    private final ScoreRateCalculator scoreRateCalculator;
    private final AmountCalculator amountCalculator;
    private final MonthlyPaymentCalculator monthlyPaymentCalculator;
    private final PskCalculator pskCalculator;
    private final PaymentScheduleCalculator paymentScheduleCalculator;

    public CreditDto execute(final ScoringDataDto scoringData) {
        scoringDataValidator.execute(scoringData);
        checkCalculator.execute(scoringData);

        final BigDecimal rate = scoreRateCalculator.execute(scoringData);
        final BigDecimal totalAmount = amountCalculator.execute(scoringData.amount(), scoringData.isInsuranceEnabled(), scoringData.isSalaryClient());
        final BigDecimal monthlyPayment = monthlyPaymentCalculator.execute(totalAmount, rate, scoringData.term());

        return new CreditDto(totalAmount,
                scoringData.term(),
                monthlyPayment,
                rate,
                pskCalculator.execute(monthlyPayment, scoringData.term()),
                scoringData.isInsuranceEnabled(),
                scoringData.isSalaryClient(),
                paymentScheduleCalculator.execute(totalAmount,
                        rate,
                        monthlyPayment,
                        scoringData.term(),
                        LocalDate.now()));
    }
}
