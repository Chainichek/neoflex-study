package ru.chainichek.neostudy.calculator.domain.entity.dto.score;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import ru.chainichek.neostudy.calculator.domain.entity.EmploymentStatus;
import ru.chainichek.neostudy.calculator.domain.entity.Position;
import ru.chainichek.neostudy.calculator.domain.util.Validation;
import ru.chainichek.neostudy.calculator.domain.util.ValidationMessage;

import java.math.BigDecimal;

public record EmploymentDto(@NotNull
                            EmploymentStatus employmentStatus,
                            @NotBlank @Pattern(regexp = Validation.INN_PATTERN, message = ValidationMessage.INN_MESSAGE)
                            String employerINN,
                            @NotNull @DecimalMin(value = Validation.SALARY_MIN, message = ValidationMessage.SALARY_MESSAGE)
                            BigDecimal salary,
                            @NotNull
                            Position position,
                            @NotNull @Min(value = Validation.WORK_EXPERIENCE_MIN, message = ValidationMessage.WORK_EXPERIENCE_MESSAGE)
                            Integer workExperienceTotal,
                            @NotNull @Min(value = Validation.WORK_EXPERIENCE_MIN, message = ValidationMessage.WORK_EXPERIENCE_MESSAGE)
                            Integer workExperienceCurrent) {
}
