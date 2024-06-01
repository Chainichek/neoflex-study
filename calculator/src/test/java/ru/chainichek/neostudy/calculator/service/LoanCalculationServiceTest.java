package ru.chainichek.neostudy.calculator.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.chainichek.neostudy.calculator.dto.score.EmploymentDto;
import ru.chainichek.neostudy.calculator.dto.score.ScoringDataDto;
import ru.chainichek.neostudy.calculator.model.EmploymentStatus;
import ru.chainichek.neostudy.calculator.model.Gender;
import ru.chainichek.neostudy.calculator.model.MaritalStatus;
import ru.chainichek.neostudy.calculator.model.Position;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanCalculationServiceTest {
    final static int PRECISION = MathContext.DECIMAL64.getPrecision();
    final static RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
    final static BigDecimal DELTA = BigDecimal.valueOf(0.0000000001);
    final static BigDecimal BASE_RATE = BigDecimal.valueOf(16);
    final static MathContext CALCULATION_MATH_CONTEXT = new MathContext(PRECISION, ROUNDING_MODE);
    LoanCalculationService loanCalculationService = new LoanCalculationService(CALCULATION_MATH_CONTEXT, BASE_RATE);

    static boolean compareBigDecimals(BigDecimal expected, BigDecimal actual) {
        return expected.subtract(actual, CALCULATION_MATH_CONTEXT).abs().compareTo(DELTA) <= 0;
    }

    @Test
    void checkScoringData() {
    }

    @ParameterizedTest
    @ArgumentsSource(AmountArgumentsProvider.class)
    void calculateAmount(BigDecimal amount, boolean isInsuranceEnabled, boolean isSalaryClient, BigDecimal expected) {
        BigDecimal totalAmount = loanCalculationService.calculateAmount(amount, isInsuranceEnabled, isSalaryClient);
        assertNotNull(totalAmount);
        assertTrue(compareBigDecimals(expected, totalAmount));
    }

    static class AmountArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    Arguments.of(BigDecimal.valueOf(30000), false, false, BigDecimal.valueOf(30000)),
                    Arguments.of(BigDecimal.valueOf(30000), false, true, BigDecimal.valueOf(30000)),
                    Arguments.of(BigDecimal.valueOf(30000), true, false, BigDecimal.valueOf(31800)),
                    Arguments.of(BigDecimal.valueOf(30000), true, true, BigDecimal.valueOf(31800)),
                    Arguments.of(BigDecimal.valueOf(100000), false, false, BigDecimal.valueOf(100000)),
                    Arguments.of(BigDecimal.valueOf(100000), false, true, BigDecimal.valueOf(100000)),
                    Arguments.of(BigDecimal.valueOf(100000), true, false, BigDecimal.valueOf(106000)),
                    Arguments.of(BigDecimal.valueOf(100000), true, true, BigDecimal.valueOf(106000))
            );
        }
    }

    @ParameterizedTest
    @ArgumentsSource(MonthlyPaymentArgumentsProvider.class)
    void calculateMonthlyPayment(BigDecimal amount, BigDecimal rate, int term, BigDecimal expected) {
        BigDecimal monthlyPayment = loanCalculationService.calculateMonthlyPayment(amount, rate, term);
        System.out.println(monthlyPayment);
        assertNotNull(monthlyPayment);
        assertTrue(compareBigDecimals(expected, monthlyPayment));
    }

    static final class MonthlyPaymentArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    Arguments.of(BigDecimal.valueOf(30000), BigDecimal.valueOf(16), 6, BigDecimal.valueOf(5235.908515504683)),
                    Arguments.of(BigDecimal.valueOf(50000), BigDecimal.valueOf(15), 6, BigDecimal.valueOf(8701.690510672815)),
                    Arguments.of(BigDecimal.valueOf(100000), BigDecimal.valueOf(16), 12, BigDecimal.valueOf(9073.085785921004)),
                    Arguments.of(BigDecimal.valueOf(500000), BigDecimal.valueOf(12), 12, BigDecimal.valueOf(44424.39433917086)),
                    Arguments.of(BigDecimal.valueOf(2000000), BigDecimal.valueOf(9), 24, BigDecimal.valueOf(91369.48455834618))
            );
        }
    }

    @Test
    void calculatePaymentSchedule() {
    }

    @ParameterizedTest
    @ArgumentsSource(PreScoreRateArgumentsProvider.class)
    void calculatePreScoreRate(boolean isInsuranceEnabled, boolean isSalaryClient, BigDecimal expected) {
        BigDecimal preScoreRate = loanCalculationService.calculatePreScoreRate(isInsuranceEnabled, isSalaryClient);
        assertNotNull(preScoreRate);
        assertTrue(compareBigDecimals(expected, preScoreRate));
    }

    static class PreScoreRateArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    Arguments.of(false, false, BigDecimal.valueOf(16)),
                    Arguments.of(false, true, BigDecimal.valueOf(15)),
                    Arguments.of(true, false, BigDecimal.valueOf(13)),
                    Arguments.of(true, true, BigDecimal.valueOf(12))
            );
        }
    }

    @ParameterizedTest
    @ArgumentsSource(PskArgumentsProvider.class)
    void calculatePsk(BigDecimal amount, BigDecimal monthlyPayment, int term, BigDecimal expected) {
        BigDecimal psk = loanCalculationService.calculatePsk(amount, monthlyPayment, term);
        assertNotNull(psk);
        assertTrue(compareBigDecimals(expected, psk));
    }

    static class PskArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    Arguments.of(BigDecimal.valueOf(30000), BigDecimal.valueOf(5176.45), 6, BigDecimal.valueOf(7.058)),
                    Arguments.of(BigDecimal.valueOf(30000), BigDecimal.valueOf(5235.91), 6, BigDecimal.valueOf(9.436400)),
                    Arguments.of(BigDecimal.valueOf(32000), BigDecimal.valueOf(5584.97), 6, BigDecimal.valueOf(9.43637500)),
                    Arguments.of(BigDecimal.valueOf(500000), BigDecimal.valueOf(45365.43), 12, BigDecimal.valueOf(8.87703200)),
                    Arguments.of(BigDecimal.valueOf(601000), BigDecimal.valueOf(29426.83), 24, BigDecimal.valueOf(8.755733777038250))
            );
        }
    }

    @ParameterizedTest
    @ArgumentsSource(ScoreRateArgumentsProvider.class)
    void calculateScoreRate(EmploymentStatus employmentStatus,
                            Position position,
                            MaritalStatus maritalStatus,
                            Gender gender,
                            LocalDate birthdate,
                            boolean isInsuranceEnabled,
                            boolean isSalaryClient,
                            BigDecimal expected) {
        EmploymentDto employmentDto = mock(EmploymentDto.class);
        ScoringDataDto scoringData = mock(ScoringDataDto.class);

        when(scoringData.employment()).thenReturn(employmentDto);
        when(scoringData.gender()).thenReturn(gender);
        when(scoringData.birthdate()).thenReturn(birthdate);
        when(scoringData.maritalStatus()).thenReturn(maritalStatus);
        when(scoringData.isInsuranceEnabled()).thenReturn(isInsuranceEnabled);
        when(scoringData.isSalaryClient()).thenReturn(isSalaryClient);

        when(employmentDto.employmentStatus()).thenReturn(employmentStatus);
        when(employmentDto.position()).thenReturn(position);

        BigDecimal scoreRate = loanCalculationService.calculateScoreRate(scoringData);

        assertNotNull(scoreRate);
        assertTrue(compareBigDecimals(expected, scoreRate));
    }

    static final class ScoreRateArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
            return Stream.of(
                    Arguments.of(EmploymentStatus.EMPLOYED, Position.OTHER, MaritalStatus.SINGLE, Gender.MALE, LocalDate.now().minusYears(20), false, false, BigDecimal.valueOf(16)),
                    Arguments.of(EmploymentStatus.SELF_EMPLOYED, Position.OTHER, MaritalStatus.SINGLE, Gender.MALE, LocalDate.now().minusYears(20), false, false, BigDecimal.valueOf(18)),
                    Arguments.of(EmploymentStatus.BUSINESS_OWNER, Position.OTHER, MaritalStatus.SINGLE, Gender.MALE, LocalDate.now().minusYears(20), false, false, BigDecimal.valueOf(19)),
                    Arguments.of(EmploymentStatus.EMPLOYED, Position.MIDDLE_MANAGER, MaritalStatus.SINGLE, Gender.MALE, LocalDate.now().minusYears(20), false, false, BigDecimal.valueOf(14)),
                    Arguments.of(EmploymentStatus.EMPLOYED, Position.TOP_MANAGER, MaritalStatus.SINGLE, Gender.MALE, LocalDate.now().minusYears(20), false, false, BigDecimal.valueOf(13)),
                    Arguments.of(EmploymentStatus.EMPLOYED, Position.OTHER, MaritalStatus.SINGLE, Gender.FEMALE, LocalDate.now().minusYears(20), false, false, BigDecimal.valueOf(16)),
                    Arguments.of(EmploymentStatus.EMPLOYED, Position.OTHER, MaritalStatus.SINGLE, Gender.MALE, LocalDate.now().minusYears(40), false, false, BigDecimal.valueOf(13)),
                    Arguments.of(EmploymentStatus.EMPLOYED, Position.OTHER, MaritalStatus.SINGLE, Gender.FEMALE, LocalDate.now().minusYears(40), false, false, BigDecimal.valueOf(13)),
                    Arguments.of(EmploymentStatus.EMPLOYED, Position.OTHER, MaritalStatus.MARRIED, Gender.MALE, LocalDate.now().minusYears(20), false, false, BigDecimal.valueOf(13)),
                    Arguments.of(EmploymentStatus.EMPLOYED, Position.OTHER, MaritalStatus.DIVORCED, Gender.MALE, LocalDate.now().minusYears(20), false, false, BigDecimal.valueOf(17)),
                    Arguments.of(EmploymentStatus.EMPLOYED, Position.OTHER, MaritalStatus.SINGLE, Gender.MALE, LocalDate.now().minusYears(20), true, false, BigDecimal.valueOf(13)),
                    Arguments.of(EmploymentStatus.EMPLOYED, Position.OTHER, MaritalStatus.SINGLE, Gender.MALE, LocalDate.now().minusYears(20), false, true, BigDecimal.valueOf(15))
            );
        }
    }
}