package ru.chainichek.neostudy.calculator.domain.exception;

public class ValidationException extends RuntimeException{
    public ValidationException(final String message) {
        super(message);
    }
}
