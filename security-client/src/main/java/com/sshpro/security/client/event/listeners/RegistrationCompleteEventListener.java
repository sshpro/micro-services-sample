package com.sshpro.security.client.event.listeners;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.sshpro.security.client.event.RegistrationCompleteEvent;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent>{


    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        //TODO send email
        log.info("####" + event.getRegistrationUrl());
        
    }
    
}
