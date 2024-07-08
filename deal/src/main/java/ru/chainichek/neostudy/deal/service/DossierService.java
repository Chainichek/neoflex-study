package ru.chainichek.neostudy.deal.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.chainichek.neostudy.deal.dto.dossier.EmailMessage;
import ru.chainichek.neostudy.deal.mapper.DossierMapper;
import ru.chainichek.neostudy.deal.model.dossier.EmailTheme;
import ru.chainichek.neostudy.deal.model.statement.Statement;
import ru.chainichek.neostudy.lib.loggerutils.annotation.ProducerLoggable;

@Service
@RequiredArgsConstructor
@ProducerLoggable
public class DossierService {
    private final KafkaTemplate<String, EmailMessage> kafkaTemplate;
    private final DossierMapper dossierMapper;
    private final ObjectMapper objectMapper;

    @Value("${app.kafka.topic.finish-registration}")
    private String finishRegistration;
    @Value("${app.kafka.topic.create-documents}")
    private String createDocuments;
    @Value("${app.kafka.topic.send-documents}")
    private String sendDocuments;
    @Value("${app.kafka.topic.send-ses}")
    private String sendSes;
    @Value("${app.kafka.topic.credit-issued}")
    private String creditIssued;
    @Value("${app.kafka.topic.statement-denied}")
    private String statementDenied;

    public void sendFinishRegistration(Statement statement) {
        kafkaTemplate.send(finishRegistration, dossierMapper.mapToEmailMessage(statement,
                EmailTheme.FINISH_REGISTRATION,
                null));
    }

    public void sendCreateDocuments(Statement statement) {
        kafkaTemplate.send(createDocuments, dossierMapper.mapToEmailMessage(statement,
                EmailTheme.CREATE_DOCUMENTS,
                null));
    }

    @SneakyThrows
    public void sendSendDocuments(Statement statement) {
        kafkaTemplate.send(sendDocuments, dossierMapper.mapToEmailMessage(statement,
                EmailTheme.SEND_DOCUMENTS,
                objectMapper.writeValueAsString(dossierMapper.mapToCreditDto(statement.getCredit()))));
    }

    public void sendSendSes(Statement statement) {
        kafkaTemplate.send(sendSes, dossierMapper.mapToEmailMessage(statement,
                EmailTheme.SEND_SES,
                statement.getSesCode()));
    }

    public void sendCreditIssued(Statement statement) {
        kafkaTemplate.send(creditIssued, dossierMapper.mapToEmailMessage(statement,
                EmailTheme.CREDIT_ISSUED,
                null));
    }

    public void sendStatementDenied(Statement statement) {
        kafkaTemplate.send(statementDenied, dossierMapper.mapToEmailMessage(statement,
                EmailTheme.STATEMENT_DENIED,
                null));
    }
}
