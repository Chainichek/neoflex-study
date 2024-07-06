package ru.chainichek.neostudy.deal.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.chainichek.neostudy.deal.api.DealApi;
import ru.chainichek.neostudy.deal.dto.offer.LoanOfferDto;
import ru.chainichek.neostudy.deal.dto.offer.LoanStatementRequestDto;
import ru.chainichek.neostudy.deal.dto.statement.FinishRegistrationRequestDto;
import ru.chainichek.neostudy.deal.service.DealService;
import ru.chainichek.neostudy.loggerutils.annotation.ControllerLoggable;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@ControllerLoggable
public class DealController implements DealApi {
    private final DealService dealService;

    @Override
    public ResponseEntity<List<LoanOfferDto>> createStatement(LoanStatementRequestDto request) {
        final List<LoanOfferDto> offers = dealService.createStatement(request);
        return ResponseEntity.ok(offers);
    }

    @Override
    public ResponseEntity<Void> selectOffer(LoanOfferDto offer) {
        dealService.selectOffer(offer);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> completeStatement(UUID statementId, FinishRegistrationRequestDto finishRegistrationRequest) {
        dealService.completeStatement(statementId, finishRegistrationRequest);
        return ResponseEntity.ok().build();
    }
}
