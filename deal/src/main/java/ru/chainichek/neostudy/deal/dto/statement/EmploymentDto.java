package ru.chainichek.neostudy.deal.dto.statement;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import ru.chainichek.neostudy.deal.model.client.EmploymentStatus;
import ru.chainichek.neostudy.deal.model.client.EmploymentPosition;

import java.math.BigDecimal;

public record EmploymentDto(@NotNull
                            EmploymentStatus employmentStatus,
                            @NotNull
                            @Schema(example = "251112724508")
                            String employerINN,
                            @NotNull
                            @Schema(example = "300000")
                            BigDecimal salary,
                            @NotNull
                            EmploymentPosition position,
                            @NotNull
                            @Schema(example = "18")
                            Integer workExperienceTotal,
                            @NotNull
                            @Schema(example = "3")
                            Integer workExperienceCurrent) {
}
