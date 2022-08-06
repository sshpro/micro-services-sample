package com.sshpro.security.client.service;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sshpro.security.client.dto.UserDto;
import com.sshpro.security.client.entity.PasswordResetToken;
import com.sshpro.security.client.entity.User;
import com.sshpro.security.client.entity.VerificationToken;
import com.sshpro.security.client.error.IncorrectPasswordException;
import com.sshpro.security.client.error.InvalidTokenException;
import com.sshpro.security.client.error.UserAlreadyExistException;
import com.sshpro.security.client.repository.PasswordResetTokenRepository;
import com.sshpro.security.client.repository.UserRepository;
import com.sshpro.security.client.repository.VerificationTokenRepository;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passcodeEncoder;

    public RegistrationServiceImpl(@Autowired UserRepository userRepository,
            @Autowired VerificationTokenRepository verificationTokenRepository,
            @Autowired PasswordResetTokenRepository passwordResetTokenRepository,
            @Autowired PasswordEncoder passcodeEncoder) {
        this.userRepository = userRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.passcodeEncoder = passcodeEncoder;
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
    public User findUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    @Override
    public void savePasswordResetToken(User user, String token) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByUserId(user.getId());
        if (passwordResetToken != null) {
            passwordResetTokenRepository.delete(passwordResetToken);
        }
        long current = System.currentTimeMillis();
        long expirationDateInMillis = current + (10 * 60 * 1000L);
        passwordResetToken = new PasswordResetToken(token, expirationDateInMillis, user);
        passwordResetTokenRepository.save(passwordResetToken);
    }

    @Override
    public void savePassword(String token, String newPassword) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        if (passwordResetToken == null) {
            throw new InvalidTokenException("Invalid password reset token");
        }
        User user = passwordResetToken.getUser();
        savePassword(user, newPassword);
        passwordResetTokenRepository.delete(passwordResetToken);
    }

    @Override
    public void validatePasswordResetToken(String token) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        if (passwordResetToken == null) {
            throw new InvalidTokenException("Invalid password reset token");
        }
        long expirationDate = passwordResetToken.getExpirationDateInMillis();
        long currentTime = System.currentTimeMillis();
        if (expirationDate < currentTime) {
            passwordResetTokenRepository.delete(passwordResetToken);
            throw new InvalidTokenException("Password reset token expired");
        }
    }

    @Override
    public void validatePassword(String email, String password) {
        User user = findUserByEmail(email);
        if (!passcodeEncoder.matches(password, user.getPassword())) {
            throw new IncorrectPasswordException("Incorrect password");
        }
    }

    @Override
    public void savePassword(User user, String newPassword) {
        user.setPassword(passcodeEncoder.encode(newPassword));
        userRepository.save(user);
    }

}
