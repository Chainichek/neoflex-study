package ru.chainichek.neostudy.deal.dto.statement;

import ru.chainichek.neostudy.deal.model.statement.ApplicationStatus;
import ru.chainichek.neostudy.deal.model.statement.ChangeType;

import java.time.LocalDateTime;

public record StatementStatusHistoryDto(ApplicationStatus status,
                             LocalDateTime time,
                             ChangeType changeType) {
    public StatementStatusHistoryDto(ApplicationStatus status) {
        this(status, LocalDateTime.now(), ChangeType.AUTOMATIC);
    }
}