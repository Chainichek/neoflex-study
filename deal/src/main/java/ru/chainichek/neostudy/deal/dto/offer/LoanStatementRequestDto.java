package ru.chainichek.neostudy.deal.dto.offer;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.springframework.format.annotation.DateTimeFormat;
import ru.chainichek.neostudy.deal.util.validation.Validation;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LoanStatementRequestDto(@NotNull
                                      BigDecimal amount,
                                      @NotNull
                                      Integer term,
                                      @NotNull
                                      String firstName,
                                      @NotNull
                                      String lastName,
                                      String middleName,
                                      @NotNull
                                      String email,
                                      @NotNull @DateTimeFormat(pattern = Validation.DATE_FORMAT_PATTERN) @PastOrPresent
                                      LocalDate birthdate,
                                      @NotNull
                                      String passportSeries,
                                      @NotNull
                                      String passportNumber) {
}
