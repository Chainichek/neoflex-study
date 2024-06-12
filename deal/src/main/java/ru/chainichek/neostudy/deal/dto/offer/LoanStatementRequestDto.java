package ru.chainichek.neostudy.deal.dto.offer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import ru.chainichek.neostudy.deal.util.validation.Validation;

import java.math.BigDecimal;
import java.time.LocalDate;

import static ru.chainichek.neostudy.deal.util.validation.client.ClientValidation.EMAIL_SIZE_MAX;
import static ru.chainichek.neostudy.deal.util.validation.client.ClientValidation.NAME_SIZE_MAX;
import static ru.chainichek.neostudy.deal.util.validation.client.PassportValidation.PASSPORT_NUMBER_SIZE_MAX;
import static ru.chainichek.neostudy.deal.util.validation.client.PassportValidation.PASSPORT_SERIES_SIZE_MAX;

public record LoanStatementRequestDto(@NotNull
                                      BigDecimal amount,
                                      @NotNull
                                      Integer term,
                                      @NotNull @NotBlank @Size(max = NAME_SIZE_MAX)
                                      String firstName,
                                      @NotNull @NotBlank @Size(max = NAME_SIZE_MAX)
                                      String lastName,
                                      @Size(max = NAME_SIZE_MAX)
                                      String middleName,
                                      @NotNull @NotBlank @Size(max = EMAIL_SIZE_MAX)
                                      String email,
                                      @NotNull @DateTimeFormat(pattern = Validation.DATE_FORMAT_PATTERN) @PastOrPresent
                                      LocalDate birthdate,
                                      @NotNull @NotBlank @Size(max = PASSPORT_SERIES_SIZE_MAX)
                                      String passportSeries,
                                      @NotNull @NotBlank @Size(max = PASSPORT_NUMBER_SIZE_MAX)
                                      String passportNumber) {
}
