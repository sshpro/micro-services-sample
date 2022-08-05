package com.sshpro.security.client.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.sshpro.security.client.entity.User;
import com.sshpro.security.client.repository.UserRepository;

public class RegistrationUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public RegistrationUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("No user found for this: " + username);
        }

        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;

        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword().toLowerCase(),
                enabled,
                accountNonExpired,
                credentialsNonExpired,
                accountNonLocked,
                getAuthorities(user.getRole()));

    }

    private static List<GrantedAuthority> getAuthorities(String role) {
        return new ArrayList<GrantedAuthority>() {
            {
                add(new SimpleGrantedAuthority(role));
            }
        };
    }

}
