package ru.chainichek.neostudy.statement.dto.prescore;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;
import ru.chainichek.neostudy.statement.annotation.Age;
import ru.chainichek.neostudy.statement.annotation.Amount;
import ru.chainichek.neostudy.statement.annotation.Email;
import ru.chainichek.neostudy.statement.annotation.Name;
import ru.chainichek.neostudy.statement.annotation.NotBlankName;
import ru.chainichek.neostudy.statement.annotation.PassportNumber;
import ru.chainichek.neostudy.statement.annotation.PassportSeries;
import ru.chainichek.neostudy.statement.annotation.Term;
import ru.chainichek.neostudy.statement.util.Validation;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LoanStatementRequestDto(@Amount
                                      BigDecimal amount,
                                      @Term
                                      Integer term,
                                      @NotBlankName
                                      @Schema(example = "Ivan")
                                      String firstName,
                                      @NotBlankName
                                      @Schema(example = "Ivanov")
                                      String lastName,
                                      @Name
                                      @Schema(example = "Ivanovich")
                                      String middleName,
                                      @Email
                                      @Schema(example = "test@test.test")
                                      String email,
                                      @Age
                                      @DateTimeFormat(pattern = Validation.DATE_FORMAT_PATTERN)
                                      LocalDate birthdate,
                                      @PassportSeries
                                      String passportSeries,
                                      @PassportNumber
                                      String passportNumber) {
}
