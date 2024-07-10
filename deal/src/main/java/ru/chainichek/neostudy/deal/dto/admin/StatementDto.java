package ru.chainichek.neostudy.deal.dto.admin;


import ru.chainichek.neostudy.deal.dto.offer.LoanOfferDto;
import ru.chainichek.neostudy.deal.dto.statement.StatementStatusHistoryDto;
import ru.chainichek.neostudy.deal.model.statement.ApplicationStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record StatementDto(
        UUID id,
        ClientDto client,
        CreditDto credit,
        ApplicationStatus status,
        LocalDateTime creationDate,
        LoanOfferDto appliedOffer,
        LocalDateTime signDate,
        String sesCode,
        List<StatementStatusHistoryDto> statusHistory
) {}