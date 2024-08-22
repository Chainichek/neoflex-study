package ru.chainichek.neostudy.deal.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.chainichek.neostudy.deal.dto.admin.StatementDto;
import ru.chainichek.neostudy.deal.exception.NotFoundException;
import ru.chainichek.neostudy.deal.mapper.AdminMapper;
import ru.chainichek.neostudy.deal.model.statement.ApplicationStatus;
import ru.chainichek.neostudy.deal.model.statement.Statement;
import ru.chainichek.neostudy.lib.loggerutils.annotation.TransactionLoggable;

import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class AdminService {
    private final StatementService statementService;
    private final AdminMapper adminMapper;

    public StatementDto getStatement(@NonNull UUID statementId) {
        return adminMapper.mapToStatementDto(getStatementEntity(statementId));
    }

    @Transactional
    @TransactionLoggable
    public void updateStatus(@NonNull UUID statementId, @NonNull ApplicationStatus status) {
        final Statement statement = getStatementEntity(statementId);
        statement.setStatus(status);
        statementService.updateStatement(statement);
        log.debug("Update status for statement {}", statementId);
    }

    private Statement getStatementEntity(@NonNull UUID statementId) {
        final Statement statement = statementService.getStatement(statementId);
        if (statement == null) {
            throw new NotFoundException(StatementService.ExceptionMessage.STATEMENT_NOT_FOUND_EXCEPTION_MESSAGE, statementId);
        }
        return statement;
    }
}
