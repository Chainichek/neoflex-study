package ru.chainichek.neostudy.deal.dto.calculation;

import ru.chainichek.neostudy.deal.dto.statement.EmploymentDto;
import ru.chainichek.neostudy.deal.model.client.Gender;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ScoringDataDto(BigDecimal amount,
                             Integer term,
                             String firstName,
                             String lastName,
                             String middleName,
                             Gender gender,
                             LocalDate birthdate,
                             String passportSeries,
                             String passportNumber,
                             LocalDate passportIssueDate,
                             String passportIssueBranch,
                             Integer dependentAmount,
                             EmploymentDto employment,
                             String accountNumber,
                             Boolean isInsuranceEnabled,
                             Boolean isSalaryClient) {
}
