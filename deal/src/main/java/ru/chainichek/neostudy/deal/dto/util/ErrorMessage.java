package ru.chainichek.neostudy.deal.dto.util;

import java.time.LocalDateTime;

public record ErrorMessage(LocalDateTime timestamp,
                           String title,
                           int status,
                           String detail,
                           String instance) {
}
