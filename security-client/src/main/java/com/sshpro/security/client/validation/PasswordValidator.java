package com.sshpro.security.client.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.sshpro.security.client.annotations.ValidPassword;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {
    private static final String EMAIL_PATTERN = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,30}$";
    private static final Pattern PATTERN = Pattern.compile(EMAIL_PATTERN);
    
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
         Matcher matcher = PATTERN.matcher(password);
        return matcher.matches();
    }

}
