package com.sshpro.security.client.dto;

import com.sshpro.security.client.annotations.ValidEmail;
import com.sshpro.security.client.annotations.ValidPassword;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    
    @ValidEmail
    private String email;

    @ValidPassword
    private String password;
}
