package ru.chainichek.neostudy.deal.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import ru.chainichek.neostudy.deal.dto.message.EmailMessage;
import ru.chainichek.neostudy.deal.mapper.DossierMapper;
import ru.chainichek.neostudy.deal.model.dossier.EmailTheme;
import ru.chainichek.neostudy.deal.model.statement.Statement;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DossierServiceTest {

    @Mock
    KafkaTemplate<String, EmailMessage> kafkaTemplate;

    @Mock
    DossierMapper dossierMapper;

    @InjectMocks
    DossierService dossierService;

    @Value("${app.kafka.topic.finish-registration}")
    String finishRegistration;
    @Value("${app.kafka.topic.create-documents}")
    String createDocuments;
    @Value("${app.kafka.topic.send-documents}")
    String sendDocuments;
    @Value("${app.kafka.topic.send-ses}")
    String sendSes;
    @Value("${app.kafka.topic.credit-issued}")
    String creditIssued;
    @Value("${app.kafka.topic.statement-denied}")
    String statementDenied;

    @Mock
    Statement statement;
    @Mock
    EmailMessage emailMessage;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(dossierService, "finishRegistration", finishRegistration);
        ReflectionTestUtils.setField(dossierService, "createDocuments", createDocuments);
        ReflectionTestUtils.setField(dossierService, "sendDocuments", sendDocuments);
        ReflectionTestUtils.setField(dossierService, "sendSes", sendSes);
        ReflectionTestUtils.setField(dossierService, "creditIssued", creditIssued);
        ReflectionTestUtils.setField(dossierService, "statementDenied", statementDenied);
    }

    @Test
    void sendFinishRegistration() {
        when(dossierMapper.mapToEmailMessage(statement, EmailTheme.FINISH_REGISTRATION)).thenReturn(emailMessage);

        dossierService.sendFinishRegistration(statement);

        verify(kafkaTemplate).send(eq(finishRegistration), eq(emailMessage));
    }

    @Test
    void sendCreateDocuments() {
        when(dossierMapper.mapToEmailMessage(statement, EmailTheme.CREATE_DOCUMENTS)).thenReturn(emailMessage);

        dossierService.sendCreateDocuments(statement);

        verify(kafkaTemplate).send(eq(createDocuments), eq(emailMessage));
    }

    @Test
    void sendSendDocuments() {
        when(dossierMapper.mapToEmailMessage(statement, EmailTheme.SEND_DOCUMENTS)).thenReturn(emailMessage);

        dossierService.sendSendDocuments(statement);

        verify(kafkaTemplate).send(eq(sendDocuments), eq(emailMessage));
    }

    @Test
    void sendSendSes() {
        when(dossierMapper.mapToEmailMessage(statement, EmailTheme.SEND_SES)).thenReturn(emailMessage);

        dossierService.sendSendSes(statement);

        verify(kafkaTemplate).send(eq(sendSes), eq(emailMessage));
    }

    @Test
    void sendCreditIssued() {
        when(dossierMapper.mapToEmailMessage(statement, EmailTheme.CREDIT_ISSUED)).thenReturn(emailMessage);

        dossierService.sendCreditIssued(statement);

        verify(kafkaTemplate).send(eq(creditIssued), eq(emailMessage));
    }

    @Test
    void sendStatementDenied() {
        when(dossierMapper.mapToEmailMessage(statement, EmailTheme.STATEMENT_DENIED)).thenReturn(emailMessage);

        dossierService.sendStatementDenied(statement);

        verify(kafkaTemplate).send(eq(statementDenied), eq(emailMessage));
    }
}