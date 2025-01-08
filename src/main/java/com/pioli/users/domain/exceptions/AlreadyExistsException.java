package com.pioli.users.domain.exceptions;

public class AlreadyExistsException extends DomainException {
    public AlreadyExistsException(String message) {
        super(message);
    }
}