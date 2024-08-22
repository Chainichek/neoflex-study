package ru.chainichek.neostudy.statement.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.chainichek.neostudy.statement.config.FeignConfig;
import ru.chainichek.neostudy.statement.dto.prescore.LoanOfferDto;
import ru.chainichek.neostudy.statement.dto.prescore.LoanStatementRequestDto;

import java.util.List;

@FeignClient(name = "calculator-service",
        url = "${app.client.deal.url}",
        path = "${app.client.deal.path.base-path}",
        configuration = FeignConfig.class)
public interface DealClient {
    @RequestMapping(method = RequestMethod.POST, value = "${app.client.deal.path.create-statement}")
    List<LoanOfferDto> createStatement(@RequestBody LoanStatementRequestDto loanStatementRequest);

    @RequestMapping(method = RequestMethod.POST, value = "${app.client.deal.path.select-offer}")
    void selectOffer(@RequestBody LoanOfferDto loanOffer);
}
