package ru.chainichek.neostudy.calculator.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.chainichek.neostudy.calculator.dto.prescore.LoanOfferDto;
import ru.chainichek.neostudy.calculator.dto.prescore.LoanStatementRequestDto;
import ru.chainichek.neostudy.calculator.dto.score.CreditDto;
import ru.chainichek.neostudy.calculator.dto.score.ScoringDataDto;

import java.util.List;

@RequestMapping("/calculator")
public interface CalculatorApi {

    @PostMapping("/offers")
    ResponseEntity<List<LoanOfferDto>> makeOffers(@RequestBody @Valid @NotNull LoanStatementRequestDto loanStatementRequest);

    @PostMapping("/calc")
    ResponseEntity<CreditDto> calculateCredit(@RequestBody @Valid @NotNull ScoringDataDto scoringData);
}
