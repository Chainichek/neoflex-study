package ru.chainichek.neostudy.calculator.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import ru.chainichek.neostudy.calculator.dto.prescore.LoanOfferDto;
import ru.chainichek.neostudy.calculator.dto.prescore.LoanStatementRequestDto;
import ru.chainichek.neostudy.calculator.dto.score.CreditDto;
import ru.chainichek.neostudy.calculator.dto.score.PaymentScheduleElementDto;
import ru.chainichek.neostudy.calculator.dto.score.ScoringDataDto;
import ru.chainichek.neostudy.calculator.service.CalculatorService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CalculatorControllerTest {
    @InjectMocks
    CalculatorController calculatorController;
    @Mock
    CalculatorService calculatorService;
    @Test
    void getOffers() {
        LoanStatementRequestDto loanStatementRequest = mock(LoanStatementRequestDto.class);
        List<LoanOfferDto> loanOffers = List.of(
                new LoanOfferDto(null, BigDecimal.valueOf(30000), BigDecimal.valueOf(300000), 6, BigDecimal.valueOf(5235.91), BigDecimal.valueOf(16), false, false),
                new LoanOfferDto(null, BigDecimal.valueOf(30000), BigDecimal.valueOf(300000), 6, BigDecimal.valueOf(5221.01), BigDecimal.valueOf(15), false,    true),
                new LoanOfferDto(null, BigDecimal.valueOf(30000), BigDecimal.valueOf(31800), 6, BigDecimal.valueOf(5502.76), BigDecimal.valueOf(13), true, false),
                new LoanOfferDto(null, BigDecimal.valueOf(30000), BigDecimal.valueOf(31800), 6, BigDecimal.valueOf(5487.04), BigDecimal.valueOf(12), true, true)
        );

        when(calculatorService.getOffers(any(LoanStatementRequestDto.class))).thenReturn(loanOffers);
        ResponseEntity<?> response = calculatorController.getOffers(loanStatementRequest);

        assertEquals(ResponseEntity.ok(loanOffers), response);
    }

    @Test
    void calculateCredit() {
        ScoringDataDto scoringDataDto = mock(ScoringDataDto.class);
        CreditDto credit = new CreditDto(
                new BigDecimal("30000"),
                6,
                new BigDecimal("5176.45"),
                new BigDecimal("12"),
                new BigDecimal("7.06"),
                true,
                true,
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
                        )
                ));
        when(calculatorService.getCreditInfo(any(ScoringDataDto.class))).thenReturn(credit);

        ResponseEntity<?> response = calculatorController.calculateCredit(scoringDataDto);

        assertEquals(ResponseEntity.ok(credit), response);
    }
}