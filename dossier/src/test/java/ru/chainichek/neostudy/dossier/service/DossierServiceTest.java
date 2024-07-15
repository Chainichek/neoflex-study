package ru.chainichek.neostudy.dossier.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.chainichek.neostudy.dossier.dto.admin.StatementDto;
import ru.chainichek.neostudy.dossier.dto.message.EmailMessage;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DossierServiceTest {
    @InjectMocks
    DossierService dossierService;

    @Mock
    DealService dealService;
    @Mock
    MailService mailService;
    @Mock
    DocumentGeneratorService documentGeneratorService;
    @Mock
    EmailMessage emailMessage;

    @Test
    void listenFinishRegistration() {
        assertDoesNotThrow(() -> dossierService.listenFinishRegistration(emailMessage));

        verify(mailService).sendSimpleMail(emailMessage.address(), emailMessage.theme());
    }

    @Test
    void listenCreateDocuments() {
        assertDoesNotThrow(() -> dossierService.listenCreateDocuments(emailMessage));

        verify(mailService).sendSimpleMail(emailMessage.address(), emailMessage.theme());
    }

    @Test
    void listenSendDocuments() {
        StatementDto statement = mock(StatementDto.class);
        byte[] file = new byte[]{};
        when(dealService.getStatement(emailMessage.statementId())).thenReturn(statement);
        when(documentGeneratorService.generateDocument(statement)).thenReturn(file);

        assertDoesNotThrow(() -> dossierService.listenSendDocuments(emailMessage));

        verify(dealService).getStatement(eq(emailMessage.statementId()));
        verify(documentGeneratorService).generateDocument(eq(statement));
        verify(mailService).sendDocumentMail(emailMessage.address(), emailMessage.statementId(), file);
    }

    @Test
    void listenSendSes() {
        StatementDto statement = mock(StatementDto.class);

        when(dealService.getStatement(emailMessage.statementId())).thenReturn(statement);

        assertDoesNotThrow(() -> dossierService.listenSendSes(emailMessage));

        verify(dealService).getStatement(eq(emailMessage.statementId()));
        verify(mailService).sendSesMail(emailMessage.address(), emailMessage.statementId(), statement.sesCode());
    }

    @Test
    void listenCreditIssued() {
        assertDoesNotThrow(() -> dossierService.listenCreditIssued(emailMessage));

        verify(mailService).sendSimpleMail(emailMessage.address(), emailMessage.theme());
    }

    @Test
    void listenStatementDenied() {
        assertDoesNotThrow(() -> dossierService.listenStatementDenied(emailMessage));

        verify(mailService).sendSimpleMail(emailMessage.address(), emailMessage.theme());
    }
}