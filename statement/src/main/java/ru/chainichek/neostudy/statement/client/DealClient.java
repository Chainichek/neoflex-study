package ru.chainichek.neostudy.statement.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.chainichek.neostudy.statement.config.FeignConfig;
import ru.chainichek.neostudy.statement.dto.prescore.LoanOfferDto;
import ru.chainichek.neostudy.statement.dto.prescore.LoanStatementRequestDto;

import java.util.List;

@FeignClient(name = "calculator-service",
        url = "${app.client.deal.url}",
        path = "/deal",
        configuration = FeignConfig.class)
public interface DealClient {
    @PostMapping("/statement")
    List<LoanOfferDto> createStatement(@RequestBody LoanStatementRequestDto loanStatementRequest);

    @PostMapping("/offer/select")
    void selectOffer(@RequestBody LoanOfferDto loanOffer);
}
