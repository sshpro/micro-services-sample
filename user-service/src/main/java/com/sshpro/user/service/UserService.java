package com.sshpro.user.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sshpro.user.entity.User;

@Service
public interface UserService {

    User save(User user);

    User getById(Long id);

    List<User> get();

    String deleteById(Long id);

    User update(Long id, User user);

}
