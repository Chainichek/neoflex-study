package ru.chainichek.neostudy.calculator.domain.entity.dto.score;

import ru.chainichek.neostudy.calculator.domain.entity.EmploymentStatus;
import ru.chainichek.neostudy.calculator.domain.entity.Position;

import java.math.BigDecimal;

public record EmploymentDto(EmploymentStatus employmentStatus,
                            String employerINN,
                            BigDecimal salary,
                            Position position,
                            Integer workExperienceTotal,
                            Integer workExperienceCurrent) {
}
