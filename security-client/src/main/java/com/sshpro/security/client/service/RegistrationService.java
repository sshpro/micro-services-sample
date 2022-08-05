package com.sshpro.security.client.service;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.sshpro.security.client.dto.UserDto;
import com.sshpro.security.client.entity.User;

@Service
public interface RegistrationService {

    User register(@Valid UserDto userDto);

    void saveRegistrationToken(User user, String token);

    boolean validateRegistrationToken(String token);

    User findUser(String email);

    // String generatePasswordVerificationToken(String userEmail);

    // void savePassword(String token, String newPassword);

    // void updatePassword(String oldPassword, String newPassword);

}
