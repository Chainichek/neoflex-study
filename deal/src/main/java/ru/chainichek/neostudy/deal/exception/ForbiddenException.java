package ru.chainichek.neostudy.deal.exception;

import ru.chainichek.neostudy.deal.model.statement.ApplicationStatus;

import java.util.UUID;

public class ForbiddenException extends RuntimeException{
    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException(String message, UUID id, ApplicationStatus status) {
        super("%s: id = %s, status = %s".formatted(message, id, status));
    }
}
