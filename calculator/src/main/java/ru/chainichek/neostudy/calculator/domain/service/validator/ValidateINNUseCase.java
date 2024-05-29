package ru.chainichek.neostudy.calculator.domain.service.validator;

import org.springframework.stereotype.Service;

@Service
public class ValidateINNUseCase implements INNValidator {
    private final int[] coefficients1 = {7, 2, 4, 10, 3, 5, 9, 4, 6, 8};
    private final int[] coefficients2 = {3, 7, 2, 4, 10, 3, 5, 9, 4, 6, 8};
    @Override
    public boolean execute(final String INN) {
        int checkNumber1 = 0, checkNumber2 = 0;
        for (int i = 0; i < coefficients2.length; i++) {
            final int symbol = (INN.charAt(i) - '0');
            if (i != coefficients1.length) {
                checkNumber1 += symbol * coefficients1[i];
            }
            checkNumber2 += symbol * coefficients2[i];
        }
        checkNumber1 = checkNumber1 % 11 % 10;
        checkNumber2 = checkNumber2 % 11 % 10;
        return ((INN.charAt(10) - '0') == checkNumber1 && (INN.charAt(11) - '0') == checkNumber2);
    }
}
