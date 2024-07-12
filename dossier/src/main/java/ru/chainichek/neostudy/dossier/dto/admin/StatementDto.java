package ru.chainichek.neostudy.dossier.dto.admin;

import jakarta.validation.constraints.NotNull;
import ru.chainichek.neostudy.dossier.dto.offer.LoanOfferDto;
import ru.chainichek.neostudy.dossier.dto.statement.StatementStatusHistoryDto;
import ru.chainichek.neostudy.dossier.model.statement.ApplicationStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record StatementDto(
        UUID id,
        @NotNull
        ClientDto client,
        @NotNull
        CreditDto credit,
        ApplicationStatus status,
        LocalDateTime creationDate,
        LoanOfferDto appliedOffer,
        LocalDateTime signDate,
        String sesCode,
        List<StatementStatusHistoryDto> statusHistory
) {}