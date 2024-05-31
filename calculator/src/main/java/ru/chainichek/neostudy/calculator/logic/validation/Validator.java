package ru.chainichek.neostudy.calculator.logic.validation;

import java.time.LocalDate;

public interface Validator {
    boolean validateBirthdate(LocalDate birthDate, int age);
    boolean validateINN(String INN);
}
