package ru.chainichek.neostudy.deal.dto.prescore;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LoanStatementRequestDto(BigDecimal amount,
                                      Integer term,
                                      String firstName,
                                      String lastName,
                                      String middleName,
                                      String email,
                                      LocalDate birthdate,
                                      String passportSeries,
                                      String passportNumber) {
}
