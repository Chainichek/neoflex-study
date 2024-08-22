package ru.chainichek.neostudy.calculator.service;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidationServiceTest {
    ValidationService validationService = new ValidationService();
    @ParameterizedTest
    @ArgumentsSource(ValidBirthdateArgumentsProvider.class)
    void validateBirthdate_whenPeriodBetweenNowAndBirthdateIsMoreOrEqualsThanAge_thenReturnTrue(LocalDate birthdate, int age) {
        assertTrue(validationService.validateBirthdate(birthdate, age));
    }

    static final class ValidBirthdateArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    Arguments.of(LocalDate.now().minusYears(20), 18),
                    Arguments.of(LocalDate.now().minusYears(33), 18),
                    Arguments.of(LocalDate.now().minusYears(20), 20)
            );
        }
    }

    @ParameterizedTest
    @ArgumentsSource(InvalidBirthdateArgumentsProvider.class)
    void validateBirthdate_whenPeriodBetweenNowAndBirthdateIsMoreThanAge_thenReturnFalse(LocalDate birthdate, int age) {
        assertFalse(validationService.validateBirthdate(birthdate, age));
    }

    static final class InvalidBirthdateArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    Arguments.of(LocalDate.now().minusYears(17), 18),
                    Arguments.of(LocalDate.now().minusYears(20), 21)
            );
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"902678028502", "145163703815", "48770686bf60", "304283708011", "320829255739", "a87892702135"})
    void validateINN_whenOneOfINNCheckSumIsNotEqualsToCheckNum_thenReturnFalse(String INN) {
        assertFalse(validationService.validateINN(INN));
    }

    @ParameterizedTest
    @ValueSource(strings = {"123", "1234567890", "12345678901"})
    void validateINN_whenINNLengthIsLessThan12_thenReturnFalse(String INN) {
        assertFalse(validationService.validateINN(INN));
    }

    @ParameterizedTest
    @ValueSource(strings = {"904678028502", "145363703815", "487709867060", "304293708011", "320859255739", "487892772135"})
    void validateINN_whenTwoOfINNCheckSumIsEqualsToCheckNum_thenReturnTrue(String INN) {
        assertTrue(validationService.validateINN(INN));
    }
}