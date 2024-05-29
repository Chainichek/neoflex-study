package ru.chainichek.neostudy.calculator.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.chainichek.neostudy.calculator.dto.score.CreditDto;
import ru.chainichek.neostudy.calculator.dto.score.ScoringDataDto;
import ru.chainichek.neostudy.calculator.exception.ValidationException;
import ru.chainichek.neostudy.calculator.service.calculation.AmountCalculator;
import ru.chainichek.neostudy.calculator.service.calculation.CheckCalculator;
import ru.chainichek.neostudy.calculator.service.calculation.MonthlyPaymentCalculator;
import ru.chainichek.neostudy.calculator.service.calculation.PaymentScheduleCalculator;
import ru.chainichek.neostudy.calculator.service.calculation.PskCalculator;
import ru.chainichek.neostudy.calculator.service.calculation.ScoreRateCalculator;
import ru.chainichek.neostudy.calculator.service.validator.BirthdateValidator;
import ru.chainichek.neostudy.calculator.service.validator.INNValidator;
import ru.chainichek.neostudy.calculator.util.Validation;
import ru.chainichek.neostudy.calculator.util.ValidationMessage;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@AllArgsConstructor
public class ScoreService {
    private final BirthdateValidator birthDateValidator;
    private final INNValidator innValidator;

    private final CheckCalculator checkCalculator;
    private final ScoreRateCalculator scoreRateCalculator;
    private final AmountCalculator amountCalculator;
    private final MonthlyPaymentCalculator monthlyPaymentCalculator;
    private final PskCalculator pskCalculator;
    private final PaymentScheduleCalculator paymentScheduleCalculator;

    public CreditDto getCreditInfo(final ScoringDataDto scoringData) {
        validateScoringData(scoringData);
        checkCalculator.check(scoringData);

        final BigDecimal rate = scoreRateCalculator.calculateScoreRate(scoringData);
        final BigDecimal totalAmount = amountCalculator.calculateAmount(scoringData.amount(), scoringData.isInsuranceEnabled(), scoringData.isSalaryClient());
        final BigDecimal monthlyPayment = monthlyPaymentCalculator.calculateMonthlyPayment(totalAmount, rate, scoringData.term());

        return new CreditDto(totalAmount,
                scoringData.term(),
                monthlyPayment,
                rate,
                pskCalculator.calculatePsk(monthlyPayment, scoringData.term()),
                scoringData.isInsuranceEnabled(),
                scoringData.isSalaryClient(),
                paymentScheduleCalculator.calculatePaymentSchedule(totalAmount,
                        rate,
                        monthlyPayment,
                        scoringData.term(),
                        LocalDate.now()));
    }

    public void validateScoringData(final ScoringDataDto scoringData) {
        if (!birthDateValidator.validateBirthdate(scoringData.birthdate(), Validation.AGE_MIN)) {
            throw new ValidationException(ValidationMessage.AGE_MESSAGE);
        }
        if (!innValidator.validateINN(scoringData.employment().employerINN())) {
            throw new ValidationException(ValidationMessage.INN_CHECK_NUMBER_MESSAGE);
        }
    }
}
