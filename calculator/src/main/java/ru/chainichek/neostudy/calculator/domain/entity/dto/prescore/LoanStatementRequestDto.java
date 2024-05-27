package ru.chainichek.neostudy.calculator.domain.entity.dto.prescore;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record LoanStatementRequestDto(BigDecimal amount,
                                      Integer term,
                                      String firstName,
                                      String lastName,
                                      String middleName,
                                      String email,
                                      LocalDateTime birthdate,
                                      String passportSeries,
                                      String passportNumber) {
}
