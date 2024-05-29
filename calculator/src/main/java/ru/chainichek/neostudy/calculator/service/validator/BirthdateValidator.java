package ru.chainichek.neostudy.calculator.service.validator;

import java.time.LocalDate;

public interface BirthdateValidator {
    boolean validateBirthdate(LocalDate birthDate, int age);
}
