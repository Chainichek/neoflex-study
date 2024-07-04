package ru.chainichek.neostudy.deal.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.chainichek.neostudy.deal.exception.ForbiddenException;
import ru.chainichek.neostudy.deal.exception.NotFoundException;
import ru.chainichek.neostudy.deal.exception.WrongStatusException;
import ru.chainichek.neostudy.deal.model.statement.ApplicationStatus;
import ru.chainichek.neostudy.deal.model.statement.Statement;

import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class DocumentService {
    private final StatementService statementService;

    private final DossierService dossierService;

    private final SignatureService signatureService;

    public void sendDocuments(@NonNull UUID statementId) {
        log.debug("Starting a transaction in order to send documents");

        final Statement statement = getStatement(statementId);
        if (statement.getStatus() != ApplicationStatus.CC_APPROVED) {
            log.debug(LogMessage.EXPECTED_CC_APPROVED_APPLICATION_STATUS_LOG_MESSAGE,
                    statement.getStatus()
            );

            throw new WrongStatusException(ExceptionMessage.EXPECTED_CC_APPROVED_APPLICATION_STATUS_EXCEPTION_MESSAGE,
                    statement.getId(),
                    statement.getStatus());
        }

        dossierService.sendSendDocuments(statement);

        log.debug("Ending the transaction");
    }

    @Transactional
    public void signDocuments(@NonNull UUID statementId) {
        log.debug("Starting a transaction in order to sign documents");

        final Statement statement = getStatement(statementId);
        if (statement.getStatus() != ApplicationStatus.DOCUMENTS_CREATED) {
            log.debug(LogMessage.EXPECTED_DOCUMENTS_CREATED_APPLICATION_STATUS_LOG_MESSAGE,
                    statement.getStatus()
            );

            throw new WrongStatusException(ExceptionMessage.EXPECTED_DOCUMENTS_CREATED_APPLICATION_STATUS_EXCEPTION_MESSAGE,
                    statement.getId(),
                    statement.getStatus());
        }

        statement.setSesCode(signatureService.generateSignature());
        statementService.updateStatement(statement);

        dossierService.sendSendSes(statement);

        log.debug("Ending the transaction");
    }

    @Transactional
    public void verifyCode(@NonNull UUID statementId, @NonNull String code) {
        log.debug("Starting a transaction in order to verify code");

        final Statement statement = getStatement(statementId);
        if (statement.getStatus() != ApplicationStatus.DOCUMENTS_CREATED) {
            log.debug(LogMessage.EXPECTED_DOCUMENTS_CREATED_APPLICATION_STATUS_LOG_MESSAGE,
                    statement.getStatus()
            );

            throw new WrongStatusException(ExceptionMessage.EXPECTED_DOCUMENTS_CREATED_APPLICATION_STATUS_EXCEPTION_MESSAGE,
                    statement.getId(),
                    statement.getStatus());
        }

        if (!signatureService.verifySignature(code, statement.getSesCode())) {
            log.debug(LogMessage.INVALID_SES_CODE_LOG_MESSAGE);
            throw new ForbiddenException(ExceptionMessage.INVALID_SES_CODE_MESSAGE.formatted(statement.getId()));
        }

        statement.setStatus(ApplicationStatus.DOCUMENTS_SIGNED);
        statement.setStatus(ApplicationStatus.CREDIT_ISSUED);

        statementService.updateStatement(statement);

        dossierService.sendCreateDocuments(statement);

        log.debug("Ending the transaction");
    }

    private static final class ExceptionMessage {
        public static final String EXPECTED_CC_APPROVED_APPLICATION_STATUS_EXCEPTION_MESSAGE = "Can't produce the request for statement that was not calculated";
        public static final String EXPECTED_DOCUMENTS_CREATED_APPLICATION_STATUS_EXCEPTION_MESSAGE = "Can't produce the request for statement which documents was not created or was signed";
        public static final String INVALID_SES_CODE_MESSAGE = "Can't sign the document because the ses code is invalid for the statement with id = %s";
    }

    private static final class LogMessage {
        public static final String EXPECTED_CC_APPROVED_APPLICATION_STATUS_LOG_MESSAGE = "Can't proceed further and throwing exception because statement.status = {} and not CC_APPROVED";
        public static final String EXPECTED_DOCUMENTS_CREATED_APPLICATION_STATUS_LOG_MESSAGE = "Can't proceed further and throwing exception because statement.status = {} and not DOCUMENTS_CREATED";
        public static final String INVALID_SES_CODE_LOG_MESSAGE = "Can't proceed further and throwing exception because code and statement.sesCode not equals";
    }

    private Statement getStatement(@NonNull UUID statementId) {
        final Statement statement = statementService.getStatement(statementId);
        if (statement == null) {
            throw new NotFoundException(StatementService.ExceptionMessage.STATEMENT_NOT_FOUND_EXCEPTION_MESSAGE, statementId);
        }
        return statement;
    }
}
