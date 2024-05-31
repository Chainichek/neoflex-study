package ru.chainichek.neostudy.calculator.logic.validation;

import java.time.LocalDate;

public interface BirthdateValidator {
    boolean validateBirthdate(LocalDate birthDate, int age);
}
