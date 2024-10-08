package ru.chainichek.neostudy.deal.model.client;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

import static ru.chainichek.neostudy.deal.util.validation.client.PassportValidation.PASSPORT_NUMBER_SIZE_MAX;
import static ru.chainichek.neostudy.deal.util.validation.client.PassportValidation.PASSPORT_SERIES_SIZE_MAX;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Passport {

    private UUID id = UUID.randomUUID();

    @Size(min = PASSPORT_SERIES_SIZE_MAX, max = PASSPORT_SERIES_SIZE_MAX)
    private String series;

    @Size(min = PASSPORT_NUMBER_SIZE_MAX, max = PASSPORT_NUMBER_SIZE_MAX)
    private String number;

    private String issueBranch;

    private LocalDate issueDate;
}
