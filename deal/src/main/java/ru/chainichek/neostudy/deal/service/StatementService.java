package ru.chainichek.neostudy.deal.service;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
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
    private final StatementRepository statementRepository;

    public Statement getStatement(@NotNull UUID statementId) {
        return statementRepository.findById(statementId)
                .orElseThrow(() -> new NotFoundException("Can't find the specified statement", statementId));
    }

    @Transactional
    public Statement createStatement(@NotNull Client client) {
        return statementRepository.save(new Statement(client));
    }

    @Transactional
    public void updateStatement(@NotNull Statement statement) {
        statementRepository.save(statement);
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void updateStatementOnDenied(@NotNull Statement statement) {
        statement.setStatus(ApplicationStatus.CC_DENIED);
        statementRepository.save(statement);
    }
}
