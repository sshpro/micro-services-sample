package com.sshpro.user.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sshpro.user.entity.User;
import com.sshpro.user.service.UserService;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/users")
    public User save(@Valid @RequestBody User user) {
        return userService.save(user);
    }

    @GetMapping("/users")
    public List<User> get() {
        return userService.get();
    }

    @GetMapping("/users/{id}")
    public User getById(@PathVariable("id") long id) {
        return userService.getById(id);
    }

    @DeleteMapping("/users/{id}")
    public String deleteById(@PathVariable("id") long id) {
        return userService.deleteById(id);
    }

    @PutMapping("/users/{id}")
    public User update(@PathVariable("id") Long id, @RequestBody User user) {
        return userService.update(id, user);
    }
}
