package ru.chainichek.neostudy.calculator.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.chainichek.neostudy.calculator.dto.score.ScoringDataDto;
import ru.chainichek.neostudy.calculator.exception.ValidationException;
import ru.chainichek.neostudy.calculator.service.validator.BirthDateValidator;
import ru.chainichek.neostudy.calculator.service.validator.INNValidator;
import ru.chainichek.neostudy.calculator.util.Validation;
import ru.chainichek.neostudy.calculator.util.ValidationMessage;

@Service
@AllArgsConstructor
public class ValidateScoringDataUseCase {
    private final BirthDateValidator birthDateValidator;
    private final INNValidator innValidator;

    public void execute(final ScoringDataDto scoringData) {
        if (!birthDateValidator.execute(scoringData.birthdate(), Validation.AGE_MIN)) {
            throw new ValidationException(ValidationMessage.AGE_MESSAGE);
        }
        if (!innValidator.execute(scoringData.employment().employerINN())) {
            throw new ValidationException(ValidationMessage.INN_CHECK_NUMBER_MESSAGE);
        }
    }
}
