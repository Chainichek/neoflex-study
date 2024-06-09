package ru.chainichek.neostudy.deal.model.statement;

import java.time.LocalDateTime;

public record StatusHistory(ApplicationStatus status,
                            LocalDateTime time,
                            ChangeType changeType) {
    public StatusHistory(ApplicationStatus status) {
        this(status, LocalDateTime.now(), ChangeType.AUTOMATIC);
    }
}
