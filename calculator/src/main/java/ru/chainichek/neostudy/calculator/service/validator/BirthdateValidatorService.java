package ru.chainichek.neostudy.calculator.service.validator;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
public class BirthdateValidatorService implements BirthdateValidator {
    @Override
    public boolean validateBirthdate(final LocalDate birthdate, final int age) {
        return Period.between(birthdate, LocalDate.now()).getYears() >= age;
    }
}
