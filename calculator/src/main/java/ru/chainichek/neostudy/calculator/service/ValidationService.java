package ru.chainichek.neostudy.calculator.service;

import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import ru.chainichek.neostudy.calculator.dto.score.ScoringDataDto;
import ru.chainichek.neostudy.calculator.exception.ValidationException;
import ru.chainichek.neostudy.calculator.logic.validation.BirthdateValidator;
import ru.chainichek.neostudy.calculator.logic.validation.INNValidator;
import ru.chainichek.neostudy.calculator.util.Validation;
import ru.chainichek.neostudy.calculator.util.ValidationMessage;

import java.time.LocalDate;
import java.time.Period;

@Service
public class ValidationService implements BirthdateValidator, INNValidator {
    private final int[] innCoefficients1 = {7, 2, 4, 10, 3, 5, 9, 4, 6, 8};
    private final int[] innCoefficients2 = {3, 7, 2, 4, 10, 3, 5, 9, 4, 6, 8};

    @Override
    public boolean validateBirthdate(final @NotNull LocalDate birthdate, final int age) {
        return Period.between(birthdate, LocalDate.now()).getYears() >= age;
    }

    @Override
    public boolean validateINN(final @NotNull String INN) {
        if (INN.length() != 12) {
            return false;
        }

        int checkNumber1 = 0, checkNumber2 = 0;
        for (int i = 0; i < innCoefficients2.length; i++) {
            final int symbol = (INN.charAt(i) - '0');
            if (i != innCoefficients1.length) {
                checkNumber1 += symbol * innCoefficients1[i];
            }
            checkNumber2 += symbol * innCoefficients2[i];
        }
        checkNumber1 = checkNumber1 % 11 % 10;
        checkNumber2 = checkNumber2 % 11 % 10;
        return ((INN.charAt(10) - '0') == checkNumber1 && (INN.charAt(11) - '0') == checkNumber2);
    }

    public void validateScoringData(final ScoringDataDto scoringData) {
        if (!validateBirthdate(scoringData.birthdate(), Validation.AGE_MIN)) {
            throw new ValidationException(ValidationMessage.AGE_MESSAGE);
        }
        if (!validateINN(scoringData.employment().employerINN())) {
            throw new ValidationException(ValidationMessage.INN_CHECK_NUMBER_MESSAGE);
        }
    }
}
