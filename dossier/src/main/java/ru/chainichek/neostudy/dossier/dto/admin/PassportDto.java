package ru.chainichek.neostudy.dossier.dto.admin;

import java.time.LocalDate;
import java.util.UUID;

public record PassportDto(
        UUID id,
        String series,
        String number,
        String issueBranch,
        LocalDate issueDate
) {}