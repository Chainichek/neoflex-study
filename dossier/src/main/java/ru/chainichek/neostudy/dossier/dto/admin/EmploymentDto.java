package ru.chainichek.neostudy.dossier.dto.admin;

import ru.chainichek.neostudy.dossier.model.client.EmploymentPosition;
import ru.chainichek.neostudy.dossier.model.client.EmploymentStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record EmploymentDto(
        UUID id,
        EmploymentStatus status,
        String employerInn,
        BigDecimal salary,
        EmploymentPosition position,
        Integer workExperienceTotal,
        Integer workExperienceCurrent
) {}