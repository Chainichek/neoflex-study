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
import ru.chainichek.neostudy.calculator.service.PreScoreService;
import ru.chainichek.neostudy.calculator.service.ScoreService;
import ru.chainichek.neostudy.calculator.api.CalculatorApi;

import java.util.List;

@RestController
@AllArgsConstructor
public class CalculatorController implements CalculatorApi {
    private static final Logger LOG = LoggerFactory.getLogger(CalculatorController.class);

    private final PreScoreService preScoreService;
    private final ScoreService scoreService;
    @Override
    public ResponseEntity<List<LoanOfferDto>> makeOffers(final LoanStatementRequestDto request) {
        LOG.info("Got: " + request);

        final List<LoanOfferDto> offers = preScoreService.getOffers(request);

        LOG.info("Send: " + offers);
        return ResponseEntity.ok(offers);
    }

    @Override
    public ResponseEntity<CreditDto> calculateCredit(final ScoringDataDto data) {
        LOG.info("Got: " + data);

        final CreditDto credit = scoreService.getCreditInfo(data);

        LOG.info("Send: " + credit);
        return ResponseEntity.ok(credit);
    }
}
