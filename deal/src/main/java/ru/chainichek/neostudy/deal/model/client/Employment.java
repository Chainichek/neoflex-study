package ru.chainichek.neostudy.deal.model.client;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class Employment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private EmploymentStatus status;

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