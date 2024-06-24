package ru.chainichek.neostudy.statement.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.chainichek.neostudy.statement.annotation.Age;

import java.time.LocalDate;
import java.time.Period;

public class AgeValidator implements ConstraintValidator<Age, LocalDate> {
    private static final Logger LOG = LoggerFactory.getLogger(AgeValidator.class);

    private int min;
    private int max;

    @Override
    public void initialize(Age constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(LocalDate birthdate, ConstraintValidatorContext constraintValidatorContext) {
        if (birthdate == null) {
            return false;
        }

        final int years = Period.between(birthdate, LocalDate.now()).getYears();

        LOG.debug("Validating age: birthdate = %s, age = %d".formatted(birthdate, years));

        return years >= min && years <= max;
    }
}
