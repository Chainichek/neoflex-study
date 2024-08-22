package ru.chainichek.neostudy.calculator.dto.score;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import ru.chainichek.neostudy.calculator.model.EmploymentStatus;
import ru.chainichek.neostudy.calculator.model.EmploymentPosition;
import ru.chainichek.neostudy.calculator.util.Validation;
import ru.chainichek.neostudy.calculator.util.ValidationMessage;

import java.math.BigDecimal;

public record EmploymentDto(@NotNull
                            @Schema(example = "SELF_EMPLOYED")
                            EmploymentStatus employmentStatus,
                            @NotBlank @Pattern(regexp = Validation.INN_PATTERN, message = ValidationMessage.INN_MESSAGE)
                            @Schema(description = """
                                    the least significant digit of the sum from 1 to 10 digits must be equal to the 11th digit,
                                    the least significant digit of the sum from 1 to 11 digits must be equal to the 12th digit
                                    """,
                                    example = "251112724508")
                            String employerINN,
                            @NotNull @DecimalMin(value = Validation.SALARY_MIN, message = ValidationMessage.SALARY_MESSAGE)
                            BigDecimal salary,
                            @NotNull
                            EmploymentPosition position,
                            @NotNull @Min(value = Validation.WORK_EXPERIENCE_MIN, message = ValidationMessage.WORK_EXPERIENCE_MESSAGE)
                            @Schema(example = "18")
                            Integer workExperienceTotal,
                            @NotNull @Min(value = Validation.WORK_EXPERIENCE_MIN, message = ValidationMessage.WORK_EXPERIENCE_MESSAGE)
                            @Schema(example = "3")
                            Integer workExperienceCurrent) {
}
