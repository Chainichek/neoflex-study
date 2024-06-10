package ru.chainichek.neostudy.deal.controller;

import lombok.AllArgsConstructor;
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
    private final DealService dealService;

    @Override
    public ResponseEntity<List<LoanOfferDto>> createStatement(LoanStatementRequestDto loanStatementRequest) {
        final List<LoanOfferDto> loanOffers = dealService.createStatement(loanStatementRequest);
        return ResponseEntity.ok(loanOffers);
    }

    @Override
    public ResponseEntity<Void> selectOffer(LoanOfferDto loanOffer) {
        dealService.selectOffer(loanOffer);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> completeStatement(UUID statementId, FinishRegistrationRequestDto finishRegistrationRequest) {
        dealService.completeStatement(statementId, finishRegistrationRequest);
        return ResponseEntity.ok().build();
    }
}
