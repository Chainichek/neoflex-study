package ru.chainichek.neostudy.deal.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.test.util.ReflectionTestUtils;
import ru.chainichek.neostudy.deal.dto.offer.LoanStatementRequestDto;
import ru.chainichek.neostudy.deal.dto.statement.EmploymentDto;
import ru.chainichek.neostudy.deal.dto.statement.FinishRegistrationRequestDto;
import ru.chainichek.neostudy.deal.mapper.ClientMapper;
import ru.chainichek.neostudy.deal.model.client.Client;
import ru.chainichek.neostudy.deal.model.client.EmploymentPosition;
import ru.chainichek.neostudy.deal.model.client.EmploymentStatus;
import ru.chainichek.neostudy.deal.model.client.Gender;
import ru.chainichek.neostudy.deal.model.client.MaritalStatus;
import ru.chainichek.neostudy.deal.model.client.Passport;
import ru.chainichek.neostudy.deal.repo.ClientRepository;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {
    @InjectMocks
    ClientService clientService;
    @Mock
    ClientRepository clientRepository;
    ClientMapper clientMapper = Mappers.getMapper(ClientMapper.class);

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(clientService, "clientMapper", clientMapper);
    }

    @Test
    void createClient() {
        final LoanStatementRequestDto request = new LoanStatementRequestDto(BigDecimal.ZERO,
                0,
                "Ivan",
                "Fedorov",
                null,
                "ivanfedorov@yandex",
                LocalDate.of(2023, 3, 7),
                "6161",
                "345678");

        final Client client = Client.builder()
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
                .build();

        when(clientRepository.save(ArgumentMatchers.any())).thenAnswer((Answer<Client>) invocation -> invocation.getArgument(0));

        final Client createdClient = clientService.createClient(request);

        client.getPassport().setId(createdClient.getPassport().getId());

        assertNotNull(createdClient);

        assertThat(client)
                .usingRecursiveComparison()
                .isEqualTo(createdClient);
    }

    @Test
    void updateClientOnFinishRegistration() {
        final Client oldClient = Client.builder()
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
                .build();
        final FinishRegistrationRequestDto finishRegistrationRequest = new FinishRegistrationRequestDto(Gender.MALE,
                MaritalStatus.SINGLE,
                0,
                LocalDate.now(),
                "Somewhere in Saratov",
                new EmploymentDto(EmploymentStatus.EMPLOYED,
                        "Some 12-digit INN",
                        BigDecimal.ZERO,
                        EmploymentPosition.WORKER,
                        0,
                        0),
                "Some bank account number");

        final Client newClient = clientMapper.updateClient(oldClient, finishRegistrationRequest);

        when(clientRepository.save(ArgumentMatchers.any())).thenAnswer((Answer<Client>) invocation -> invocation.getArgument(0));
        final Client updatedClient = clientService.updateClientOnFinishRegistration(oldClient, finishRegistrationRequest);


        assertNotNull(updatedClient);
        assertThat(newClient)
                .usingRecursiveComparison()
                .ignoringFieldsMatchingRegexes(".*id")
                .isEqualTo(updatedClient);
    }
}