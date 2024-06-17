package ru.chainichek.neostudy.deal.service;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.chainichek.neostudy.deal.exception.NotFoundException;
import ru.chainichek.neostudy.deal.model.client.Client;
import ru.chainichek.neostudy.deal.model.statement.ApplicationStatus;
import ru.chainichek.neostudy.deal.model.statement.Statement;
import ru.chainichek.neostudy.deal.repo.StatementRepository;

import java.util.UUID;

@Service
@AllArgsConstructor
public class StatementService {
    private final static Logger LOG = LoggerFactory.getLogger(StatementService.class);

    private final StatementRepository statementRepository;

    public Statement getStatement(@NotNull UUID statementId) {
        return statementRepository.findById(statementId)
                .orElseThrow(() -> new NotFoundException("Can't find the specified statement", statementId));
    }

    @Transactional
    public Statement createStatement(@NotNull Client client) {
        final Statement statement = statementRepository.save(Statement.builder().client(client).build());

        LOG.debug("Created a statement: statementId = %s".formatted(statement.getId()));

        return statement;
    }

    @Transactional
    public void updateStatement(@NotNull Statement statement) {
        statementRepository.save(statement);

        LOG.debug("Updated a statement: statementId = %s".formatted(statement.getId()));
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void updateStatementOnDenied(@NotNull Statement statement) {
        LOG.debug("Interrupting statement update transaction with a new one in order to set status to CC_DENIED: statementId = %s"
                .formatted(statement.getId()));

        statement.setStatus(ApplicationStatus.CC_DENIED);
        statementRepository.save(statement);

        LOG.debug("Updated a statement, continuing the transaction: statementId = %s".formatted(statement.getId()));
    }
}
