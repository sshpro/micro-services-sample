package com.sshpro.user.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sshpro.user.entity.User;
import com.sshpro.user.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    UserServiceImpl(@Autowired UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getById(Long id) {
        Optional<User> found = userRepository.findById(id);
        if (!found.isPresent()) {
            throw new EntityNotFoundException("User not found for id: " + id);
        }
        return found.get();

    }

    @Override
    public List<User> get() {
        return userRepository.findAll();
    }

    @Override
    public String deleteById(Long id) {
        userRepository.deleteById(id);
        return "User deleted";
    }

    @Override
    public User update(Long id, User user) {
        User userDb = getById(id);
        if (Objects.nonNull(user.getUserName())) {
            userDb.setUserName(user.getUserName());
        }
        if (Objects.nonNull(user.getFirstName())) {
            userDb.setFirstName(user.getFirstName());
        }
        if (Objects.nonNull(user.getLastName())) {
            userDb.setLastName(user.getLastName());
        }
        if (Objects.nonNull(user.getEmail())) {
            userDb.setEmail(user.getEmail());
        }
        return userRepository.save(userDb);
    }

}
