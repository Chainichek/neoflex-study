package ru.chainichek.neostudy.deal.service;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final static Logger LOG = LoggerFactory.getLogger(DealService.class);

    private final CalculatorService calculatorService;

    private final StatementService statementService;
    private final ClientService clientService;
    private final CreditService creditService;

    @Transactional
    public List<LoanOfferDto> createStatement(@NotNull LoanStatementRequestDto request) {
        LOG.debug("Starting a transaction in order to create statement");

        final Statement statement = statementService.createStatement(clientService.createClient(request));
        final List<LoanOfferDto> offers = calculatorService.getOffers(request, statement.getId());

        LOG.debug("Ending the transaction");

        return offers;
    }

    @Transactional
    public void selectOffer(@NotNull LoanOfferDto loanOffer) {
        LOG.debug("Starting a transaction in order to select offer for statement");

        final Statement statement = statementService.getStatement(loanOffer.statementId());
        if (statement.getStatus() != ApplicationStatus.PREAPPROVAL) {
            LOG.debug("Can't proceed further and throwing exception because finishRegistrationRequest.status = %s and not PREAPPROVAL"
                    .formatted(statement.getStatus()));

            throw new ForbiddenException("Can't apply offer for statement that already was approved",
                    statement.getId(),
                    statement.getStatus());
        }
        statement.setAppliedOffer(loanOffer);
        statement.setStatus(ApplicationStatus.APPROVED);

        statementService.updateStatement(statement);

        LOG.debug("Ending the transaction");
    }

    @Transactional
    public void completeStatement(@NotNull UUID statementId,
                                  @NotNull FinishRegistrationRequestDto finishRegistrationRequest) {
        LOG.debug("Starting a transaction in order to complete the statement");

        final Statement statement = statementService.getStatement(statementId);

        if (statement.getStatus() != ApplicationStatus.APPROVED) {
            LOG.debug("Can't proceed further and throwing exception because finishRegistrationRequest.status = %s and not APPROVED"
                    .formatted(statement.getStatus()));

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
            LOG.debug("Can't proceed further, setting statement status to denied and throwing exception because the exception was caught in scoring process");

            statementService.updateStatementOnDenied(statement);

            throw e;
        }

        statement.setStatus(ApplicationStatus.CC_APPROVED);
        statementService.updateStatement(statement);

        LOG.debug("Ending the transaction");
    }
}
