package ru.chainichek.neostudy.deal.model.client;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

import static ru.chainichek.neostudy.deal.util.validation.client.EmploymentValidation.INN_SIZE_MAX;

@Getter
@Setter
public class Employment {
    private UUID id = UUID.randomUUID();

    private EmploymentStatus status;

    @Size(min = INN_SIZE_MAX, max = INN_SIZE_MAX)
    private String employerInn;

    private BigDecimal salary;

    private EmploymentPosition position;

    private Integer workExperienceTotal;

    private Integer workExperienceCurrent;

    public Employment(EmploymentStatus status,
                      String employerInn,
                      BigDecimal salary,
                      EmploymentPosition position,
                      Integer workExperienceTotal,
                      Integer workExperienceCurrent) {
        this.status = status;
        this.employerInn = employerInn;
        this.salary = salary;
        this.position = position;
        this.workExperienceTotal = workExperienceTotal;
        this.workExperienceCurrent = workExperienceCurrent;
    }
}