package ru.chainichek.neostudy.deal.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.chainichek.neostudy.deal.exception.ForbiddenException;
import ru.chainichek.neostudy.deal.exception.NotFoundException;
import ru.chainichek.neostudy.deal.exception.WrongStatusException;
import ru.chainichek.neostudy.deal.model.statement.ApplicationStatus;
import ru.chainichek.neostudy.deal.model.statement.Statement;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {
    @InjectMocks
    DocumentService documentService;
    @Mock
    StatementService statementService;
    @Mock
    DossierService dossierService;
    @Mock
    SignatureService signatureService;

    @Test
    void sendDocuments_whenStatusIsCorrect_thenDoesNotThrow() {
        Statement statement = mock(Statement.class);
        when(statementService.getStatement(any())).thenReturn(statement);
        when(statement.getStatus()).thenReturn(ApplicationStatus.CC_APPROVED);
        assertDoesNotThrow(() -> documentService.sendDocuments(mock(UUID.class)));

        verify(dossierService).sendSendDocuments(eq(statement));
    }

    @Test
    void sendDocuments_whenStatusIsNotCorrect_thenThrowWrongStatusException() {
        Statement statement = mock(Statement.class);
        when(statementService.getStatement(any())).thenReturn(statement);
        when(statement.getStatus()).thenReturn(ApplicationStatus.PREAPPROVAL);
        assertThrows(WrongStatusException.class, () -> documentService.sendDocuments(mock(UUID.class)));
    }

    @Test
    void sendDocuments_whenStatementIsNull_thenThrowNotFoundException() {
        when(statementService.getStatement(any())).thenReturn(null);
        assertThrows(NotFoundException.class, () -> documentService.sendDocuments(mock(UUID.class)));
    }

    @Test
    void signDocuments_whenStatusIsCorrect_thenDoesNotThrow() {
        Statement statement = mock(Statement.class);
        when(statementService.getStatement(any())).thenReturn(statement);
        when(statement.getStatus()).thenReturn(ApplicationStatus.DOCUMENTS_CREATED);
        when(signatureService.generateSignature()).thenReturn("");
        assertDoesNotThrow(() -> documentService.signDocuments(mock(UUID.class)));

        verify(dossierService).sendSendSes(eq(statement));
    }

    @Test
    void signDocuments_whenStatusIsNotCorrect_thenThrowWrongStatusException() {
        Statement statement = mock(Statement.class);
        when(statementService.getStatement(any())).thenReturn(statement);
        when(statement.getStatus()).thenReturn(ApplicationStatus.PREAPPROVAL);
        assertThrows(WrongStatusException.class, () -> documentService.signDocuments(mock(UUID.class)));
    }

    @Test
    void signDocuments_whenStatementIsNull_thenThrowNotFoundException() {
        when(statementService.getStatement(any())).thenReturn(null);
        assertThrows(NotFoundException.class, () -> documentService.signDocuments(mock(UUID.class)));
    }

    @Test
    void verifyCode_whenStatusAndCodeIsCorrect_thenDoesNotThrow() {
        Statement statement = mock(Statement.class);
        when(statementService.getStatement(any())).thenReturn(statement);
        when(statement.getStatus()).thenReturn(ApplicationStatus.DOCUMENTS_CREATED);
        when(signatureService.verifySignature(any(), any())).thenReturn(true);
        assertDoesNotThrow(() -> documentService.verifyCode(mock(UUID.class), ""));

        verify(dossierService).sendCreditIssued(eq(statement));
    }

    @Test
    void verifyCode_whenStatusIsNotCorrect_thenThrowWrongStatusException() {
        Statement statement = mock(Statement.class);
        when(statementService.getStatement(any())).thenReturn(statement);
        when(statement.getStatus()).thenReturn(ApplicationStatus.PREAPPROVAL);
        assertThrows(WrongStatusException.class, () -> documentService.verifyCode(mock(UUID.class), ""));
    }

    @Test
    void verifyCode_whenCodeIsNotCorrect_thenThrowForbiddenException() {
        Statement statement = mock(Statement.class);
        when(statementService.getStatement(any())).thenReturn(statement);
        when(statement.getStatus()).thenReturn(ApplicationStatus.DOCUMENTS_CREATED);
        when(signatureService.verifySignature(any(), any())).thenReturn(false);
        assertThrows(ForbiddenException.class, () -> documentService.verifyCode(mock(UUID.class), ""));
    }

    @Test
    void verifyCode_whenStatementIsNull_thenThrowNotFoundException() {
        when(statementService.getStatement(any())).thenReturn(null);
        assertThrows(NotFoundException.class, () -> documentService.verifyCode(mock(UUID.class), ""));
    }
}