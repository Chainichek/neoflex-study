package ru.chainichek.neostudy.statement.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.chainichek.neostudy.statement.client.DealClient;
import ru.chainichek.neostudy.statement.dto.prescore.LoanOfferDto;
import ru.chainichek.neostudy.statement.dto.prescore.LoanStatementRequestDto;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatementServiceTest {
    @InjectMocks
    StatementService statementService;
    @Mock
    DealClient dealClient;

    @Test
    void getOffers() {
        final LoanStatementRequestDto request = mock(LoanStatementRequestDto.class);
        final List<LoanOfferDto> offers = List.of(mock(LoanOfferDto.class), mock(LoanOfferDto.class), mock(LoanOfferDto.class), mock(LoanOfferDto.class));
        when(dealClient.createStatement(request)).thenReturn(offers);

        final List<LoanOfferDto> result = statementService.getOffers(request);
        assertNotNull(result);
        assertNotEquals(0, result.size());
        assertEquals(4, result.size());
        assertThat(result).allMatch(Objects::nonNull);
    }

    @Test
    void selectOffer() {
        final LoanOfferDto offer = mock(LoanOfferDto.class);

        assertDoesNotThrow(() -> statementService.selectOffer(offer));
    }
}