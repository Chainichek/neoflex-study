package ru.chainichek.neostudy.calculator.service;

import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.chainichek.neostudy.calculator.logic.validation.Validator;

import java.time.LocalDate;
import java.time.Period;

@Service
public class ValidationService implements Validator {
    private final static Logger LOG = LoggerFactory.getLogger(ValidationService.class);
    private final int[] innCoefficients1 = {7, 2, 4, 10, 3, 5, 9, 4, 6, 8};
    private final int[] innCoefficients2 = {3, 7, 2, 4, 10, 3, 5, 9, 4, 6, 8};

    @Override
    public boolean validateBirthdate(final @NotNull LocalDate birthdate, final int age) {
        LOG.debug("Starting to validate birthdate: birthdate = %s, age = %d".formatted(birthdate, age));
        final int years = Period.between(birthdate, LocalDate.now()).getYears();
        LOG.debug("Age is %d y.o.".formatted(years));
        return years >= age;
    }

    @Override
    public boolean validateINN(final @NotNull String INN) {
        LOG.debug("Starting to validate INN: INN = %s".formatted(INN));
        if (INN.length() != 12) {
            LOG.debug("INN length is not valid");
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

        LOG.debug("Calculated checkNumber1 = %d when given '%s', checkNumber2 = %d when given '%s'"
                .formatted(checkNumber1, INN.charAt(10), checkNumber2, INN.charAt(11)));

        return ((INN.charAt(10) - '0') == checkNumber1 && (INN.charAt(11) - '0') == checkNumber2);
    }
}
