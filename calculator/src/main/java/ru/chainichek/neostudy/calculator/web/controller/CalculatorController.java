package ru.chainichek.neostudy.calculator.web.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.chainichek.neostudy.calculator.domain.entity.dto.prescore.LoanOfferDto;
import ru.chainichek.neostudy.calculator.domain.entity.dto.prescore.LoanStatementRequestDto;
import ru.chainichek.neostudy.calculator.domain.entity.dto.score.CreditDto;
import ru.chainichek.neostudy.calculator.domain.entity.dto.score.ScoringDataDto;
import ru.chainichek.neostudy.calculator.web.api.CalculatorApi;

import java.util.List;

@RestController
@AllArgsConstructor
public class CalculatorController implements CalculatorApi {
    @Override
    public ResponseEntity<List<LoanOfferDto>> makeOffers(final LoanStatementRequestDto loanStatementRequest) {
        return null;
    }

    @Override
    public ResponseEntity<CreditDto> calculateCredit(final ScoringDataDto scoringData) {
        return null;
    }
}
