package ru.chainichek.neostudy.calculator.domain.service.validator;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
public class ValidateBirthDateUseCase implements BirthDateValidator {
    @Override
    public boolean execute(final LocalDate birthdate, final int age) {
        return Period.between(birthdate, LocalDate.now()).getYears() >= age;
    }
}
