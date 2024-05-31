package ru.chainichek.neostudy.calculator.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.chainichek.neostudy.calculator.dto.prescore.LoanOfferDto;
import ru.chainichek.neostudy.calculator.dto.prescore.LoanStatementRequestDto;
import ru.chainichek.neostudy.calculator.dto.score.CreditDto;
import ru.chainichek.neostudy.calculator.dto.score.PaymentScheduleElementDto;
import ru.chainichek.neostudy.calculator.dto.score.ScoringDataDto;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class CalculatorService {
    private final static Logger LOG = LoggerFactory.getLogger(CalculatorService.class);
    private final MathContext resultMathContext;
    private final LoanCalculationService loanCalculationService;
    private final ValidationService validationService;

    public CalculatorService(final @Qualifier("resultMathContext") MathContext resultMathContext,
                             final LoanCalculationService loanCalculationService,
                             final ValidationService validationService) {
        this.resultMathContext = resultMathContext;
        this.loanCalculationService = loanCalculationService;
        this.validationService = validationService;
    }


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
                        request.amount().setScale(resultMathContext.getPrecision(), resultMathContext.getRoundingMode()),
                        totalAmount.setScale(resultMathContext.getPrecision(), resultMathContext.getRoundingMode()),
                        request.term(),
                        monthlyPayment.setScale(resultMathContext.getPrecision(), resultMathContext.getRoundingMode()),
                        rate.setScale(resultMathContext.getPrecision(), resultMathContext.getRoundingMode()),
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
                LocalDate.now())
                .stream()
                .map(x -> new PaymentScheduleElementDto(x.number(),
                        x.date(),
                        x.totalPayment().setScale(resultMathContext.getPrecision(), resultMathContext.getRoundingMode()),
                        x.interestPayment().setScale(resultMathContext.getPrecision(), resultMathContext.getRoundingMode()),
                        x.debtPayment().setScale(resultMathContext.getPrecision(), resultMathContext.getRoundingMode()),
                        x.remainingDebt().setScale(resultMathContext.getPrecision(), resultMathContext.getRoundingMode())))
                .toList();

        final CreditDto credit = new CreditDto(totalAmount.setScale(resultMathContext.getPrecision(), resultMathContext.getRoundingMode()),
                scoringData.term(),
                monthlyPayment.setScale(resultMathContext.getPrecision(), resultMathContext.getRoundingMode()),
                rate.setScale(resultMathContext.getPrecision(), resultMathContext.getRoundingMode()),
                psk.setScale(resultMathContext.getPrecision(), resultMathContext.getRoundingMode()),
                scoringData.isInsuranceEnabled(),
                scoringData.isSalaryClient(),
                schedule);

        LOG.debug("Finished generating credit info: credit = %s".formatted(credit));

        return credit;
    }
}
