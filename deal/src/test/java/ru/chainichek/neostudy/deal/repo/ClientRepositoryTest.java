package ru.chainichek.neostudy.deal.repo;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.chainichek.neostudy.deal.model.client.Client;
import ru.chainichek.neostudy.deal.model.client.Passport;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest(properties = {
        "spring.liquibase.enabled=false"
})
class ClientRepositoryTest {


    @Autowired
    ClientRepository clientRepository;

    @Test
    @Transactional
    void save() {
        final Client client = new Client(
                "Fedorov",
                "Ivan",
                null,
                LocalDate.of(2023, 3, 7),
                "ivanfedorov@yandex",
                new Passport("6161",
                        "345678"));

        final Client savedClient = clientRepository.save(client);
        final Client findedClient = clientRepository.findById(savedClient.getId()).orElse(null);

        assertNotNull(savedClient);
        assertNotNull(findedClient);

        assertThat(savedClient)
                .usingRecursiveComparison()
                .ignoringFieldsMatchingRegexes(".*id")
                .isEqualTo(client);


        assertThat(findedClient)
                .usingRecursiveComparison()
                .ignoringFieldsMatchingRegexes(".*id")
                .isEqualTo(client);
    }
}