package ru.chainichek.neostudy.deal.dto.statement;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import ru.chainichek.neostudy.deal.model.client.Gender;
import ru.chainichek.neostudy.deal.model.client.MaritalStatus;
import ru.chainichek.neostudy.deal.util.validation.Validation;

import java.time.LocalDate;

import static ru.chainichek.neostudy.deal.util.validation.client.ClientValidation.ACCOUNT_NUMBER_SIZE_MAX;

public record FinishRegistrationRequestDto(@NotNull
                                           Gender gender,
                                           @NotNull
                                           MaritalStatus maritalStatus,
                                           @NotNull
                                           Integer dependentAmount,
                                           @NotNull @DateTimeFormat(pattern = Validation.DATE_FORMAT_PATTERN) @PastOrPresent
                                           LocalDate passportIssueDate,
                                           @NotNull
                                           String passportIssueBranch,
                                           @NotNull
                                           EmploymentDto employment,
                                           @NotBlank @Size(max = ACCOUNT_NUMBER_SIZE_MAX)
                                           String accountNumber) {
}
