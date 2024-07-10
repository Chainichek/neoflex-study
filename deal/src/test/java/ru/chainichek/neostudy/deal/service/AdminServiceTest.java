package ru.chainichek.neostudy.deal.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.chainichek.neostudy.deal.dto.admin.StatementDto;
import ru.chainichek.neostudy.deal.exception.NotFoundException;
import ru.chainichek.neostudy.deal.mapper.AdminMapper;
import ru.chainichek.neostudy.deal.model.statement.ApplicationStatus;
import ru.chainichek.neostudy.deal.model.statement.Statement;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {
    @InjectMocks
    AdminService adminService;
    @Mock
    StatementService statementService;
    @Mock
    AdminMapper adminMapper;

    @Test
    void getStatement_whenStatementIsNotNull_returnStatement() {
        Statement statementEntity = mock(Statement.class);
        StatementDto statement = mock(StatementDto.class);
        when(statementService.getStatement(any())).thenReturn(statementEntity);
        when(adminMapper.mapToStatementDto(statementEntity)).thenReturn(statement);
        assertEquals(statement, adminService.getStatement(mock(UUID.class)));
    }

    @Test
    void getStatement_whenStatementIsNull_throwNotFoundException() {
        when(statementService.getStatement(any())).thenReturn(null);

        assertThrows(NotFoundException.class, () -> adminService.getStatement(mock(UUID.class)));
    }

    @Test
    void updateStatus() {
        Statement statement = mock(Statement.class);
        when(statementService.getStatement(any())).thenReturn(statement);

        assertDoesNotThrow(() -> adminService.updateStatus(mock(UUID.class), mock(ApplicationStatus.class)));
    }
}