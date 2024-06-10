package ru.chainichek.neostudy.deal.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.chainichek.neostudy.deal.client.CalculatorClient;
import ru.chainichek.neostudy.deal.dto.calculation.CreditDto;
import ru.chainichek.neostudy.deal.dto.calculation.ScoringDataDto;
import ru.chainichek.neostudy.deal.dto.offer.LoanOfferDto;
import ru.chainichek.neostudy.deal.dto.offer.LoanStatementRequestDto;
import ru.chainichek.neostudy.deal.dto.statement.EmploymentDto;
import ru.chainichek.neostudy.deal.model.client.Client;
import ru.chainichek.neostudy.deal.model.client.Passport;
import ru.chainichek.neostudy.deal.model.statement.Statement;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CalculatorService {
    private final CalculatorClient calculatorClient;

    public List<LoanOfferDto> getOffers(LoanStatementRequestDto loanStatementRequest, UUID statementId) {
        return calculatorClient.getOffers(loanStatementRequest)
                .stream()
                .map(x -> x.withStatementId(statementId))
                .toList();
    }

    public CreditDto calculateCredit(Statement statement, EmploymentDto employment) {
        final Client client = statement.getClient();
        final Passport passport = client.getPassport();
        final LoanOfferDto loanOffer = statement.getAppliedOffer();

        return calculatorClient.calculateCredit(new ScoringDataDto(loanOffer.totalAmount(),
                loanOffer.term(),
                client.getFirstName(),
                client.getLastName(),
                client.getMiddleName(),
                client.getGender(),
                client.getBirthdate(),
                passport.getSeries(),
                passport.getNumber(),
                passport.getIssueDate(),
                passport.getIssueBranch(),
                client.getMaritalStatus(),
                client.getDependentAmount(),
                employment,
                client.getAccountNumber(),
                loanOffer.isInsuranceEnabled(),
                loanOffer.isSalaryClient()));
    }
}
