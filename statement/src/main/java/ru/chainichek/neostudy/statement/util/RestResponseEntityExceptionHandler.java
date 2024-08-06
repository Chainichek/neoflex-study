package ru.chainichek.neostudy.statement.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import ru.chainichek.neostudy.statement.dto.util.ErrorMessage;
import ru.chainichek.neostudy.statement.dto.util.InternalErrorMessage;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class RestResponseEntityExceptionHandler {
    private final ObjectMapper mapper;


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> httpMessageNotReadableException(@NonNull HttpMessageNotReadableException exception,
                                                                  @NonNull HttpServletRequest request) {
        final ErrorMessage message = new ErrorMessage(LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                HttpStatus.BAD_REQUEST.value(),
                exception.getMessage(),
                request.getRequestURI());

        log.error(exception.getMessage(), exception);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(message);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorMessage> handlerMethodValidationException(@NonNull HandlerMethodValidationException exception,
                                                                         @NonNull HttpServletRequest request) {
        final List<String> errors = exception.getAllValidationResults().stream()
                .flatMap(result -> result.getResolvableErrors().stream())
                .filter(messageError -> messageError instanceof ObjectError)
                .map(messageError -> {
                    if (messageError instanceof FieldError fieldError) {
                        return "%s = %s: %s".formatted(
                                fieldError.getField(),
                                fieldError.getRejectedValue(),
                                fieldError.getDefaultMessage());
                    }

                    return messageError.getDefaultMessage();
                })
                .toList();

        final ErrorMessage message = new ErrorMessage(LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                HttpStatus.BAD_REQUEST.value(),
                errors.toString(),
                request.getRequestURI());

        log.error("%s: %s".formatted(exception.getMessage(), errors.toString()), exception);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(message);
    }

    @ExceptionHandler(FeignException.BadRequest.class)
    public ResponseEntity<ErrorMessage> badRequestException(@NonNull FeignException.BadRequest exception,
                                                            @NonNull HttpServletRequest request) {
        final ErrorMessage message = new ErrorMessage(LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                HttpStatus.BAD_REQUEST.value(),
                exception.getMessage(),
                request.getRequestURI());

        log.error(exception.getMessage(), exception);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(message);
    }

    @ExceptionHandler(FeignException.Forbidden.class)
    public ResponseEntity<ErrorMessage> forbiddenException(@NonNull FeignException.Forbidden exception,
                                                           @NonNull HttpServletRequest request) {
        final ErrorMessage message = new ErrorMessage(LocalDateTime.now(),
                HttpStatus.FORBIDDEN.getReasonPhrase(),
                HttpStatus.FORBIDDEN.value(),
                exception.getMessage(),
                request.getRequestURI());

        log.error(exception.getMessage(), exception);

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(message);
    }

    @ExceptionHandler(FeignException.NotFound.class)
    public ResponseEntity<ErrorMessage> notFoundException(@NonNull FeignException.NotFound exception,
                                                          @NonNull HttpServletRequest request) {
        final ErrorMessage message = new ErrorMessage(LocalDateTime.now(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                HttpStatus.NOT_FOUND.value(),
                exception.getMessage(),
                request.getRequestURI());

        log.error(exception.getMessage(), exception);

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(message);
    }

    @SneakyThrows
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<?> feignException(@NonNull FeignException exception,
                                            @NonNull HttpServletRequest request) {

        if (exception.status() >= 500 && exception.status() < 600) {
            final InternalErrorMessage message = mapper.readValue(exception.contentUTF8(), InternalErrorMessage.class);
            log.error(LogMessage.FEIGN_UNEXPECTED_EXCEPTION_LOG_MESSAGE, exception.request().url(), message);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(message);
        }

        if (exception.status() >= 400) {
            final ErrorMessage message = mapper.readValue(exception.contentUTF8(), ErrorMessage.class);
            log.error(LogMessage.FEIGN_EXCEPTION_LOG_MESSAGE, exception.request().url(), message);

            if (exception.status() == 412) {
                return ResponseEntity
                        .status(HttpStatus.PRECONDITION_FAILED)
                        .body(message);
            }
        }

        final InternalErrorMessage message = new InternalErrorMessage(LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                exception.getMessage(),
                Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).toArray(String[]::new),
                request.getRequestURI());

        log.error(exception.getMessage(), exception);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(message);

    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<InternalErrorMessage> otherException(@NonNull RuntimeException exception,
                                                               @NonNull HttpServletRequest request) {
        final InternalErrorMessage message = new InternalErrorMessage(LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                exception.getMessage(),
                Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).toArray(String[]::new),
                request.getRequestURI());

        log.error(exception.getMessage(), exception);
        log.warn("Unexpected exception: %s".formatted(exception));

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(message);
    }

    private static final class LogMessage {
        public static final String FEIGN_UNEXPECTED_EXCEPTION_LOG_MESSAGE = "Resolved an unexpected error: path = {}, message = {}";
        public static final String FEIGN_EXCEPTION_LOG_MESSAGE = "Resolved an error: path = {}, message = {}";

    }
}
