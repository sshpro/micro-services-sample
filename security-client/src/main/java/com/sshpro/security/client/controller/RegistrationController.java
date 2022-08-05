package com.sshpro.security.client.controller;

import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sshpro.security.client.dto.BaseResponse;
import com.sshpro.security.client.dto.UserDto;
import com.sshpro.security.client.entity.User;
import com.sshpro.security.client.event.RegistrationCompleteEvent;
import com.sshpro.security.client.service.RegistrationService;

@RestController
public class RegistrationController {
    private final RegistrationService service;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final MessageSource messageSource;

    public RegistrationController(@Autowired RegistrationService service,
            @Autowired ApplicationEventPublisher applicationEventPublisher,
            @Autowired MessageSource messageSource) {
        this.service = service;
        this.applicationEventPublisher = applicationEventPublisher;
        this.messageSource = messageSource;
    }

    @PostMapping("/register")
    public BaseResponse register(
            @Valid @RequestBody UserDto userDto, final HttpServletRequest request) {
        User user = service.register(userDto);
        saveRegistrationToken(user, request);
        return new BaseResponse("Registration is successful");
    }

    @GetMapping("/verifyRegistration")
    public BaseResponse verifyRegistration(final HttpServletRequest request,
            @RequestParam("token") String token) {
        service.validateRegistrationToken(token);
        return new BaseResponse("Registration verification is successful");
    }

    @GetMapping("/resendRegistrationToken")
    public BaseResponse resendRegistrationToken(final HttpServletRequest request,
            @RequestParam("email") String email) {
        User user = service.findUser(email);
        saveRegistrationToken(user, request);
        return new BaseResponse("Registration token resend successful");
    }

    private void saveRegistrationToken(final User user, final HttpServletRequest request) {
        final String token = UUID.randomUUID().toString();
        final String registrationUrl = getRegistrationUrl(request, token);
        service.saveRegistrationToken(user, token);
        applicationEventPublisher.publishEvent(new RegistrationCompleteEvent(user.getEmail(), registrationUrl));
    }

    // @PostMapping("/resetPassword")
    // public BaseResponse resetPassword(final HttpServletRequest request,
    // @RequestParam("email") final String userEmail) {
    // String token = service.generatePasswordVerificationToken(userEmail);
    // return new BaseResponse(getMessage("message.resetPasswordEmail",
    // request.getLocale()));
    // }

    // @PostMapping("/savePassword")
    // public BaseResponse savePassword(final HttpServletRequest request,
    // @Valid PasswordDto passwordDto) {
    // service.savePassword(passwordDto.getToken(), passwordDto.getNewPassword());
    // return new BaseResponse(getMessage("message.resetPasswordEmail",
    // request.getLocale()));
    // }

    // @PostMapping("/updatePassword")
    // public BaseResponse updatePassword(final HttpServletRequest request,
    // @Valid PasswordDto passwordDto) {
    // service.updatePassword(passwordDto.getOldPassword(),
    // passwordDto.getNewPassword());
    // return new BaseResponse(getMessage("message.updatePassword",
    // request.getLocale()));
    // }

    private String getRegistrationUrl(HttpServletRequest request, String token) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() +
                "/verifyRegistration?token=" + token;
    }

    private String getMessage(String code, Locale locale) {
        return messageSource.getMessage(code, null, locale);
    }

}
