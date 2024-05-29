package ru.chainichek.neostudy.calculator.domain.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.chainichek.neostudy.calculator.domain.entity.dto.prescore.LoanOfferDto;
import ru.chainichek.neostudy.calculator.domain.entity.dto.prescore.LoanStatementRequestDto;
import ru.chainichek.neostudy.calculator.domain.service.calculation.AmountCalculator;
import ru.chainichek.neostudy.calculator.domain.service.calculation.MonthlyPaymentCalculator;
import ru.chainichek.neostudy.calculator.domain.service.calculation.RateCalculator;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PreScoreUseCase {
    private final RateCalculator rateCalculator;
    private final AmountCalculator amountCalculator;
    private final MonthlyPaymentCalculator monthlyPaymentCalculator;

    private final boolean[] booleans = {false, true};

    public List<LoanOfferDto> execute(final LoanStatementRequestDto request) {
        final LoanOfferDto[] offers = new LoanOfferDto[4];

        int i = 0;
        for (boolean isInsuranceEnabled: booleans) {
            for (boolean isSalaryClient: booleans) {
                final BigDecimal rate = rateCalculator.execute(isInsuranceEnabled, isSalaryClient);
                final BigDecimal totalAmount = amountCalculator.execute(request.amount(), isInsuranceEnabled, isSalaryClient);
                final BigDecimal monthlyPayment = monthlyPaymentCalculator.execute(totalAmount, rate, request.term());

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
}
