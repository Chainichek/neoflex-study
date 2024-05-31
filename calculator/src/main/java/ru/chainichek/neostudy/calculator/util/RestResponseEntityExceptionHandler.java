package ru.chainichek.neostudy.calculator.util;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import ru.chainichek.neostudy.calculator.dto.util.ErrorMessage;
import ru.chainichek.neostudy.calculator.exception.UnprocessableEntityException;
import ru.chainichek.neostudy.calculator.exception.ValidationException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler{
    private final static Logger LOG = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);

    @ExceptionHandler(UnprocessableEntityException.class)
    public ResponseEntity<ErrorMessage> unprocessableEntityException(final UnprocessableEntityException exception,
                                                            final HttpServletRequest request) {
        final ErrorMessage message = new ErrorMessage(LocalDateTime.now(),
                HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(),
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                exception.getMessage(),
                request.getRequestURI());

        LOG.error(exception.getMessage(), exception);

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(message);
    }

    @ExceptionHandler({ValidationException.class,
            HandlerMethodValidationException.class})
    public ResponseEntity<ErrorMessage> validationException(final RuntimeException exception,
                                                            final HttpServletRequest request) {
        final ErrorMessage message = new ErrorMessage(LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                HttpStatus.BAD_REQUEST.value(),
                exception.getMessage(),
                request.getRequestURI());

        LOG.error(exception.getMessage(), exception);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(message);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorMessage> otherException(final RuntimeException exception,
                                                       final HttpServletRequest request) {
        final ErrorMessage message = new ErrorMessage(LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                exception.getMessage(),
                request.getRequestURI());

        LOG.error(exception.getMessage(), exception);
        LOG.warn("Unexpected exception: %s".formatted(exception));

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(message);
    }
}
