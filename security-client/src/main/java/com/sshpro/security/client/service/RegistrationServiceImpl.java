package com.sshpro.security.client.service;

import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sshpro.security.client.dto.UserDto;
import com.sshpro.security.client.entity.User;
import com.sshpro.security.client.entity.VerificationToken;
import com.sshpro.security.client.error.InvalidTokenException;
import com.sshpro.security.client.error.UserAlreadyExistException;
import com.sshpro.security.client.repository.UserRepository;
import com.sshpro.security.client.repository.VerificationTokenRepository;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordEncoder passcodeEncoder;
    private final MessageSource messageSource;

    public RegistrationServiceImpl(@Autowired UserRepository userRepository,
            @Autowired VerificationTokenRepository verificationTokenRepository,
            @Autowired PasswordEncoder passcodeEncoder,
            @Autowired MessageSource messageSource) {
        this.userRepository = userRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.passcodeEncoder = passcodeEncoder;
        this.messageSource = messageSource;
    }

    @Override
    public User register(@Valid UserDto userDto) {
        if (emailExists(userDto.getEmail())) {
            throw new UserAlreadyExistException("Account already exists with the email address: " + userDto.getEmail());
        }
        User user = User.builder()
                .password(passcodeEncoder.encode(userDto.getPassword()))
                .email(userDto.getEmail())
                .build();
        userRepository.save(user);
        return user;
    }

    private boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }

    private String getMessage(String code, Locale locale) {
        return messageSource.getMessage(code, null, locale);
    }

    @Override
    public void saveRegistrationToken(User user, String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByUserId(user.getId());
        if (verificationToken != null) {
            verificationTokenRepository.delete(verificationToken);
        }
        long current = System.currentTimeMillis();
        long expirationDateInMillis = current + (10 * 60 * 1000L);
        verificationToken = new VerificationToken(token, expirationDateInMillis, user);
        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public boolean validateRegistrationToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken == null) {
            throw new InvalidTokenException("Invalid token");
        }
        long expirationDate = verificationToken.getExpirationDateInMillis();
        long currentTime = System.currentTimeMillis();
        if (expirationDate < currentTime) {
            verificationTokenRepository.delete(verificationToken);
            throw new InvalidTokenException("Token expired");
        }
        User user = verificationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        return true;
    }

    @Override
    public User findUser(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;

    }

}
