package ru.chainichek.neostudy.deal.dto.statement;

import ru.chainichek.neostudy.deal.model.client.EmploymentStatus;
import ru.chainichek.neostudy.deal.model.client.EmploymentPosition;

import java.math.BigDecimal;

public record EmploymentDto(EmploymentStatus employmentStatus,
                            String employerINN,
                            BigDecimal salary,
                            EmploymentPosition position,
                            Integer workExperienceTotal,
                            Integer workExperienceCurrent) {
}
