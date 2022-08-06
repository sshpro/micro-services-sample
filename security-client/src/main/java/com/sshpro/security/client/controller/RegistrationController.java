package com.sshpro.security.client.controller;

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
import com.sshpro.security.client.dto.PasswordDto;
import com.sshpro.security.client.dto.UserDto;
import com.sshpro.security.client.entity.User;
import com.sshpro.security.client.event.RegistrationCompleteEvent;
import com.sshpro.security.client.service.RegistrationService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class RegistrationController {
    private final RegistrationService service;
    private final ApplicationEventPublisher applicationEventPublisher;

    public RegistrationController(@Autowired RegistrationService service,
            @Autowired ApplicationEventPublisher applicationEventPublisher,
            @Autowired MessageSource messageSource) {
        this.service = service;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @PostMapping("/register")
    public BaseResponse register(
            @Valid @RequestBody UserDto userDto, final HttpServletRequest request) {
        User user = service.register(userDto);
        sendRegistrationToken(user, request);
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
        User user = service.findUserByEmail(email);
        sendRegistrationToken(user, request);
        return new BaseResponse("Registration token resend successful");
    }

    private void sendRegistrationToken(final User user, final HttpServletRequest request) {
        final String token = UUID.randomUUID().toString();
        final String registrationUrl = getRegistrationUrl(request, token);
        service.saveRegistrationToken(user, token);
        applicationEventPublisher.publishEvent(new RegistrationCompleteEvent(user.getEmail(), registrationUrl));
    }

    @GetMapping("/resetPassword")
    public BaseResponse resetPassword(final HttpServletRequest request,
            @RequestParam("email") final String userEmail) {
        User user = service.findUserByEmail(userEmail);
        sendPasswordResetToken(user, request);
        return new BaseResponse("Reset Password token sent");
    }

    private void sendPasswordResetToken(final User user, final HttpServletRequest request) {
        final String token = UUID.randomUUID().toString();
        final String resetPasswordUrl = getResetPasswordUrl(request, token);
        service.savePasswordResetToken(user, token);
        log.info("Password reset: " + resetPasswordUrl);
    }

    @PostMapping("/savePassword")
    public BaseResponse savePassword(final HttpServletRequest request,
            @RequestParam("token") String token,
            @Valid @RequestBody PasswordDto passwordDto) {
        service.validatePasswordResetToken(token);
        service.validatePassword(passwordDto.getEmail(), passwordDto.getOldPassword());
        service.savePassword(token, passwordDto.getNewPassword());
        return new BaseResponse("Password save successful");
    }

    @PostMapping("/changePassword")
    public BaseResponse changePassword(final HttpServletRequest request,
            @Valid @RequestBody PasswordDto passwordDto) {
        service.validatePassword(passwordDto.getEmail(), passwordDto.getOldPassword());
        User user = service.findUserByEmail(passwordDto.getEmail());
        service.savePassword(user, passwordDto.getNewPassword());
        return new BaseResponse("Password change successful");
    }

    private String getRegistrationUrl(HttpServletRequest request, String token) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() +
                "/verifyRegistration?token=" + token;
    }

    private String getResetPasswordUrl(HttpServletRequest request, String token) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() +
                "/savePassword?token=" + token;
    }

}
