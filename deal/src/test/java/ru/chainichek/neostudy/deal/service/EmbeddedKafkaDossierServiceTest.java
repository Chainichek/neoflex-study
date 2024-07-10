package ru.chainichek.neostudy.deal.service;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import ru.chainichek.neostudy.deal.dto.message.EmailMessage;
import ru.chainichek.neostudy.deal.mapper.DossierMapper;
import ru.chainichek.neostudy.deal.model.client.Client;
import ru.chainichek.neostudy.deal.model.dossier.EmailTheme;
import ru.chainichek.neostudy.deal.model.statement.Statement;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {
        "${app.kafka.topic.finish-registration}",
        "${app.kafka.topic.create-documents}",
        "${app.kafka.topic.send-documents}",
        "${app.kafka.topic.send-ses}",
        "${app.kafka.topic.credit-issued}",
        "${app.kafka.topic.statement-denied}"})
@DirtiesContext
@TestPropertySource(properties = "spring.liquibase.enabled=false")
public class EmbeddedKafkaDossierServiceTest {
    @Autowired
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

    Consumer<String, EmailMessage> consumer;
    DossierMapper dossierMapper =  Mappers.getMapper(DossierMapper.class);

    @BeforeEach
    void setUp(@Autowired EmbeddedKafkaBroker embeddedKafka) {
        Client client = mock(Client.class);
        when(statement.getId()).thenReturn(UUID.randomUUID());
        when(statement.getClient()).thenReturn(client);
        when(client.getEmail()).thenReturn("kafka@test.com");

        ReflectionTestUtils.setField(dossierService, "finishRegistration", finishRegistration);
        ReflectionTestUtils.setField(dossierService, "createDocuments", createDocuments);
        ReflectionTestUtils.setField(dossierService, "sendDocuments", sendDocuments);
        ReflectionTestUtils.setField(dossierService, "sendSes", sendSes);
        ReflectionTestUtils.setField(dossierService, "creditIssued", creditIssued);
        ReflectionTestUtils.setField(dossierService, "statementDenied", statementDenied);

        ReflectionTestUtils.setField(dossierService, "dossierMapper", dossierMapper);

        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("testGroup", "true", embeddedKafka);
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        consumerProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, EmailMessage.class);

        DefaultKafkaConsumerFactory<String, EmailMessage> consumerFactory = new DefaultKafkaConsumerFactory<>(consumerProps);

        consumer = consumerFactory.createConsumer();
        consumer.subscribe(List.of(finishRegistration, createDocuments, sendDocuments, sendSes, creditIssued, statementDenied));
    }

    @AfterEach
    void tearDown() {
        if (consumer != null) {
            consumer.close();
        }
    }

    @Test
    void testSendFinishRegistration() {
        EmailMessage emailMessage = dossierMapper.mapToEmailMessage(statement, EmailTheme.FINISH_REGISTRATION);

        dossierService.sendFinishRegistration(statement);

        ConsumerRecord<String, EmailMessage> received = KafkaTestUtils.getSingleRecord(consumer, finishRegistration);
        assertEquals(emailMessage, received.value());
    }

    @Test
    void testSendCreateDocuments() {
        EmailMessage emailMessage = dossierMapper.mapToEmailMessage(statement, EmailTheme.CREATE_DOCUMENTS);

        dossierService.sendCreateDocuments(statement);

        ConsumerRecord<String, EmailMessage> received = KafkaTestUtils.getSingleRecord(consumer, createDocuments);
        assertEquals(emailMessage, received.value());
    }

    @Test
    void testSendSendDocuments() {
        EmailMessage emailMessage = dossierMapper.mapToEmailMessage(statement, EmailTheme.SEND_DOCUMENTS);

        dossierService.sendSendDocuments(statement);

        ConsumerRecord<String, EmailMessage> received = KafkaTestUtils.getSingleRecord(consumer, sendDocuments);
        assertEquals(emailMessage, received.value());
    }

    @Test
    void testSendSendSes() {
        EmailMessage emailMessage = dossierMapper.mapToEmailMessage(statement, EmailTheme.SEND_SES);

        dossierService.sendSendSes(statement);

        ConsumerRecord<String, EmailMessage> received = KafkaTestUtils.getSingleRecord(consumer, sendSes);
        assertEquals(emailMessage, received.value());
    }

    @Test
    void testSendCreditIssued() {
        EmailMessage emailMessage = dossierMapper.mapToEmailMessage(statement, EmailTheme.CREDIT_ISSUED);

        dossierService.sendCreditIssued(statement);

        ConsumerRecord<String, EmailMessage> received = KafkaTestUtils.getSingleRecord(consumer, creditIssued);
        assertEquals(emailMessage, received.value());
    }

    @Test
    void testSendStatementDenied() {
        EmailMessage emailMessage = dossierMapper.mapToEmailMessage(statement, EmailTheme.STATEMENT_DENIED);

        dossierService.sendStatementDenied(statement);

        ConsumerRecord<String, EmailMessage> received = KafkaTestUtils.getSingleRecord(consumer, statementDenied);
        assertEquals(emailMessage, received.value());
    }


}