package ru.chainichek.neostudy.calculator.util;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import ru.chainichek.neostudy.calculator.dto.util.ErrorMessage;
import ru.chainichek.neostudy.calculator.dto.util.InternalErrorMessage;
import ru.chainichek.neostudy.calculator.exception.ForbiddenException;
import ru.chainichek.neostudy.calculator.exception.ValidationException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler{
    private final static Logger LOG = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorMessage> forbiddenException(ForbiddenException exception,
                                                                     HttpServletRequest request) {
        final ErrorMessage message = new ErrorMessage(LocalDateTime.now(),
                HttpStatus.FORBIDDEN.getReasonPhrase(),
                HttpStatus.FORBIDDEN.value(),
                exception.getMessage(),
                request.getRequestURI());

        LOG.error(exception.getMessage(), exception);

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(message);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorMessage> handlerMethodValidationException(HandlerMethodValidationException exception,
                                                                         HttpServletRequest request) {
        final List<String> errors = new ArrayList<>();
        for (ParameterValidationResult parameterValidationResult : exception.getAllValidationResults()) {
            for (MessageSourceResolvable messageError : parameterValidationResult.getResolvableErrors()) {
                if (messageError instanceof FieldError) {
                    errors.add("%s = %s: %s".formatted(((FieldError) messageError).getField(), ((FieldError) messageError).getRejectedValue(), messageError.getDefaultMessage()));
                }
            }
        }
        final ErrorMessage message = new ErrorMessage(LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                HttpStatus.BAD_REQUEST.value(),
                errors.toString(),
                request.getRequestURI());

        LOG.error("%s: %s".formatted(exception.getMessage(), errors.toString()), exception);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(message);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorMessage> validationException(ValidationException exception,
                                                            HttpServletRequest request) {
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
    public ResponseEntity<InternalErrorMessage> otherException(RuntimeException exception,
                                                               HttpServletRequest request) {
        final InternalErrorMessage message = new InternalErrorMessage(LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                exception.getMessage(),
                Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).toArray(String[]::new),
                request.getRequestURI());

        LOG.error(exception.getMessage(), exception);
        LOG.warn("Unexpected exception: %s".formatted(exception));

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(message);
    }
}
