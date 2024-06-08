package ru.chainichek.neostudy.deal.dto.statement;

import ru.chainichek.neostudy.deal.model.client.Gender;
import ru.chainichek.neostudy.deal.model.client.MaritalStatus;

import java.time.LocalDate;

public record FinishRegistrationRequestDto(Gender gender,
                                           MaritalStatus maritalStatus,
                                           Integer dependentAmount,
                                           LocalDate passportIssueDate,
                                           String passportIssueBranch,
                                           EmploymentDto employment,
                                           String accountNumber) {
}
