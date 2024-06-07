package ru.chainichek.neostudy.deal.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.chainichek.neostudy.deal.api.DealApi;
import ru.chainichek.neostudy.deal.dto.offer.LoanOfferDto;
import ru.chainichek.neostudy.deal.dto.offer.LoanStatementRequestDto;
import ru.chainichek.neostudy.deal.dto.statement.FinishRegistrationRequestDto;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class DealController implements DealApi {
    @Override
    public ResponseEntity<List<LoanOfferDto>> createStatement(LoanStatementRequestDto loanStatementRequest) {
        return null;
    }

    @Override
    public ResponseEntity<Void> selectOffer(LoanOfferDto loanOffer) {
        return null;
    }

    @Override
    public ResponseEntity<Void> completeStatement(UUID statementId, FinishRegistrationRequestDto finishRegistrationRequest) {
        return null;
    }
}
