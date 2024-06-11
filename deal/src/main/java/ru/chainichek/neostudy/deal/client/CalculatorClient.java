package ru.chainichek.neostudy.deal.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.chainichek.neostudy.deal.config.FeignConfig;
import ru.chainichek.neostudy.deal.dto.calculation.CreditDto;
import ru.chainichek.neostudy.deal.dto.calculation.ScoringDataDto;
import ru.chainichek.neostudy.deal.dto.offer.LoanOfferDto;
import ru.chainichek.neostudy.deal.dto.offer.LoanStatementRequestDto;

import java.util.List;

@FeignClient(name = "calculator-service",
        url = "${app.client.calculator.url}",
        configuration = FeignConfig.class)
public interface CalculatorClient {
    @PostMapping("/offers")
    List<LoanOfferDto> getOffers(@RequestBody LoanStatementRequestDto request);

    @PostMapping("/calc")
    CreditDto calculateCredit(@RequestBody ScoringDataDto data);
}
