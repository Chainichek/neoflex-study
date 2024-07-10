package ru.chainichek.neostudy.dossier.dto.admin;

import ru.chainichek.neostudy.dossier.model.client.Gender;
import ru.chainichek.neostudy.dossier.model.client.MaritalStatus;

import java.time.LocalDate;
import java.util.UUID;

public record ClientDto(
        UUID id,
        String firstName,
        String lastName,
        String middleName,
        LocalDate birthdate,
        String email,
        Gender gender,
        MaritalStatus maritalStatus,
        Integer dependentAmount,
        PassportDto passport,
        EmploymentDto employment,
        String accountNumber
) {}