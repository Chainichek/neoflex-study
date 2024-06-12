package ru.chainichek.neostudy.deal.repo;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.chainichek.neostudy.deal.dto.calculation.PaymentScheduleElementDto;
import ru.chainichek.neostudy.deal.model.credit.Credit;
import ru.chainichek.neostudy.deal.model.credit.CreditStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class CreditRepositoryTest {
    @Container
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }

    @Autowired
    CreditRepository creditRepository;

    @Test
    @Transactional
    void save() {
        final Credit credit = new Credit(new BigDecimal("30000"),
                        6,
                        new BigDecimal("5176.45"),
                        new BigDecimal("12"),
                        new BigDecimal("7.06"),
                List.of(
                        new PaymentScheduleElementDto(
                                1,
                                LocalDate.of(2024, 6, 1),
                                new BigDecimal("5176.45"),
                                new BigDecimal("300"),
                                new BigDecimal("4876.45"),
                                new BigDecimal("25123.55")
                        ),
                        new PaymentScheduleElementDto(
                                2,
                                LocalDate.of(2024, 7, 1),
                                new BigDecimal("10352.9"),
                                new BigDecimal("251.24"),
                                new BigDecimal("4925.22"),
                                new BigDecimal("20198.33")
                        ),
                        new PaymentScheduleElementDto(
                                3,
                                LocalDate.of(2024, 8, 1),
                                new BigDecimal("15529.35"),
                                new BigDecimal("201.98"),
                                new BigDecimal("4974.47"),
                                new BigDecimal("15223.87")
                        ),
                        new PaymentScheduleElementDto(
                                4,
                                LocalDate.of(2024, 9, 1),
                                new BigDecimal("20705.8"),
                                new BigDecimal("152.24"),
                                new BigDecimal("5024.21"),
                                new BigDecimal("10199.65")
                        ),
                        new PaymentScheduleElementDto(
                                5,
                                LocalDate.of(2024, 10, 1),
                                new BigDecimal("25882.26"),
                                new BigDecimal("102"),
                                new BigDecimal("5074.45"),
                                new BigDecimal("5125.2")
                        ),
                        new PaymentScheduleElementDto(
                                6,
                                LocalDate.of(2024, 11, 1),
                                new BigDecimal("31058.71"),
                                new BigDecimal("51.25"),
                                new BigDecimal("5125.2"),
                                new BigDecimal("0")
                        )),
                true,
                true,
                CreditStatus.CALCULATED);

        final Credit savedCredit = creditRepository.save(credit);
        final Credit findedCredit = creditRepository.findById(savedCredit.getId()).orElse(null);

        assertNotNull(savedCredit);
        assertNotNull(findedCredit);

        assertThat(savedCredit)
                .usingRecursiveComparison()
                .ignoringFieldsMatchingRegexes(".*id")
                .isEqualTo(credit);


        assertThat(findedCredit)
                .usingRecursiveComparison()
                .ignoringFieldsMatchingRegexes(".*id")
                .isEqualTo(credit);
    }
}