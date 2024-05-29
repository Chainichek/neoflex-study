package ru.chainichek.neostudy.calculator.service.validator;

import java.time.LocalDate;

public interface BirthDateValidator {
    boolean execute(LocalDate birthDate, int age);
}
