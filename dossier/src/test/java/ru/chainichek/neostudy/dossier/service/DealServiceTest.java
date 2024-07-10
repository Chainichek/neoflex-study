package ru.chainichek.neostudy.dossier.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.chainichek.neostudy.dossier.client.DealClient;
import ru.chainichek.neostudy.dossier.dto.admin.StatementDto;
import ru.chainichek.neostudy.dossier.model.statement.ApplicationStatus;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DealServiceTest {
    @InjectMocks
    DealService dealService;

    @Mock
    DealClient dealClient;
    @Mock
    UUID statementId;

    @Test
    void getStatement() {
        StatementDto statement = mock(StatementDto.class);
        when(dealClient.getStatement(statementId)).thenReturn(statement);

        StatementDto result = dealService.getStatement(statementId);

        assertEquals(statement, result);

        verify(dealClient).getStatement(eq(statementId));
    }

    @Test
    void updateStatementStatus() {
        dealService.updateStatementStatus(statementId);

        verify(dealClient).updateStatus(eq(statementId), eq(ApplicationStatus.DOCUMENTS_CREATED));
    }
}