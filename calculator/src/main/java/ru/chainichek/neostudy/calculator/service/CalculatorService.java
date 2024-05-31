package ru.chainichek.neostudy.calculator.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.chainichek.neostudy.calculator.dto.prescore.LoanOfferDto;
import ru.chainichek.neostudy.calculator.dto.prescore.LoanStatementRequestDto;
import ru.chainichek.neostudy.calculator.dto.score.CreditDto;
import ru.chainichek.neostudy.calculator.dto.score.ScoringDataDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CalculatorService {
    private final LoanCalculationService loanCalculationService;
    private final ValidationService validationService;

    public List<LoanOfferDto> getOffers(final LoanStatementRequestDto request) {
        final LoanOfferDto[] offers = new LoanOfferDto[4];
        final boolean[] booleans = {false, true};

        int i = 0;
        for (boolean isInsuranceEnabled: booleans) {
            for (boolean isSalaryClient: booleans) {
                final BigDecimal rate = loanCalculationService.calculatePreScoreRate(isInsuranceEnabled, isSalaryClient);
                final BigDecimal totalAmount = loanCalculationService.calculateAmount(request.amount(), isInsuranceEnabled, isSalaryClient);
                final BigDecimal monthlyPayment = loanCalculationService.calculateMonthlyPayment(totalAmount, rate, request.term());

                offers[i++] = new LoanOfferDto(UUID.randomUUID(),
                        request.amount(),
                        totalAmount,
                        request.term(),
                        monthlyPayment,
                        rate,
                        isInsuranceEnabled,
                        isSalaryClient);
            }
        }
        return List.of(offers);
    }

    public CreditDto getCreditInfo(final ScoringDataDto scoringData) {
        validationService.validateScoringData(scoringData);
        loanCalculationService.check(scoringData);

        final BigDecimal rate = loanCalculationService.calculateScoreRate(scoringData);
        final BigDecimal totalAmount = loanCalculationService.calculateAmount(scoringData.amount(), scoringData.isInsuranceEnabled(), scoringData.isSalaryClient());
        final BigDecimal monthlyPayment = loanCalculationService.calculateMonthlyPayment(totalAmount, rate, scoringData.term());

        return new CreditDto(totalAmount,
                scoringData.term(),
                monthlyPayment,
                rate,
                loanCalculationService.calculatePsk(monthlyPayment, scoringData.term()),
                scoringData.isInsuranceEnabled(),
                scoringData.isSalaryClient(),
                loanCalculationService.calculatePaymentSchedule(totalAmount,
                        rate,
                        monthlyPayment,
                        scoringData.term(),
                        LocalDate.now()));
    }
}
