package com.pioli.users.domain.exceptions;

public class RequiredParameterException extends DomainException {
    public RequiredParameterException(String message) {
        super(message);
    }
}