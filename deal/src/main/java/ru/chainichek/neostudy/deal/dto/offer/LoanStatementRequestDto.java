package ru.chainichek.neostudy.deal.dto.offer;

import io.swagger.v3.oas.annotations.media.Schema;
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
                                      @Schema(example = "30000")
                                      BigDecimal amount,
                                      @NotNull
                                      @Schema(example = "6")
                                      Integer term,
                                      @NotBlank @Size(max = NAME_SIZE_MAX)
                                      @Schema(example = "Ivan")
                                      String firstName,
                                      @NotBlank @Size(max = NAME_SIZE_MAX)
                                      @Schema(example = "Ivanov")
                                      String lastName,
                                      @Size(max = NAME_SIZE_MAX)
                                      @Schema(example = "Ivanovich")
                                      String middleName,
                                      @NotBlank @Size(max = EMAIL_SIZE_MAX)
                                      @Schema(example = "test@test.test")
                                      String email,
                                      @NotNull @DateTimeFormat(pattern = Validation.DATE_FORMAT_PATTERN) @PastOrPresent
                                      LocalDate birthdate,
                                      @NotBlank @Size(max = PASSPORT_SERIES_SIZE_MAX)
                                      @Schema(example = "1234")
                                      String passportSeries,
                                      @NotBlank @Size(max = PASSPORT_NUMBER_SIZE_MAX)
                                      @Schema(example = "123456")
                                      String passportNumber) {
}
