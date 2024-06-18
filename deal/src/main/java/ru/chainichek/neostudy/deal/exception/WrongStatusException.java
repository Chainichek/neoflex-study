package ru.chainichek.neostudy.deal.exception;

import ru.chainichek.neostudy.deal.model.statement.ApplicationStatus;

import java.util.UUID;

public class WrongStatusException extends RuntimeException {
    public WrongStatusException(String message, UUID id, ApplicationStatus status) {
        super("%s: id = %s, status = %s".formatted(message, id, status));
    }
}
