package ru.chainichek.neostudy.deal.dto.statement;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.chainichek.neostudy.deal.model.statement.ApplicationStatus;
import ru.chainichek.neostudy.deal.model.statement.ChangeType;

import java.time.LocalDateTime;

import static ru.chainichek.neostudy.deal.util.validation.Validation.DATE_TIME_FORMAT_PATTERN;

public record StatementStatusHistoryDto(ApplicationStatus status,
                                        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT_PATTERN)
                                        LocalDateTime time,
                                        ChangeType changeType) {
    public StatementStatusHistoryDto(ApplicationStatus status) {
        this(status, LocalDateTime.now(), ChangeType.AUTOMATIC);
    }
}