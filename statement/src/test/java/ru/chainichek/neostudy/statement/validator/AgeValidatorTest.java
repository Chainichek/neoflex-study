package ru.chainichek.neostudy.statement.validator;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.chainichek.neostudy.statement.annotation.Age;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class AgeValidatorTest {
    AgeValidator ageValidator = new AgeValidator();

    @Test
    void initialize() {
        final Age age = mock(Age.class);

        when(age.min()).thenReturn(0);
        when(age.max()).thenReturn(20);

        assertDoesNotThrow(() -> ageValidator.initialize(age));
    }

    @ParameterizedTest
    @ArgumentsSource(IsValidArgumentsProvider.class)
    void isValid(int min,  int max, LocalDate birthday, boolean expected) {
        final Age age = mock(Age.class);
        final ConstraintValidatorContext constraintValidatorContext = mock(ConstraintValidatorContext.class);

        when(age.min()).thenReturn(min);
        when(age.max()).thenReturn(max);

        assertDoesNotThrow(() -> ageValidator.initialize(age));

        boolean result = ageValidator.isValid(birthday, constraintValidatorContext);

        assertEquals(expected, result);
    }

    static final class IsValidArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    Arguments.of(10,  20, null, true),
                    Arguments.of(10,  20, LocalDate.now(), false),
                    Arguments.of(10,  20, LocalDate.now().minusYears(11), true),
                    Arguments.of(10,  20, LocalDate.now().minusYears(21), false),
                    Arguments.of(10,  20, LocalDate.now().minusYears(20), true)
            );
        }
    }
}