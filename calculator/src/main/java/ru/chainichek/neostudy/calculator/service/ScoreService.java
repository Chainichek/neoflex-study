package ru.chainichek.neostudy.calculator.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.chainichek.neostudy.calculator.dto.score.CreditDto;
import ru.chainichek.neostudy.calculator.dto.score.ScoringDataDto;
import ru.chainichek.neostudy.calculator.exception.ValidationException;
import ru.chainichek.neostudy.calculator.service.calculation.CalculatorService;
import ru.chainichek.neostudy.calculator.service.validator.ValidatorService;
import ru.chainichek.neostudy.calculator.util.Validation;
import ru.chainichek.neostudy.calculator.util.ValidationMessage;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@AllArgsConstructor
public class ScoreService {
    private final ValidatorService validatorService;

    private final CalculatorService calculatorService;

    public CreditDto getCreditInfo(final ScoringDataDto scoringData) {
        validateScoringData(scoringData);
        calculatorService.check(scoringData);

        final BigDecimal rate = calculatorService.calculateScoreRate(scoringData);
        final BigDecimal totalAmount = calculatorService.calculateAmount(scoringData.amount(), scoringData.isInsuranceEnabled(), scoringData.isSalaryClient());
        final BigDecimal monthlyPayment = calculatorService.calculateMonthlyPayment(totalAmount, rate, scoringData.term());

        return new CreditDto(totalAmount,
                scoringData.term(),
                monthlyPayment,
                rate,
                calculatorService.calculatePsk(monthlyPayment, scoringData.term()),
                scoringData.isInsuranceEnabled(),
                scoringData.isSalaryClient(),
                calculatorService.calculatePaymentSchedule(totalAmount,
                        rate,
                        monthlyPayment,
                        scoringData.term(),
                        LocalDate.now()));
    }

    public void validateScoringData(final ScoringDataDto scoringData) {
        if (!validatorService.validateBirthdate(scoringData.birthdate(), Validation.AGE_MIN)) {
            throw new ValidationException(ValidationMessage.AGE_MESSAGE);
        }
        if (!validatorService.validateINN(scoringData.employment().employerINN())) {
            throw new ValidationException(ValidationMessage.INN_CHECK_NUMBER_MESSAGE);
        }
    }
}
