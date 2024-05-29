package ru.chainichek.neostudy.calculator.web.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.chainichek.neostudy.calculator.domain.entity.dto.prescore.LoanOfferDto;
import ru.chainichek.neostudy.calculator.domain.entity.dto.prescore.LoanStatementRequestDto;
import ru.chainichek.neostudy.calculator.domain.entity.dto.score.CreditDto;
import ru.chainichek.neostudy.calculator.domain.entity.dto.score.ScoringDataDto;

import java.util.List;

@RequestMapping("/calculator")
public interface CalculatorApi {

    @PostMapping("/offers")
    ResponseEntity<List<LoanOfferDto>> makeOffers(@RequestBody @Valid @NotNull LoanStatementRequestDto loanStatementRequest);

    @PostMapping("/calc")
    ResponseEntity<CreditDto> calculateCredit(@RequestBody @Valid @NotNull ScoringDataDto scoringData);
}
