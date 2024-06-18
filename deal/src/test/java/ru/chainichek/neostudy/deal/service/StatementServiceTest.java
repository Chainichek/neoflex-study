package ru.chainichek.neostudy.deal.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import ru.chainichek.neostudy.deal.exception.NotFoundException;
import ru.chainichek.neostudy.deal.model.client.Client;
import ru.chainichek.neostudy.deal.model.statement.ApplicationStatus;
import ru.chainichek.neostudy.deal.model.statement.Statement;
import ru.chainichek.neostudy.deal.repo.StatementRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatementServiceTest {
    @InjectMocks
    StatementService statementService;
    @Mock
    StatementRepository statementRepository;

    @Test
    void getStatement_whenStatementExists_thenReturnStatement() {
        final UUID uuid = mock(UUID.class);

        when(statementRepository.findById(uuid)).thenReturn(Optional.of(mock(Statement.class)));

        assertNotNull(statementService.getStatement(uuid));
    }

    @Test
    void getStatement_whenStatementDoesNotExist_thenThrowNotFoundException() {
        final UUID uuid = mock(UUID.class);

        when(statementRepository.findById(uuid)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> statementService.getStatement(uuid));
    }

    @Test
    void createStatement() {
        final Client client = mock(Client.class);
        final Statement statement = Statement.builder()
                .client(client)
                .status(ApplicationStatus.PREAPPROVAL)
                .creationDate(LocalDateTime.now())
                .statusHistory(new ArrayList<>())
                .build();

        when(statementRepository.save(ArgumentMatchers.any())).thenAnswer((Answer<Statement>) invocation -> invocation.getArgument(0));

        final Statement createdStatement = statementService.createStatement(client);

        assertNotNull(createdStatement);
        assertThat(statement)
                .usingRecursiveComparison()
                .ignoringFields("creationDate")
                .isEqualTo(createdStatement);
    }

    @Test
    void updateStatement() {
        when(statementRepository.save(ArgumentMatchers.any())).thenReturn(null);

        assertDoesNotThrow(() -> statementService.updateStatement(mock(Statement.class)));
    }

    @Test
    void updateStatementOnDenied() {
        when(statementRepository.save(ArgumentMatchers.any())).thenReturn(null);

        assertDoesNotThrow(() -> statementService.updateStatementOnDenied(mock(Statement.class)));
    }
}