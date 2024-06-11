package ru.chainichek.neostudy.deal.dto.statement;

import jakarta.validation.constraints.NotNull;
import ru.chainichek.neostudy.deal.model.client.EmploymentStatus;
import ru.chainichek.neostudy.deal.model.client.EmploymentPosition;

import java.math.BigDecimal;

public record EmploymentDto(@NotNull
                            EmploymentStatus employmentStatus,
                            @NotNull
                            String employerINN,
                            @NotNull
                            BigDecimal salary,
                            @NotNull
                            EmploymentPosition position,
                            @NotNull
                            Integer workExperienceTotal,
                            @NotNull
                            Integer workExperienceCurrent) {
}
