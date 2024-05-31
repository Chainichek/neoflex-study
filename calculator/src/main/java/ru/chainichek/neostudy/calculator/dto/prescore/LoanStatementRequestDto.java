package ru.chainichek.neostudy.calculator.dto.prescore;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import org.springframework.format.annotation.DateTimeFormat;
import ru.chainichek.neostudy.calculator.util.Validation;
import ru.chainichek.neostudy.calculator.util.ValidationMessage;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LoanStatementRequestDto(@NotNull @DecimalMin(value = Validation.AMOUNT_MIN, message = ValidationMessage.AMOUNT_MESSAGE)
                                      BigDecimal amount,
                                      @NotNull @Min(value = Validation.TERM_MIN, message = ValidationMessage.TERM_MESSAGE)
                                      Integer term,
                                      @NotBlank @Pattern(regexp = Validation.NAME_PATTERN, message = ValidationMessage.NAME_MESSAGE)
                                      String firstName,
                                      @NotBlank @Pattern(regexp = Validation.NAME_PATTERN, message = ValidationMessage.NAME_MESSAGE)
                                      String lastName,
                                      @Pattern(regexp = Validation.NAME_PATTERN, message = ValidationMessage.NAME_MESSAGE)
                                      String middleName,
                                      @NotBlank @Pattern(regexp = Validation.EMAIL_PATTERN, message = ValidationMessage.EMAIL_MESSAGE)
                                      String email,
                                      @NotNull @DateTimeFormat(pattern = Validation.DATE_FORMAT_PATTERN) @PastOrPresent
                                      LocalDate birthdate,
                                      @NotBlank @Pattern(regexp = Validation.PASSPORT_SERIES_PATTERN, message = ValidationMessage.PASSPORT_SERIES_MESSAGE)
                                      String passportSeries,
                                      @NotBlank @Pattern(regexp = Validation.PASSPORT_NUMBER_PATTERN, message = ValidationMessage.PASSPORT_NUMBER_MESSAGE)
                                      String passportNumber) {
}
