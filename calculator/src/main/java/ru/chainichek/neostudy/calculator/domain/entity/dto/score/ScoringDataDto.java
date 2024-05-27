package ru.chainichek.neostudy.calculator.domain.entity.dto.score;

import ru.chainichek.neostudy.calculator.domain.entity.Gender;
import ru.chainichek.neostudy.calculator.domain.entity.MaritalStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ScoringDataDto(BigDecimal amount,
                             Integer term,
                             String firstName,
                             String lastName,
                             String middleName,
                             Gender gender,
                             LocalDate birthDate,
                             String passportSeries,
                             String passportNumber,
                             LocalDate passportIssueDate,
                             String passportIssueBranch,
                             MaritalStatus maritalStatus,
                             Integer dependentAmount,
                             EmploymentDto employment,
                             String accountNumber,
                             Boolean isInsuranceEnabled,
                             Boolean isSalaryClient) {
}
