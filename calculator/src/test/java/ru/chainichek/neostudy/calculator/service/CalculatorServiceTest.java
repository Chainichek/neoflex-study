package ru.chainichek.neostudy.calculator.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.chainichek.neostudy.calculator.dto.prescore.LoanOfferDto;
import ru.chainichek.neostudy.calculator.dto.prescore.LoanStatementRequestDto;
import ru.chainichek.neostudy.calculator.dto.score.CreditDto;
import ru.chainichek.neostudy.calculator.dto.score.EmploymentDto;
import ru.chainichek.neostudy.calculator.dto.score.PaymentScheduleElementDto;
import ru.chainichek.neostudy.calculator.dto.score.ScoringDataDto;
import ru.chainichek.neostudy.calculator.exception.ValidationException;
import ru.chainichek.neostudy.calculator.logic.calculation.MonthlyPaymentCalculator;
import ru.chainichek.neostudy.calculator.logic.calculation.PreScoreCalculator;
import ru.chainichek.neostudy.calculator.logic.calculation.ScoreCalculator;
import ru.chainichek.neostudy.calculator.logic.validation.Validator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CalculatorServiceTest {
    final static int PRECISION = 2;
    final static RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
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

    @Test
    void getOffers() {
        when(resultMathContext.getPrecision()).thenReturn(PRECISION);
        when(resultMathContext.getRoundingMode()).thenReturn(ROUNDING_MODE);

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

    @Test
    void getCreditInfo_whenScoringDataBirthdateIsInvalid_thenThrowValidationException() {
        ScoringDataDto scoringData = mock(ScoringDataDto.class);

        when(validator.validateBirthdate(eq(scoringData.birthdate()), any(Integer.class))).thenReturn(false);

        assertThrows(ValidationException.class, () -> calculatorService.getCreditInfo(scoringData));
    }

    @Test
    void getCreditInfo_whenScoringDataINNIsInvalid_thenThrowValidationException() {
        EmploymentDto employment = mock(EmploymentDto.class);
        ScoringDataDto scoringData = mock(ScoringDataDto.class);

        when(scoringData.employment()).thenReturn(employment);

        when(validator.validateBirthdate(eq(scoringData.birthdate()), any(Integer.class))).thenReturn(true);
        when(validator.validateINN(scoringData.employment().employerINN())).thenReturn(false);

        assertThrows(ValidationException.class, () -> calculatorService.getCreditInfo(scoringData));
    }

    @Test
    void getCreditInfo_whenScoringDataIsValid_thenReturnCreditDto() {
        when(resultMathContext.getPrecision()).thenReturn(PRECISION);
        when(resultMathContext.getRoundingMode()).thenReturn(ROUNDING_MODE);

        EmploymentDto employment = mock(EmploymentDto.class);
        ScoringDataDto scoringData = mock(ScoringDataDto.class);

        BigDecimal amount = BigDecimal.valueOf(30000);
        BigDecimal rate = BigDecimal.valueOf(12);
        BigDecimal monthlyPayment = BigDecimal.valueOf(5176.45);
        BigDecimal psk = BigDecimal.valueOf(7.06);
        List<PaymentScheduleElementDto> paymentSchedule = List.of(
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
        );

        when(scoringData.employment()).thenReturn(employment);
        when(scoringData.amount()).thenReturn(amount);

        when(validator.validateBirthdate(eq(scoringData.birthdate()), any(Integer.class))).thenReturn(true);
        when(validator.validateINN(scoringData.employment().employerINN())).thenReturn(true);

        doNothing().when(scoreCalculator).checkScoringData(scoringData);
        when(scoreCalculator.calculateScoreRate(scoringData)).thenReturn(rate);
        when(monthlyPaymentCalculator.calculateMonthlyPayment(scoringData.amount(), rate, scoringData.term())).thenReturn(monthlyPayment);
        when(scoreCalculator.calculatePsk(scoringData.amount(), monthlyPayment, scoringData.term())).thenReturn(psk);
        when(scoreCalculator.calculatePaymentSchedule(scoringData.amount(), rate, monthlyPayment, scoringData.term(), LocalDate.now())).thenReturn(paymentSchedule);


        CreditDto credit = calculatorService.getCreditInfo(scoringData);
        assertNotNull(credit);
    }
}