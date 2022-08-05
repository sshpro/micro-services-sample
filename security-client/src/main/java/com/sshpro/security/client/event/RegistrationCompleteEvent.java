package com.sshpro.security.client.event;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

@Getter
public class RegistrationCompleteEvent extends ApplicationEvent {
    private final String email;
    private final String registrationUrl;

    public RegistrationCompleteEvent(String email, String registrationUrl) {
        super(email);
        this.email = email;
        this.registrationUrl = registrationUrl;
    }
}
