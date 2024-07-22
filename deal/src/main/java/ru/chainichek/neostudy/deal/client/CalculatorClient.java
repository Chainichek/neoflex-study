package ru.chainichek.neostudy.deal.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.chainichek.neostudy.deal.config.FeignConfig;
import ru.chainichek.neostudy.deal.dto.calculation.CreditDto;
import ru.chainichek.neostudy.deal.dto.calculation.ScoringDataDto;
import ru.chainichek.neostudy.deal.dto.offer.LoanOfferDto;
import ru.chainichek.neostudy.deal.dto.offer.LoanStatementRequestDto;

import java.util.List;

@FeignClient(name = "calculator-service",
        url = "${app.client.calculator.url}",
        path = "${app.client.calculator.path.base-path}",
        configuration = FeignConfig.class)
public interface CalculatorClient {
    @RequestMapping(method = RequestMethod.POST, value = "${app.client.calculator.path.get-offers}")
    List<LoanOfferDto> getOffers(@RequestBody LoanStatementRequestDto request);

    @RequestMapping(method = RequestMethod.POST, value = "${app.client.calculator.path.calculate-credit}")
    CreditDto calculateCredit(@RequestBody ScoringDataDto data);
}
