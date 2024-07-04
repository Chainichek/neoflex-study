package ru.chainichek.neostudy.deal.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.chainichek.neostudy.deal.exception.NotFoundException;
import ru.chainichek.neostudy.deal.model.statement.ApplicationStatus;
import ru.chainichek.neostudy.deal.model.statement.Statement;

import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class AdminService {
    private final StatementService statementService;

    public void updateStatus(@NonNull UUID statementId, @NonNull ApplicationStatus status) {
        final Statement statement = getStatement(statementId);
        statement.setStatus(status);
        statementService.updateStatement(statement);
        log.info("Update status for statement {}", statementId);
    }

    private Statement getStatement(@NonNull UUID statementId) {
        final Statement statement = statementService.getStatement(statementId);
        if (statement == null) {
            throw new NotFoundException(StatementService.ExceptionMessage.STATEMENT_NOT_FOUND_EXCEPTION_MESSAGE, statementId);
        }
        return statement;
    }
}
