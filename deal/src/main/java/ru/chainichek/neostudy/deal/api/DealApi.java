package ru.chainichek.neostudy.deal.api;

import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.chainichek.neostudy.deal.dto.offer.LoanOfferDto;
import ru.chainichek.neostudy.deal.dto.offer.LoanStatementRequestDto;
import ru.chainichek.neostudy.deal.dto.statement.FinishRegistrationRequestDto;

import java.util.List;
import java.util.UUID;

@RequestMapping("/deal")
public interface DealApi {
    @PostMapping("/statement")
    ResponseEntity<List<LoanOfferDto>> createStatement(@RequestBody @NotNull LoanStatementRequestDto loanStatementRequest);

    @PostMapping("/offer/select")
    ResponseEntity<Void> selectOffer(@RequestBody @NotNull LoanOfferDto loanOffer);

    @PostMapping("/calculate/{statementId}")
    ResponseEntity<Void> completeStatement(@PathVariable UUID statementId,
                                           @RequestBody @NotNull FinishRegistrationRequestDto finishRegistrationRequest);
}
