package ru.chainichek.neostudy.deal.dto.statement;

import ru.chainichek.neostudy.deal.model.EmploymentStatus;
import ru.chainichek.neostudy.deal.model.Position;

import java.math.BigDecimal;

public record EmploymentDto(EmploymentStatus employmentStatus,
                            String employerINN,
                            BigDecimal salary,
                            Position position,
                            Integer workExperienceTotal,
                            Integer workExperienceCurrent) {
}
