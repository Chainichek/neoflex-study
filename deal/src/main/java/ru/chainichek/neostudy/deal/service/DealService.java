package ru.chainichek.neostudy.deal.service;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.chainichek.neostudy.deal.dto.offer.LoanOfferDto;
import ru.chainichek.neostudy.deal.dto.offer.LoanStatementRequestDto;
import ru.chainichek.neostudy.deal.dto.statement.FinishRegistrationRequestDto;
import ru.chainichek.neostudy.deal.exception.ForbiddenException;
import ru.chainichek.neostudy.deal.exception.ValidationException;
import ru.chainichek.neostudy.deal.model.statement.ApplicationStatus;
import ru.chainichek.neostudy.deal.model.statement.Statement;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class DealService {
    private final CalculatorService calculatorService;

    private final StatementService statementService;
    private final ClientService clientService;
    private final CreditService creditService;

    @Transactional
    public List<LoanOfferDto> createStatement(@NotNull LoanStatementRequestDto loanStatementRequest) {
        final Statement statement = statementService.createStatement(clientService.createClient(loanStatementRequest));
        return calculatorService.getOffers(loanStatementRequest, statement.getId());
    }

    @Transactional
    public void selectOffer(@NotNull LoanOfferDto loanOffer) {
        final Statement statement = statementService.getStatement(loanOffer.statementId());
        if (statement.getStatus() != ApplicationStatus.PREAPPROVAL) {
            throw new ForbiddenException("Can't apply offer for statement that already was approved",
                    statement.getId(),
                    statement.getStatus());
        }
        statement.setAppliedOffer(loanOffer);
        statement.setStatus(ApplicationStatus.APPROVED);

        statementService.updateStatement(statement);
    }

    @Transactional
    public void completeStatement(@NotNull UUID statementId,
                                  @NotNull FinishRegistrationRequestDto finishRegistrationRequest) {
        final Statement statement = statementService.getStatement(statementId);
        if (statement.getStatus() != ApplicationStatus.APPROVED) {
            throw new ForbiddenException("Can't complete statement that already was completed or not approved",
                    statement.getId(),
                    statement.getStatus());
        }

        statement.setClient(clientService.updateClientOnFinishRegistration(statement.getClient(), finishRegistrationRequest));

        try {
            statement.setCredit(
                    creditService.createCredit(calculatorService.calculateCredit(statement, finishRegistrationRequest.employment()))
            );
        } catch (ValidationException | ForbiddenException e) {
            statementService.updateStatementOnDenied(statement);

            throw e;
        }

        statement.setStatus(ApplicationStatus.CC_APPROVED);
        statementService.updateStatement(statement);
    }
}
