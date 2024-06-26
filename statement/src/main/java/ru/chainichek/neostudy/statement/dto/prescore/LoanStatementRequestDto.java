package ru.chainichek.neostudy.statement.dto.prescore;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;
import ru.chainichek.neostudy.statement.annotation.IsAgeValid;
import ru.chainichek.neostudy.statement.annotation.IsAmountValid;
import ru.chainichek.neostudy.statement.annotation.IsEmailValid;
import ru.chainichek.neostudy.statement.annotation.IsNameValid;
import ru.chainichek.neostudy.statement.annotation.IsNameNotBlankAndValid;
import ru.chainichek.neostudy.statement.annotation.IsPassportNumberValid;
import ru.chainichek.neostudy.statement.annotation.IsPassportSeriesValid;
import ru.chainichek.neostudy.statement.annotation.IsTermValid;
import ru.chainichek.neostudy.statement.util.Validation;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LoanStatementRequestDto(@IsAmountValid
                                      @Schema(example = "30000")
                                      BigDecimal amount,
                                      @IsTermValid
                                      @Schema(example = "6")
                                      Integer term,
                                      @IsNameNotBlankAndValid
                                      @Schema(example = "Ivan")
                                      String firstName,
                                      @IsNameNotBlankAndValid
                                      @Schema(example = "Ivanov")
                                      String lastName,
                                      @IsNameValid
                                      @Schema(example = "Ivanovich")
                                      String middleName,
                                      @IsEmailValid
                                      @Schema(example = "test@test.test")
                                      String email,
                                      @IsAgeValid
                                      @DateTimeFormat(pattern = Validation.DATE_FORMAT_PATTERN)
                                      LocalDate birthdate,
                                      @IsPassportSeriesValid
                                      @Schema(example = "1234")
                                      String passportSeries,
                                      @IsPassportNumberValid
                                      @Schema(example = "123456")
                                      String passportNumber) {
}
