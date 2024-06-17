package ru.chainichek.neostudy.deal.dto.util;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

import static ru.chainichek.neostudy.deal.util.validation.Validation.DATE_TIME_FORMAT_PATTERN;

public record ErrorMessage(@JsonFormat(pattern = DATE_TIME_FORMAT_PATTERN)
                           LocalDateTime timestamp,
                           String title,
                           int status,
                           String detail,
                           String instance) {
}
