package ru.chainichek.neostudy.deal.model.client;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class Passport {

    private UUID id = UUID.randomUUID();

    private String series;

    private String number;

    private String issueBranch;

    private LocalDate issueDate;

    public Passport(String series,
                    String number) {
        this.series = series;
        this.number = number;
    }
}
