package com.sshpro.security.client.error;

public class InvalidTokenException  extends RuntimeException {
    public InvalidTokenException(final String message) {
        super(message);
    }
}