package ru.chainichek.neostudy.deal.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import ru.chainichek.neostudy.deal.dto.offer.LoanOfferDto;
import ru.chainichek.neostudy.deal.dto.offer.LoanStatementRequestDto;
import ru.chainichek.neostudy.deal.dto.statement.FinishRegistrationRequestDto;
import ru.chainichek.neostudy.deal.service.DealService;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DealControllerTest {

    @InjectMocks
    DealController dealController;

    @Mock
    DealService dealService;

    @Test
    void createStatement() {
        final LoanStatementRequestDto request = mock(LoanStatementRequestDto.class);
        final List<LoanOfferDto> offers = List.of(
                new LoanOfferDto(null, BigDecimal.valueOf(30000), BigDecimal.valueOf(300000), 6, BigDecimal.valueOf(5235.91), BigDecimal.valueOf(16), false, false),
                new LoanOfferDto(null, BigDecimal.valueOf(30000), BigDecimal.valueOf(300000), 6, BigDecimal.valueOf(5221.01), BigDecimal.valueOf(15), false,    true),
                new LoanOfferDto(null, BigDecimal.valueOf(30000), BigDecimal.valueOf(31800), 6, BigDecimal.valueOf(5502.76), BigDecimal.valueOf(13), true, false),
                new LoanOfferDto(null, BigDecimal.valueOf(30000), BigDecimal.valueOf(31800), 6, BigDecimal.valueOf(5487.04), BigDecimal.valueOf(12), true, true)
        );

        when(dealService.createStatement(any(LoanStatementRequestDto.class))).thenReturn(offers);
        final ResponseEntity<?> response = dealController.createStatement(request);

        assertEquals(ResponseEntity.ok(offers), response);
    }

    @Test
    void selectOffer() {
        final LoanOfferDto offer = mock(LoanOfferDto.class);

        final ResponseEntity<?> response = dealController.selectOffer(offer);

        assertEquals(ResponseEntity.ok().build(), response);
    }

    @Test
    void completeStatement() {
        final FinishRegistrationRequestDto finishRegistrationRequest = mock(FinishRegistrationRequestDto.class);
        final UUID statementId = mock(UUID.class);
        final ResponseEntity<?> response = dealController.completeStatement(statementId, finishRegistrationRequest);

        assertEquals(ResponseEntity.ok().build(), response);
    }
}