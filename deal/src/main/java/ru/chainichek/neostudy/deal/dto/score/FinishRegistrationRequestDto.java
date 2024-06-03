package ru.chainichek.neostudy.deal.dto.score;

import ru.chainichek.neostudy.deal.model.Gender;
import ru.chainichek.neostudy.deal.model.MaritalStatus;

import java.time.LocalDate;

public record FinishRegistrationRequestDto(Gender gender,
                                           MaritalStatus maritalStatus,
                                           Integer dependentAmount,
                                           LocalDate passportIssueDate,
                                           String passportIssueBranch,
                                           EmploymentDto employment,
                                           String accountNumber) {
}
