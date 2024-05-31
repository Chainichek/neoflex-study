package ru.chainichek.neostudy.calculator.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.chainichek.neostudy.calculator.dto.prescore.LoanOfferDto;
import ru.chainichek.neostudy.calculator.dto.prescore.LoanStatementRequestDto;
import ru.chainichek.neostudy.calculator.dto.score.CreditDto;
import ru.chainichek.neostudy.calculator.dto.score.PaymentScheduleElementDto;
import ru.chainichek.neostudy.calculator.dto.score.ScoringDataDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CalculatorService {
    private final static Logger LOG = LoggerFactory.getLogger(CalculatorService.class);

    private final LoanCalculationService loanCalculationService;
    private final ValidationService validationService;

    public List<LoanOfferDto> getOffers(final LoanStatementRequestDto request) {
        LOG.debug("Starting to generate offers");

        final LoanOfferDto[] offers = new LoanOfferDto[4];
        final boolean[] booleans = {false, true};

        int i = 0;
        for (boolean isInsuranceEnabled : booleans) {
            for (boolean isSalaryClient : booleans) {
                final BigDecimal rate = loanCalculationService.calculatePreScoreRate(isInsuranceEnabled, isSalaryClient);
                final BigDecimal totalAmount = loanCalculationService.calculateAmount(request.amount(), isInsuranceEnabled, isSalaryClient);
                final BigDecimal monthlyPayment = loanCalculationService.calculateMonthlyPayment(totalAmount, rate, request.term());

                final LoanOfferDto loanOffer = new LoanOfferDto(UUID.randomUUID(),
                        request.amount(),
                        totalAmount,
                        request.term(),
                        monthlyPayment,
                        rate,
                        isInsuranceEnabled,
                        isSalaryClient);

                LOG.debug("Generated offer: isInsuranceEnabled = %s, isSalaryClient = %s, offer = %s".formatted(isInsuranceEnabled, isSalaryClient, loanOffer));

                offers[i++] = loanOffer;
            }
        }

        LOG.debug("Finished generating offers: offers = %s".formatted(Arrays.toString(offers)));
        return List.of(offers);
    }

    public CreditDto getCreditInfo(final ScoringDataDto scoringData) {
        LOG.debug("Starting to generate credit info");

        validationService.validateScoringData(scoringData);
        loanCalculationService.check(scoringData);

        final BigDecimal rate = loanCalculationService.calculateScoreRate(scoringData);
        final BigDecimal totalAmount = loanCalculationService.calculateAmount(scoringData.amount(), scoringData.isInsuranceEnabled(), scoringData.isSalaryClient());
        final BigDecimal monthlyPayment = loanCalculationService.calculateMonthlyPayment(totalAmount, rate, scoringData.term());
        final BigDecimal psk = loanCalculationService.calculatePsk(totalAmount, monthlyPayment, scoringData.term());
        final List<PaymentScheduleElementDto> schedule = loanCalculationService.calculatePaymentSchedule(totalAmount,
                rate,
                monthlyPayment,
                scoringData.term(),
                LocalDate.now());

        final CreditDto credit = new CreditDto(totalAmount,
                scoringData.term(),
                monthlyPayment,
                rate,
                psk,
                scoringData.isInsuranceEnabled(),
                scoringData.isSalaryClient(),
                schedule);

        LOG.debug("Finished generating credit info: credit = %s".formatted(credit));

        return credit;
    }
}
