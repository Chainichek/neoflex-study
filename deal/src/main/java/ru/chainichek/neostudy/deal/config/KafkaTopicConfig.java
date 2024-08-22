package ru.chainichek.neostudy.deal.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

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

    @Bean
    public KafkaAdmin kafkaAdmin() {
        final Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic finishRegistrationTopic() {
        return new NewTopic(finishRegistration, 1, (short) 1);
    }
    @Bean
    public NewTopic createDocumentsTopic() {
        return new NewTopic(createDocuments, 1, (short) 1);
    }
    @Bean
    public NewTopic sendDocumentsTopic() {
        return new NewTopic(sendDocuments, 1, (short) 1);
    }
    @Bean
    public NewTopic sendSeTopic() {
        return new NewTopic(sendSes, 1, (short) 1);
    }
    @Bean
    public NewTopic creditIssuedTopic() {
        return new NewTopic(creditIssued, 1, (short) 1);
    }
    @Bean
    public NewTopic statementDeniedTopic() {
        return new NewTopic(statementDenied, 1, (short) 1);
    }

}
