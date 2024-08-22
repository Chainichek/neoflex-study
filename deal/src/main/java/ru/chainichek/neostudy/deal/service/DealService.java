package ru.chainichek.neostudy.deal.service;

import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.chainichek.neostudy.deal.dto.offer.LoanOfferDto;
import ru.chainichek.neostudy.deal.dto.offer.LoanStatementRequestDto;
import ru.chainichek.neostudy.deal.dto.statement.FinishRegistrationRequestDto;
import ru.chainichek.neostudy.deal.exception.NotFoundException;
import ru.chainichek.neostudy.deal.exception.WrongStatusException;
import ru.chainichek.neostudy.deal.model.statement.ApplicationStatus;
import ru.chainichek.neostudy.deal.model.statement.Statement;
import ru.chainichek.neostudy.lib.loggerutils.annotation.TransactionLoggable;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class DealService {
    private final CalculatorService calculatorService;

    private final StatementService statementService;
    private final ClientService clientService;
    private final CreditService creditService;

    private final DossierService dossierService;

    @Transactional
    @TransactionLoggable
    public List<LoanOfferDto> createStatement(@NonNull LoanStatementRequestDto request) {
        final Statement statement = statementService.createStatement(clientService.createClient(request));
        return calculatorService.getOffers(request, statement.getId());
    }

    @Transactional
    @TransactionLoggable
    public void selectOffer(@NonNull LoanOfferDto loanOffer) {
        final Statement statement = getStatement(loanOffer.statementId());

        if (statement.getStatus() != ApplicationStatus.PREAPPROVAL) {
            log.debug(LogMessage.EXPECTED_PREAPPROVAL_APPLICATION_STATUS_LOG_MESSAGE,
                    statement.getStatus());

            throw new WrongStatusException(ExceptionMessage.EXPECTED_PREAPPROVAL_APPLICATION_STATUS_EXCEPTION_MESSAGE,
                    statement.getId(),
                    statement.getStatus());
        }
        statement.setAppliedOffer(loanOffer);
        statement.setStatus(ApplicationStatus.APPROVED);

        statementService.updateStatement(statement);

        dossierService.sendFinishRegistration(statement);
    }

    @Transactional
    @TransactionLoggable
    public void completeStatement(@NonNull UUID statementId,
                                  @NonNull FinishRegistrationRequestDto finishRegistrationRequest) {
        final Statement statement = getStatement(statementId);

        if (statement.getStatus() != ApplicationStatus.APPROVED) {
            log.debug(LogMessage.EXPECTED_APPROVED_APPLICATION_STATUS_LOG_MESSAGE,
                    statement.getStatus());

            throw new WrongStatusException(ExceptionMessage.EXPECTED_APPROVED_APPLICATION_STATUS_EXCEPTION_MESSAGE,
                    statement.getId(),
                    statement.getStatus());
        }

        statement.setClient(clientService.updateClientOnFinishRegistration(statement.getClient(), finishRegistrationRequest));

        try {
            statement.setCredit(
                    creditService.createCredit(calculatorService.calculateCredit(statement, finishRegistrationRequest.employment()))
            );
        } catch (FeignException e) {
            if (e.status() == 400 || e.status() == 403) {
                log.debug(LogMessage.STATEMENT_WAS_REJECTED_LOG_MESSAGE);

                statementService.updateStatementOnDenied(statement);
            }

            throw e;
        }

        statement.setStatus(ApplicationStatus.CC_APPROVED);
        statementService.updateStatement(statement);

        dossierService.sendCreateDocuments(statement);

    }

    private static final class ExceptionMessage {
        public static final String EXPECTED_PREAPPROVAL_APPLICATION_STATUS_EXCEPTION_MESSAGE = "Can't apply offer for statement that already was approved";
        public static final String EXPECTED_APPROVED_APPLICATION_STATUS_EXCEPTION_MESSAGE = "Can't complete statement that already was completed or not approved";
    }

    private static final class LogMessage {
        public static final String EXPECTED_PREAPPROVAL_APPLICATION_STATUS_LOG_MESSAGE = "Can't proceed further and throwing exception because statement.status = {} and not PREAPPROVAL";
        public static final String EXPECTED_APPROVED_APPLICATION_STATUS_LOG_MESSAGE = "Can't proceed further and throwing exception because statement.status = {} and not APPROVED";
        public static final String STATEMENT_WAS_REJECTED_LOG_MESSAGE = "Can't proceed further, setting statement status to denied and throwing exception because the exception was caught in scoring process";

    }

    private Statement getStatement(@NonNull UUID statementId) {
        final Statement statement = statementService.getStatement(statementId);
        if (statement == null) {
            throw new NotFoundException(StatementService.ExceptionMessage.STATEMENT_NOT_FOUND_EXCEPTION_MESSAGE, statementId);
        }
        return statement;
    }
}
