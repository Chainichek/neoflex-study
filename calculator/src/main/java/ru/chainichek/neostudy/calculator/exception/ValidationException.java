package ru.chainichek.neostudy.calculator.exception;

public class ValidationException extends RuntimeException{
    public ValidationException(String message) {
        super(message);
    }
}
