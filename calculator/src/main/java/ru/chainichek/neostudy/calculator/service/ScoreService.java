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
import ru.chainichek.neostudy.calculator.service.validator.BirthDateValidator;
import ru.chainichek.neostudy.calculator.service.validator.INNValidator;
import ru.chainichek.neostudy.calculator.util.Validation;
import ru.chainichek.neostudy.calculator.util.ValidationMessage;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@AllArgsConstructor
public class ScoreService {
    private final BirthDateValidator birthDateValidator;
    private final INNValidator innValidator;

    private final CheckCalculator checkCalculator;
    private final ScoreRateCalculator scoreRateCalculator;
    private final AmountCalculator amountCalculator;
    private final MonthlyPaymentCalculator monthlyPaymentCalculator;
    private final PskCalculator pskCalculator;
    private final PaymentScheduleCalculator paymentScheduleCalculator;

    public CreditDto getCreditInfo(final ScoringDataDto scoringData) {
        validateScoringData(scoringData);
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

    public void validateScoringData(final ScoringDataDto scoringData) {
        if (!birthDateValidator.execute(scoringData.birthdate(), Validation.AGE_MIN)) {
            throw new ValidationException(ValidationMessage.AGE_MESSAGE);
        }
        if (!innValidator.execute(scoringData.employment().employerINN())) {
            throw new ValidationException(ValidationMessage.INN_CHECK_NUMBER_MESSAGE);
        }
    }
}
