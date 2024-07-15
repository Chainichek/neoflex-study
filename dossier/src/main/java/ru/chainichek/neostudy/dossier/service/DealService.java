package ru.chainichek.neostudy.dossier.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.event.Level;
import org.springframework.stereotype.Service;
import ru.chainichek.neostudy.dossier.client.DealClient;
import ru.chainichek.neostudy.dossier.dto.admin.StatementDto;
import ru.chainichek.neostudy.dossier.model.statement.ApplicationStatus;
import ru.chainichek.neostudy.lib.loggerutils.annotation.Loggable;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Loggable(Level.DEBUG)
public class DealService {
    private final DealClient dealClient;

    public StatementDto getStatement(@NonNull UUID statementId) {
        return dealClient.getStatement(statementId);
    }

    public void updateStatementStatus(@NonNull UUID statementId) {
        dealClient.updateStatus(statementId, ApplicationStatus.DOCUMENTS_CREATED);
    }
}
