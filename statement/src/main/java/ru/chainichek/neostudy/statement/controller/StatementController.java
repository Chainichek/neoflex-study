package ru.chainichek.neostudy.statement.controller;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.chainichek.neostudy.statement.api.StatementApi;
import ru.chainichek.neostudy.statement.dto.prescore.LoanOfferDto;
import ru.chainichek.neostudy.statement.dto.prescore.LoanStatementRequestDto;
import ru.chainichek.neostudy.statement.service.StatementService;

import java.util.List;

@RestController
@AllArgsConstructor
public class StatementController implements StatementApi {
    private final static Logger LOG = LoggerFactory.getLogger(StatementController.class);
    private final StatementService statementService;

    @Override
    public ResponseEntity<List<LoanOfferDto>> getOffers(LoanStatementRequestDto request) {
        LOG.info("Request on 'getting offers' Got: request=%s".formatted(request));

        final List<LoanOfferDto> offers = statementService.getOffers(request);

        LOG.info("Response on 'getting offers' Sending: offers=%s".formatted(offers));
        return ResponseEntity.ok(offers);
    }

    @Override
    public ResponseEntity<Void> selectOffer(LoanOfferDto offer) {
        LOG.info("Request on 'select offer' Got: offer=%s".formatted(offer));

        statementService.selectOffer(offer);

        return ResponseEntity.ok().build();
    }
}
