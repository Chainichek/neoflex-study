package ru.chainichek.neostudy.deal.exception;

import java.util.UUID;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message, UUID id) {
        super("%s: id = %s".formatted(message, id));
    }
}
