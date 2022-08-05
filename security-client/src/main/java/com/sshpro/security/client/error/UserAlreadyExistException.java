package com.sshpro.security.client.error;

public final class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException(final String message) {
        super(message);
    }
}
