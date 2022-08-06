package com.sshpro.security.client.error;

public class IncorrectPasswordException extends RuntimeException {
    public IncorrectPasswordException(final String message) {
        super(message);
    }
}