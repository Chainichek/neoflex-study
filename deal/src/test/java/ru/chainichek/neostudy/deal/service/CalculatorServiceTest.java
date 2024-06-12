package ru.chainichek.neostudy.deal.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.chainichek.neostudy.deal.client.CalculatorClient;
import ru.chainichek.neostudy.deal.dto.calculation.CreditDto;
import ru.chainichek.neostudy.deal.dto.calculation.ScoringDataDto;
import ru.chainichek.neostudy.deal.dto.offer.LoanOfferDto;
import ru.chainichek.neostudy.deal.dto.offer.LoanStatementRequestDto;
import ru.chainichek.neostudy.deal.dto.statement.EmploymentDto;
import ru.chainichek.neostudy.deal.model.client.Client;
import ru.chainichek.neostudy.deal.model.client.Passport;
import ru.chainichek.neostudy.deal.model.statement.Statement;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CalculatorServiceTest {
    @InjectMocks
    CalculatorService calculatorService;
    @Mock
    CalculatorClient calculatorClient;

    @Test
    void getOffers() {
        final LoanStatementRequestDto request = mock(LoanStatementRequestDto.class);
        final UUID uuid = mock(UUID.class);

        final List<LoanOfferDto> offers = List.of(
                new LoanOfferDto(null, BigDecimal.valueOf(30000), BigDecimal.valueOf(300000), 6, BigDecimal.valueOf(5235.91), BigDecimal.valueOf(16), false, false),
                new LoanOfferDto(null, BigDecimal.valueOf(30000), BigDecimal.valueOf(300000), 6, BigDecimal.valueOf(5221.01), BigDecimal.valueOf(15), false, true)
        );

        when(calculatorClient.getOffers(request)).thenReturn(offers);

        final List<LoanOfferDto> receivedOffers = calculatorService.getOffers(request, uuid);

        assertNotEquals(0, receivedOffers.size());
        assertThat(receivedOffers)
                .extracting(LoanOfferDto::statementId)
                .containsOnly(uuid);

        assertThat(offers)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("statementId")
                .containsExactlyInAnyOrderElementsOf(offers);
    }

    @Test
    void calculateCredit() {
        final Statement statement = mock(Statement.class);
        final EmploymentDto employment = mock(EmploymentDto.class);
        final Client client = mock(Client.class);
        final Passport passport = mock(Passport.class);
        final LoanOfferDto appliedOffer = mock(LoanOfferDto.class);

        when(statement.getAppliedOffer()).thenReturn(appliedOffer);
        when(statement.getClient()).thenReturn(client);
        when(client.getPassport()).thenReturn(passport);

        final CreditDto credit = mock(CreditDto.class);

        when(calculatorClient.calculateCredit(ArgumentMatchers.any(ScoringDataDto.class))).thenReturn(credit);

        final CreditDto receivedCredit = calculatorService.calculateCredit(statement, employment);

        assertNotNull(receivedCredit);
        assertEquals(credit, receivedCredit);
    }
}