package ru.chainichek.neostudy.calculator.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.chainichek.neostudy.calculator.dto.prescore.LoanOfferDto;
import ru.chainichek.neostudy.calculator.dto.prescore.LoanStatementRequestDto;
import ru.chainichek.neostudy.calculator.logic.calculation.MonthlyPaymentCalculator;
import ru.chainichek.neostudy.calculator.logic.calculation.PreScoreCalculator;
import ru.chainichek.neostudy.calculator.logic.calculation.ScoreCalculator;
import ru.chainichek.neostudy.calculator.logic.validation.Validator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CalculatorServiceTest {
    @InjectMocks
    CalculatorService calculatorService;

    @Mock
    MathContext resultMathContext;
    @Mock
    PreScoreCalculator preScoreCalculator;
    @Mock
    ScoreCalculator scoreCalculator;
    @Mock
    MonthlyPaymentCalculator monthlyPaymentCalculator;
    @Mock
    Validator validator;

    @BeforeEach
    void setUp() {
        when(resultMathContext.getPrecision()).thenReturn(2);
        when(resultMathContext.getRoundingMode()).thenReturn(RoundingMode.HALF_UP);
    }

    @Test
    void getOffers() {
        LoanStatementRequestDto request = mock(LoanStatementRequestDto.class);

        when(request.amount()).thenReturn(BigDecimal.valueOf(30000));
        when(request.term()).thenReturn(6);

        boolean[] booleans = {false, true};
        BigDecimal[] rateValues = {BigDecimal.valueOf(16), BigDecimal.valueOf(15), BigDecimal.valueOf(13), BigDecimal.valueOf(12)};
        BigDecimal[] amountValues = {BigDecimal.valueOf(30000), BigDecimal.valueOf(30000), BigDecimal.valueOf(31800), BigDecimal.valueOf(31800)};
        BigDecimal[] monthlyPayment = {BigDecimal.valueOf(5235.91), BigDecimal.valueOf(5221.01), BigDecimal.valueOf(5502.76), BigDecimal.valueOf(5487.04)};

        int i = 0;
        for (boolean isInsuranceEnabled : booleans) {
            for (boolean isSalaryClient : booleans) {
                when(preScoreCalculator.calculatePreScoreRate(isSalaryClient, isInsuranceEnabled)).thenReturn(rateValues[i]);
                when(preScoreCalculator.calculateAmount(request.amount(), isSalaryClient, isInsuranceEnabled)).thenReturn(amountValues[i]);
                when(monthlyPaymentCalculator.calculateMonthlyPayment(amountValues[i], rateValues[i], request.term())).thenReturn(monthlyPayment[i]);
                i++;
            }
        }

        List<LoanOfferDto> result = calculatorService.getOffers(request);
        assertEquals(result.size(), 4);

        assertArrayEquals(new Boolean[]{false, false, true, true}, result.stream()
                .map(LoanOfferDto::isInsuranceEnabled)
                .toArray(Boolean[]::new));
        assertArrayEquals(new Boolean[]{false, true, false, true}, result.stream()
                .map(LoanOfferDto::isSalaryClient)
                .toArray(Boolean[]::new));
        assertArrayEquals(result.stream()
                        .sorted(Comparator.comparing(LoanOfferDto::rate).reversed())
                        .map(LoanOfferDto::rate)
                        .toArray(BigDecimal[]::new),
                Arrays.stream(rateValues)
                        .map(x -> x.setScale(resultMathContext.getPrecision(), resultMathContext.getRoundingMode()))
                        .toArray(BigDecimal[]::new));
    }

//    @Test
//    void getCreditInfo() {
//    }
}