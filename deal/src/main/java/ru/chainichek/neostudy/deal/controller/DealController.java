package ru.chainichek.neostudy.deal.controller;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.chainichek.neostudy.deal.api.DealApi;
import ru.chainichek.neostudy.deal.dto.offer.LoanOfferDto;
import ru.chainichek.neostudy.deal.dto.offer.LoanStatementRequestDto;
import ru.chainichek.neostudy.deal.dto.statement.FinishRegistrationRequestDto;
import ru.chainichek.neostudy.deal.service.DealService;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class DealController implements DealApi {
    private final static Logger LOG = LoggerFactory.getLogger(DealController.class);

    private final DealService dealService;

    @Override
    public ResponseEntity<List<LoanOfferDto>> createStatement(LoanStatementRequestDto request) {
        LOG.info("Request on 'create statement' Got: request=%s".formatted(request));

        final List<LoanOfferDto> offers = dealService.createStatement(request);

        LOG.info("Response on 'create statement' Sending: offers=%s".formatted(offers));

        return ResponseEntity.ok(offers);
    }

    @Override
    public ResponseEntity<Void> selectOffer(LoanOfferDto offer) {
        LOG.info("Request on 'select offer' Got: offer=%s".formatted(offer));

        dealService.selectOffer(offer);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> completeStatement(UUID statementId, FinishRegistrationRequestDto finishRegistrationRequest) {
        LOG.info("Request on 'complete statement' Got: statementId = %s finishRegistrationRequest = %s".formatted(statementId, finishRegistrationRequest));

        dealService.completeStatement(statementId, finishRegistrationRequest);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> sendDocuments(UUID statementId) {
        return null;
    }

    @Override
    public ResponseEntity<Void> signDocuments(UUID statementId) {
        return null;
    }

    @Override
    public ResponseEntity<Void> verifyCode(UUID statementId) {
        return null;
    }
}
