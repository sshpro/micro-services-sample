package com.sshpro.security.client.dto;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse { 
    private final HttpStatus status;
    private final String message;
}
