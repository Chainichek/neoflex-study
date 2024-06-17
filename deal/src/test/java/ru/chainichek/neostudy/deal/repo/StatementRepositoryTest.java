package ru.chainichek.neostudy.deal.repo;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import ru.chainichek.neostudy.deal.model.client.Client;
import ru.chainichek.neostudy.deal.model.client.Passport;
import ru.chainichek.neostudy.deal.model.statement.Statement;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@TestPropertySource(properties = "spring.liquibase.enabled=false")
class StatementRepositoryTest {

    @Autowired
    StatementRepository statementRepository;
    @Autowired
    ClientRepository clientRepository;

    @Test
    @Transactional
    void save() {
        final Statement statement = Statement.builder().client(clientRepository.save(Client.builder()
                .lastName("Fedorov")
                .firstName("Ivan")
                .birthdate(LocalDate.of(2023, 3, 7))
                .email("ivanfedorov@yandex")
                .passport(
                        Passport.builder()
                                .series("6161")
                                .number("345678")
                                .build()
                )
                .build())
        ).build();

        final Statement savedStatement = statementRepository.save(statement);
        final Statement findedStatement = statementRepository.findById(savedStatement.getId()).orElse(null);

        assertNotNull(savedStatement);
        assertNotNull(findedStatement);

        assertThat(savedStatement)
                .usingRecursiveComparison()
                .ignoringFieldsMatchingRegexes(".*id")
                .isEqualTo(statement);


        assertThat(findedStatement)
                .usingRecursiveComparison()
                .ignoringFieldsMatchingRegexes(".*id")
                .isEqualTo(statement);
    }
}