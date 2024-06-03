package ru.chainichek.neostudy.calculator.controller;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.chainichek.neostudy.calculator.dto.prescore.LoanOfferDto;
import ru.chainichek.neostudy.calculator.dto.prescore.LoanStatementRequestDto;
import ru.chainichek.neostudy.calculator.dto.score.CreditDto;
import ru.chainichek.neostudy.calculator.dto.score.ScoringDataDto;
import ru.chainichek.neostudy.calculator.api.CalculatorApi;
import ru.chainichek.neostudy.calculator.service.CalculatorService;

import java.util.List;

@RestController
@AllArgsConstructor
public class CalculatorController implements CalculatorApi {
    private static final Logger LOG = LoggerFactory.getLogger(CalculatorController.class);

    private final CalculatorService calculatorService;
    @Override
    public ResponseEntity<List<LoanOfferDto>> getOffers(LoanStatementRequestDto request) {
        LOG.info("Request on 'getting offers' Got: request=%s".formatted(request));

        final List<LoanOfferDto> offers = calculatorService.getOffers(request);

        LOG.info("Response on 'getting offers' Sending: offers=%s".formatted(offers));
        return ResponseEntity.ok(offers);
    }

    @Override
    public ResponseEntity<CreditDto> calculateCredit(ScoringDataDto data) {
        LOG.info("Request on 'calculating credit' Got: data=%s".formatted(data));

        final CreditDto credit = calculatorService.getCreditInfo(data);

        LOG.info("Response on 'calculating credit' Sending: credit=%s".formatted(credit));
        return ResponseEntity.ok(credit);
    }
}
