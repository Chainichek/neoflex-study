package ru.chainichek.neostudy.statement.service;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.chainichek.neostudy.statement.client.DealClient;
import ru.chainichek.neostudy.statement.dto.prescore.LoanOfferDto;
import ru.chainichek.neostudy.statement.dto.prescore.LoanStatementRequestDto;

import java.util.List;


@Service
@AllArgsConstructor
public class StatementService {
    private final DealClient dealClient;

    public List<LoanOfferDto> getOffers(LoanStatementRequestDto request) {
        return dealClient.createStatement(request);
    }

    public void selectOffer(LoanOfferDto offer) {
        dealClient.selectOffer(offer);
    }
}
