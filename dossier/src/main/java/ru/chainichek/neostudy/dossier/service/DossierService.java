package ru.chainichek.neostudy.dossier.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.chainichek.neostudy.dossier.dto.message.EmailMessage;
import ru.chainichek.neostudy.dossier.dto.admin.StatementDto;
import ru.chainichek.neostudy.lib.loggerutils.annotation.ConsumerLoggable;

@Service
@AllArgsConstructor
@ConsumerLoggable
public class DossierService {
    private final DealService dealService;
    private final MailService mailService;
    private final DocumentGeneratorService documentGeneratorService;

    @KafkaListener(topics = "${app.kafka.topic.finish-registration}", groupId = "${spring.kafka.consumer.group-id}")
    public void listenFinishRegistration(@NonNull EmailMessage message) {
        mailService.sendSimpleMail(message.address(), message.theme());
    }

    @KafkaListener(topics = "${app.kafka.topic.create-documents}", groupId = "${spring.kafka.consumer.group-id}")
    public void listenCreateDocuments(@NonNull EmailMessage message) {
        mailService.sendSimpleMail(message.address(), message.theme());
    }

    @KafkaListener(topics = "${app.kafka.topic.send-documents}", groupId = "${spring.kafka.consumer.group-id}")
    @Transactional
    public void listenSendDocuments(@NonNull EmailMessage message) {
        final StatementDto statement = dealService.getStatement(message.statementId());
        mailService.sendDocumentMail(message.address(), message.statementId(),
                documentGeneratorService.generateDocument(statement));
        dealService.updateStatementStatus(message.statementId());
    }

    @KafkaListener(topics = "${app.kafka.topic.send-ses}", groupId = "${spring.kafka.consumer.group-id}")
    @Transactional
    public void listenSendSes(@NonNull EmailMessage message) {
        final StatementDto statement = dealService.getStatement(message.statementId());
        mailService.sendSesMail(message.address(), message.statementId(), statement.sesCode());
    }

    @KafkaListener(topics = "${app.kafka.topic.credit-issued}", groupId = "${spring.kafka.consumer.group-id}")
    public void listenCreditIssued(@NonNull EmailMessage message) {
        mailService.sendSimpleMail(message.address(),message.theme());
    }

    @KafkaListener(topics = "${app.kafka.topic.statement-denied}", groupId = "${spring.kafka.consumer.group-id}")
    public void listenStatementDenied(@NonNull EmailMessage message) {
        mailService.sendSimpleMail(message.address(), message.theme());
    }
}
