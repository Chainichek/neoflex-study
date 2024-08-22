package ru.chainichek.neostudy.calculator.dto.util;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.chainichek.neostudy.calculator.util.Validation;

import java.time.LocalDateTime;

public record InternalErrorMessage(@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Validation.DATE_TIME_FORMAT_PATTERN)
                                   LocalDateTime timestamp,
                                   String title,
                                   int status,
                                   String detail,
                                   String[] stackTrace,
                                   String instance) {
}
