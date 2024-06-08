package ru.chainichek.neostudy.deal.model.client;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class Passport {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String series;

    private String number;

    private String issueBranch;

    private LocalDate issueDate;

    public Passport(String series,
                    String number,
                    String issueBranch,
                    LocalDate issueDate) {
        this.series = series;
        this.number = number;
        this.issueBranch = issueBranch;
        this.issueDate = issueDate;
    }
}
