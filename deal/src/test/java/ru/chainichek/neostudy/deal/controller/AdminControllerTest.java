package ru.chainichek.neostudy.deal.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.chainichek.neostudy.deal.model.statement.ApplicationStatus;
import ru.chainichek.neostudy.deal.model.statement.Statement;
import ru.chainichek.neostudy.deal.service.AdminService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @InjectMocks
    AdminController adminController;

    @Mock
    AdminService adminService;
    @Mock
    UUID statementId;

    @Test
    void getStatement() {
        Statement statement = mock(Statement.class);

        when(adminService.getStatement(statementId)).thenReturn(statement);

        Statement result = adminController.getStatement(statementId).getBody();

        assertEquals(statement, result);
        verify(adminService).getStatement(eq(statementId));
    }

    @Test
    void updateStatus() {
        adminController.updateStatus(statementId, ApplicationStatus.DOCUMENTS_CREATED);

        verify(adminService).updateStatus(eq(statementId), eq(ApplicationStatus.DOCUMENTS_CREATED));
    }
}