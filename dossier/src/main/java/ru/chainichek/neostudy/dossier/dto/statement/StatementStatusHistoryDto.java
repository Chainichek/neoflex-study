package ru.chainichek.neostudy.dossier.dto.statement;

import ru.chainichek.neostudy.dossier.model.statement.ApplicationStatus;
import ru.chainichek.neostudy.dossier.model.statement.ChangeType;

import java.time.LocalDateTime;

public record StatementStatusHistoryDto(ApplicationStatus status,
                                        LocalDateTime time,
                                        ChangeType changeType) {
}