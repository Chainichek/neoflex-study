package ru.chainichek.neostudy.deal.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import ru.chainichek.neostudy.deal.dto.util.ErrorMessage;
import ru.chainichek.neostudy.deal.dto.util.InternalErrorMessage;
import ru.chainichek.neostudy.deal.exception.ForbiddenException;
import ru.chainichek.neostudy.deal.exception.NotFoundException;
import ru.chainichek.neostudy.deal.exception.WrongStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestControllerAdvice
@AllArgsConstructor
public class RestResponseEntityExceptionHandler {
    private final static Logger LOG = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);
    private final ObjectMapper mapper;

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> httpMessageNotReadableException(HttpMessageNotReadableException exception,
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

    @ExceptionHandler(WrongStatusException.class)
    public ResponseEntity<ErrorMessage> wrongStatusException(WrongStatusException exception,
                                                             HttpServletRequest request) {
        final ErrorMessage message = new ErrorMessage(LocalDateTime.now(),
                HttpStatus.PRECONDITION_FAILED.getReasonPhrase(),
                HttpStatus.PRECONDITION_FAILED.value(),
                exception.getMessage(),
                request.getRequestURI());

        LOG.error(exception.getMessage(), exception);

        return ResponseEntity
                .status(HttpStatus.PRECONDITION_FAILED)
                .body(message);
    }

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

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMessage> notFoundException(NotFoundException exception,
                                                          HttpServletRequest request) {
        final ErrorMessage message = new ErrorMessage(LocalDateTime.now(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                HttpStatus.NOT_FOUND.value(),
                exception.getMessage(),
                request.getRequestURI());

        LOG.error(exception.getMessage(), exception);

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(message);
    }

    @SneakyThrows
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<?> feignException(FeignException exception,
                                            HttpServletRequest request) {

        if (exception.status() >= 500 && exception.status() < 600) {
            final InternalErrorMessage message = mapper.readValue(exception.contentUTF8(), InternalErrorMessage.class);
            LOG.error(LogMessage.FEIGN_UNEXPECTED_EXCEPTION_LOG_MESSAGE, exception.request().url(), message);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(message);
        }

        if (exception.status() >= 400) {
            final ErrorMessage message = mapper.readValue(exception.contentUTF8(), ErrorMessage.class);
            LOG.error(LogMessage.FEIGN_EXCEPTION_LOG_MESSAGE, exception.request().url(), message);

            switch (exception.status()) {
                case 400 -> {
                    return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body(message);
                }
                case 403 -> {
                    return ResponseEntity
                            .status(HttpStatus.FORBIDDEN)
                            .body(message);
                }
            }
        }

        final InternalErrorMessage message = new InternalErrorMessage(LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                exception.getMessage(),
                Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).toArray(String[]::new),
                request.getRequestURI());

        LOG.error(exception.getMessage(), exception);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
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
        LOG.warn(LogMessage.UNEXPECTED_EXCEPTION_LOG_MESSAGE, exception);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(message);
    }

    private static final class LogMessage {
        public static final String FEIGN_UNEXPECTED_EXCEPTION_LOG_MESSAGE = "Resolved an unexpected error: path = {}, message = {}";
        public static final String FEIGN_EXCEPTION_LOG_MESSAGE = "Resolved an error: path = {}, message = {}";
        public static final String UNEXPECTED_EXCEPTION_LOG_MESSAGE = "Unexpected exception: {}";

    }

}
